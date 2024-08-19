package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.models.CronData;
import com.deliveroo.renuka.parsers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CronTokenizerAndParserTest {

    CronTokenizerAndParser tokenizeAndParse;

    @BeforeEach
    void setUp() throws CronException {
        tokenizeAndParse = new CronTokenizerAndParser();
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 /usr/bin/find";
        CronData cronData = tokenizeAndParse.tokenizeExpression();
        tokenizeAndParse.cronData = cronData;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void tokenizeExpression() throws CronException {
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 /usr/bin/find";
        CronData actualOutput = tokenizeAndParse.tokenizeExpression();
        CronData expectedOutput = new CronData("*/15", "0", "1,15", "*", "1-5", "/usr/bin/find");
        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    void givenInvalidExpression_whentokenizeExpression_thenThrowCronException() {
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 ";
        Exception exception = Assertions.assertThrows(CronException.class, () -> tokenizeAndParse.tokenizeExpression());
        assertEquals("CRON_EXPRESSION : Cron expression has missing fields", exception.getMessage());
    }

    @Test
    void givenInvalidExpression_whenMulitpleCommands_thenReturnsCorrectOutput() throws CronException {
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 /usr/bin/find /usr/bin/find /usr/bin/find";
        CronData actualOutput = tokenizeAndParse.tokenizeExpression();
        CronData expectedOutput = new CronData("*/15", "0", "1,15", "*", "1-5", "/usr/bin/find /usr/bin/find /usr/bin/find");
        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    void testTokenizeExpression(){
    }

    @Test
    void parseExpression() throws CronException{
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 /usr/bin/find";
        tokenizeAndParse.cronData = tokenizeAndParse.tokenizeExpression();
        CronData actualOutput = tokenizeAndParse.parseExpression();
        CronData expectedOutput = new CronData(
                "0 15 30 45",
                 "0",
                "1 15",
                "1 2 3 4 5 6 7 8 9 10 11 12",
                "1 2 3 4 5",
                "/usr/bin/find");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidMinutesAndHours_whenParseExpression_thenThrowCumulativeExceptionMessages() throws CronException {
        tokenizeAndParse.cronExpression = "*/60 -1 1,15 * 1-5 /usr/bin/find";
        CronData cronData = tokenizeAndParse.tokenizeExpression();
        tokenizeAndParse.cronData = cronData;
        Exception exception = Assertions.assertThrows(CronException.class, () -> tokenizeAndParse.parseExpression());
        assertEquals("MINUTE : Invalid step\n" +
                "HOUR : Invalid range", exception.getMessage());

    }

    @Test
    void givenStarExpression_whenParseMinutes_thenReturnAllMinutes() throws CronException {
        String minutesExpression = "*";
        String expectedOutput = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 " +
                "32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59";
        String actualOutput = new MinutesParser(minutesExpression).parse();
        assertEquals(expectedOutput, actualOutput);

    }

    @Test
    void givenStarExpression_whenParseHours_thenReturnAllHours() throws CronException {
        String hoursExpression = "*";
        String expectedOutput = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23";
        String actualOutput = new HoursParser(hoursExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseDayOfMonth_thenReturnAllDaysOfMonth() throws CronException {
        String dayOfMonthExpression = "*";
        String expectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31";
        String actualOutput = new DayOfMonthParser(dayOfMonthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseMonth_thenReturnAllMonths() throws CronException {
        String monthExpression = "*";
        String expectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12";
        String actualOutput = new MonthParser(monthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseDayOfWeek_thenReturnAllDaysOfWeek() throws CronException {
        String dayOfWeekExpression = "*";
        String expectedOutput = "0 1 2 3 4 5 6";
        String actualOutput = new DayOfWeekParser(dayOfWeekExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseMinutes_thenReturnSpecificMinute() throws CronException {
        String minutesExpression = "5";
        String expectedOutput = "5";
        String actualOutput = new MinutesParser(minutesExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenRangeExpression_whenParseMinutes_thenReturnRangeOfMinutes() throws CronException {
        String minutesExpression = "10-20";
        String expectedOutput = "10 11 12 13 14 15 16 17 18 19 20";
        String actualOutput = new MinutesParser(minutesExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseMinutes_thenShouldThrowCronException() {
        String minutesExpression = "50-70";
        Assertions.assertThrows(CronException.class, () -> new MinutesParser(minutesExpression).parse());
    }

    @Test
    void givenTooManyRangesExpression_whenParseMinutes_thenShouldThrowCronExceptionWithMessageTooManyRanges(){
        String minutesExpression = "50-70-80";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MinutesParser(minutesExpression).parse());
        assertEquals("MINUTE : Invalid range", exception.getMessage());
    }

    @Test
    void givenListExpression_whenParseMinutes_thenReturnSpecificMinutes() throws CronException {
        String minutesExpression = "5,10,15";
        String expectedOutput = "5 10 15";
        String actualOutput = new MinutesParser(minutesExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseMinutes_thenShouldThrowCronExceptionWithMessageInvalidListRange() {
        String minutesExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MinutesParser(minutesExpression).parse());
        assertEquals("MINUTE : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStarSlashExpression_whenParseMinutes_thenReturnTimedMinutes() throws CronException {
        String minutesExpression = "*/15";
        String expectedOutput = "0 15 30 45";
        String actualOutput = new MinutesParser(minutesExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseMinutes_thenShouldThrowInvalidTimerException(){
        String minutesExpression = "*/0";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MinutesParser(minutesExpression).parse());
        assertEquals("MINUTE : Invalid step", exception.getMessage());
    }

    @Test
    void givenSingleValueExpression_whenParseHours_thenReturnSpecificHour() throws CronException {
        String hoursExpression = "3";
        String expectedOutput = "3";
        String actualOutput = new HoursParser(hoursExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenRangeExpression_whenParseHours_thenReturnRangeOfHours() throws CronException {
        String hoursExpression = "2-6";
        String expectedOutput = "2 3 4 5 6";
        String actualOutput = new HoursParser(hoursExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseHours_thenShouldThrowCronException(){
        String hoursExpression = "10-24";
        Assertions.assertThrows(CronException.class, () -> new HoursParser(hoursExpression).parse());
    }

    @Test
    void givenTooManyRangesExpression_whenParseHours_thenShouldThrowCronExceptionWithMessageTooManyRanges(){
        String hoursExpression = "50-70-80";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new HoursParser(hoursExpression).parse());
        assertEquals("HOUR : Invalid range", exception.getMessage());
    }


    @Test
    void givenListExpression_whenParseHours_thenReturnSpecificHours() throws CronException {
        String hoursExpression = "1,2,3";
        String expectedOutput = "1 2 3";
        String actualOutput = new HoursParser(hoursExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseHours_thenShouldThrowInvalidListExceptionWithMessageRangeOutOfBound(){
        String hoursExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new HoursParser(hoursExpression).parse());
        assertEquals("HOUR : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseHours_thenReturnSteppedHours() throws CronException {
        String hoursExpression = "*/4";
        String expectedOutput = "0 4 8 12 16 20";
        String actualOutput = new HoursParser(hoursExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseHours_thenShouldThrowInvalidException(){
        String hoursExpression = "*/0";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new HoursParser(hoursExpression).parse());
        assertEquals("HOUR : Invalid step", exception.getMessage());
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfMonth_thenReturnSpecificDay() throws CronException {
        String dayOfMonthExpression = "15";
        String expectedOutput = "15";
        String actualOutput = new DayOfMonthParser(dayOfMonthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfMonth_thenReturnSpecificMinute(){
        String dayOfMonthExpression = "32";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(dayOfMonthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid number", exception.getMessage());
    }

    @Test
    void givenRangeExpression_whenParseDayOfMonth_thenReturnRangeOfDays() throws CronException {
        String dayOfMonthExpression = "10-20";
        String expectedOutput = "10 11 12 13 14 15 16 17 18 19 20";
        String actualOutput = new DayOfMonthParser(dayOfMonthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenMultipleRangeExpression_whenParseDayOfMonth_thenShouldThrowMultipleRangeException() {
        String dayOfMonthExpression = "0-24-30";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(dayOfMonthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid range", exception.getMessage());
    }

    @Test
    void givenInvalidRangeExpression_whenParseDayOfMonth_thenShouldThrowCronException() {
        String dayOfMonthExpression = "0-35";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(dayOfMonthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid range", exception.getMessage());
    }

    @Test
    void givenListExpression_whenParseDayOfMonth_thenReturnSpecificDays() throws CronException {
        String dayOfMonthExpression = "1,15,30";
        String expectedOutput = "1 15 30";
        String actualOutput = new DayOfMonthParser(dayOfMonthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseDayOfMonth_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange() throws CronException {
        String dayOfMonthExpression = "1,15,30,40";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(dayOfMonthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid list range", exception.getMessage());
    }
//
    @Test
    void givenStarSlashExpression_whenParseDayOfMonth_thenReturnSteppedDays() throws CronException {
        String dayOfMonthExpression = "*/5";
        String expectedOutput = "1 6 11 16 21 26 31";
        String actualOutput = new DayOfMonthParser(dayOfMonthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseDayOfMonth_thenShouldThrowInvalidStepException() {
        String dayOfMonthExpression = "*/0";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(dayOfMonthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid step", exception.getMessage());
    }



    @Test
    void givenSingleValueExpression_whenParseMonth_thenReturnSpecificMonth() throws CronException {
        String monthExpression = "6";
        String expectedOutput = "6";
        String actualOutput = new MonthParser(monthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidSingleValueExpression_whenParseDOM_thenReturnSpecificMonth() {
        String monthExpression = "36";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfMonthParser(monthExpression).parse());
        assertEquals("DAY_OF_MONTH : Invalid number", exception.getMessage());

    }

    @Test
    void givenRangeExpression_whenParseMonth_thenReturnRangeOfMonths() throws CronException {
        String monthExpression = "3-5";
        String expectedOutput = "3 4 5";
        String actualOutput = new MonthParser(monthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseMonth_thenShouldThrowCronException() {
        String monthExpression = "0-15";
        Assertions.assertThrows(CronException.class, () -> new MonthParser(monthExpression).parse());
    }

    @Test
    void givenListExpression_whenParseMonth_thenReturnSpecificMonths() throws CronException {
        String monthExpression = "3,6,9";
        String expectedOutput = "3 6 9";
        String actualOutput = new MonthParser(monthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseMonth_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange(){
        String monthExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MonthParser(monthExpression).parse());
        assertEquals("MONTH : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseMonth_thenReturnSteppedMonths() throws CronException {
        String monthExpression = "*/3";
        String expectedOutput = "1 4 7 10";
        String actualOutput = new MonthParser(monthExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseMonth_thenShouldThrowInvalidStepException() {
        String monthExpression = "*/0";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MonthParser(monthExpression).parse());
        assertEquals("MONTH : Invalid step", exception.getMessage());
    }

    @Test
    void givenInvalidStepExpression_whenParseMonth_thenReturnThrowInvalidStepException(){
        String monthExpression = "*/13";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new MonthParser(monthExpression).parse());
        assertEquals("MONTH : Invalid step", exception.getMessage());
    }


    @Test
    void givenSingleValueExpression_whenParseDayOfWeek_thenReturnSpecificDay() throws CronException {
        String dayOfWeekExpression = "3";
        String expectedOutput = "3";
        String actualOutput = new DayOfWeekParser(dayOfWeekExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfWeek_thenThrowsInvalidNumberException(){
        String dayOfWeekExpression = "7";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfWeekParser(dayOfWeekExpression).parse());
        assertEquals("DAY_OF_WEEK : Invalid number", exception.getMessage());
    }

    @Test
    void givenRangeExpression_whenParseDayOfWeek_thenReturnRangeOfDays() throws CronException{
        String dayOfWeekExpression = "2-4";
        String expectedOutput = "2 3 4";
        String actualOutput = new DayOfWeekParser(dayOfWeekExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseDayOfWeek_thenShouldThrowCronException() {
        String dayOfWeekExpression = "0-15";
        Assertions.assertThrows(CronException.class, () -> new DayOfWeekParser(dayOfWeekExpression).parse());
    }

    @Test
    void givenListExpression_whenParseDayOfWeek_thenReturnSpecificDays() throws CronException {
        String dayOfWeekExpression = "1,3,5";
        String expectedOutput = "1 3 5";
        String actualOutput = new DayOfWeekParser(dayOfWeekExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseDayOfWeek_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange(){
        String dayOfWeekExpression = "2,5,8";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfWeekParser(dayOfWeekExpression).parse());
        assertEquals("DAY_OF_WEEK : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseDayOfWeek_thenReturnSteppedDays() throws CronException {
        String dayOfWeekExpression = "*/2";
        String expectedOutput = "0 2 4 6";
        String actualOutput = new DayOfWeekParser(dayOfWeekExpression).parse();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseDayOfWeek_thenShouldThrowInvalidStepException() {
        String dayOfWeekExpression = "*/0";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfWeekParser(dayOfWeekExpression).parse());
        assertEquals("DAY_OF_WEEK : Invalid step", exception.getMessage());
    }

    @Test
    void givenInvalidStepExpression_whenParseDayOfWeek_thenReturnThrowInvalidStepException(){
        String dayOfWeekExpression = "*/8";
        Exception exception = Assertions.assertThrows(CronException.class, () -> new DayOfWeekParser(dayOfWeekExpression).parse());
        assertEquals("DAY_OF_WEEK : Invalid step", exception.getMessage());
    }

    @Test
    void givenCronData_thenPrintCronData() {
//        PrintStream defaultPrintStream = System.out;
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outStream));
//        CronData cronData = new CronData("0 15 30 45",
//                "0",
//                "1 15",
//                "1 2 3 4 5 6 7 8 9 10 11 12",
//                "1 2 3 4 5",
//                "/usr/bin/find").print();
//        System.setOut(defaultPrintStream);
//        String actual = outStream.toString();
//        assertEquals(expected, actual);

        //todo

    }
}