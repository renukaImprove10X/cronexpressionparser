package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.*;
import com.deliveroo.renuka.models.CronData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CronTokenizerAndParserTest {

    CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();

    @BeforeEach
    void setUp() throws CronException {
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
        assertEquals("Cron expression has missing fields", exception.getMessage());
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
        tokenizeAndParse.cronExpression = "*/15 0 1,15 * 1-5 /usr/bin/find";
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
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "*";
        String expectedOutput = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 " +
                "32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59";
        String actualOutput = tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression);
        assertEquals(expectedOutput, actualOutput);

    }

    @Test
    void givenStarExpression_whenParseHours_thenReturnAllHours() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "*";
        String expectedOutput = "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23";
        String actualOutput = tokenizeAndParse.parse(FieldType.HOUR, hoursExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseDayOfMonth_thenReturnAllDaysOfMonth() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "*";
        String expectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseMonth_thenReturnAllMonths() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "*";
        String expectedOutput = "1 2 3 4 5 6 7 8 9 10 11 12";
        String actualOutput = tokenizeAndParse.parse(FieldType.MONTH, monthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenStarExpression_whenParseDayOfWeek_thenReturnAllDaysOfWeek() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "*";
        String expectedOutput = "1 2 3 4 5 6 7";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseMinutes_thenReturnSpecificMinute() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "5";
        String expectedOutput = "5";
        String actualOutput = tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenRangeExpression_whenParseMinutes_thenReturnRangeOfMinutes() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "10-20";
        String expectedOutput = "10 11 12 13 14 15 16 17 18 19 20";
        String actualOutput = tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseMinutes_thenShouldThrowInvalidRangeException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "50-70";
        Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression));
    }

    @Test
    void givenTooManyRangesExpression_whenParseMinutes_thenShouldThrowInvalidRangeExceptionWithMessageTooManyRanges(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "50-70-80";
        Exception exception = Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression));
        assertEquals("MINUTE : Multiple Ranges Detected", exception.getMessage());
    }

    @Test
    void givenListExpression_whenParseMinutes_thenReturnSpecificMinutes() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "5,10,15";
        String expectedOutput = "5 10 15";
        String actualOutput = tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseMinutes_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(InvalidListException.class, () -> tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression));
        assertEquals("MINUTE : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStarSlashExpression_whenParseMinutes_thenReturnTimedMinutes() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "*/15";
        String expectedOutput = "0 15 30 45";
        String actualOutput = tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseMinutes_thenShouldThrowInvalidTimerException(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "*/0";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.MINUTE, minutesExpression));
        assertEquals("MINUTE : Invalid step", exception.getMessage());
    }

    @Test
    void givenSingleValueExpression_whenParseHours_thenReturnSpecificHour() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "3";
        String expectedOutput = "3";
        String actualOutput = tokenizeAndParse.parse(FieldType.HOUR, hoursExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenRangeExpression_whenParseHours_thenReturnRangeOfHours() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "2-6";
        String expectedOutput = "2 3 4 5 6";
        String actualOutput = tokenizeAndParse.parse(FieldType.HOUR, hoursExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseHours_thenShouldThrowInvalidRangeException(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "10-24";
        Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.HOUR, hoursExpression));
    }

    @Test
    void givenTooManyRangesExpression_whenParseHours_thenShouldThrowInvalidRangeExceptionWithMessageTooManyRanges(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "50-70-80";
        Exception exception = Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.HOUR, hoursExpression));
        assertEquals("HOUR : Multiple Ranges Detected", exception.getMessage());
    }


    @Test
    void givenListExpression_whenParseHours_thenReturnSpecificHours() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "1,2,3";
        String expectedOutput = "1 2 3";
        String actualOutput = tokenizeAndParse.parse(FieldType.HOUR, hoursExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseHours_thenShouldThrowInvalidListExceptionWithMessageRangeOutOfBound(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String minutesExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(InvalidListException.class, () -> tokenizeAndParse.parse(FieldType.HOUR, minutesExpression));
        assertEquals("HOUR : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseHours_thenReturnSteppedHours() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "*/4";
        String expectedOutput = "0 4 8 12 16 20";
        String actualOutput = tokenizeAndParse.parse(FieldType.HOUR, hoursExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseHours_thenShouldThrowInvalidException(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String hoursExpression = "*/0";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.HOUR, hoursExpression));
        assertEquals("HOUR : Invalid step", exception.getMessage());
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfMonth_thenReturnSpecificDay() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "15";
        String expectedOutput = "15";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfMonth_thenReturnSpecificMinute(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "32";
        Exception exception = Assertions.assertThrows(InvalidNumberException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression));
        assertEquals("DAY_OF_MONTH : Invalid number", exception.getMessage());
    }

    @Test
    void givenRangeExpression_whenParseDayOfMonth_thenReturnRangeOfDays() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "10-20";
        String expectedOutput = "10 11 12 13 14 15 16 17 18 19 20";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenMultipleRangeExpression_whenParseDayOfMonth_thenShouldThrowMultipleRangeException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "0-24-30";
        Exception exception = Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression));
        assertEquals("DAY_OF_MONTH : Multiple Ranges Detected", exception.getMessage());
    }

    @Test
    void givenInvalidRangeExpression_whenParseDayOfMonth_thenShouldThrowInvalidRangeException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "0-35";
        Exception exception = Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression));
        assertEquals("DAY_OF_MONTH : Invalid range", exception.getMessage());
    }

    @Test
    void givenListExpression_whenParseDayOfMonth_thenReturnSpecificDays() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "1,15,30";
        String expectedOutput = "1 15 30";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseDayOfMonth_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange() throws InvalidRangeException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "1,15,30,40";
        Exception exception = Assertions.assertThrows(InvalidListException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression));
        assertEquals("DAY_OF_MONTH : Invalid list range", exception.getMessage());
    }
//
    @Test
    void givenStarSlashExpression_whenParseDayOfMonth_thenReturnSteppedDays() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "*/5";
        String expectedOutput = "1 6 11 16 21 26 31";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseDayOfMonth_thenShouldThrowInvalidStepException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfMonthExpression = "*/0";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_MONTH, dayOfMonthExpression));
        assertEquals("DAY_OF_MONTH : Invalid step", exception.getMessage());
    }



    @Test
    void givenSingleValueExpression_whenParseMonth_thenReturnSpecificMonth() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "6";
        String expectedOutput = "6";
        String actualOutput = tokenizeAndParse.parse(FieldType.MONTH, monthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenRangeExpression_whenParseMonth_thenReturnRangeOfMonths() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "3-5";
        String expectedOutput = "3 4 5";
        String actualOutput = tokenizeAndParse.parse(FieldType.MONTH, monthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseMonth_thenShouldThrowInvalidRangeException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "0-15";
        Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.MONTH, monthExpression));
    }

    @Test
    void givenListExpression_whenParseMonth_thenReturnSpecificMonths() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "3,6,9";
        String expectedOutput = "3 6 9";
        String actualOutput = tokenizeAndParse.parse(FieldType.MONTH, monthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseMonth_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "20,50,80";
        Exception exception = Assertions.assertThrows(InvalidListException.class, () -> tokenizeAndParse.parse(FieldType.MONTH, monthExpression));
        assertEquals("MONTH : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseMonth_thenReturnSteppedMonths() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "*/3";
        String expectedOutput = "1 4 7 10";
        String actualOutput = tokenizeAndParse.parse(FieldType.MONTH, monthExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseMonth_thenShouldThrowInvalidStepException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "*/0";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.MONTH, monthExpression));
        assertEquals("MONTH : Invalid step", exception.getMessage());
    }

    @Test
    void givenInvalidStepExpression_whenParseMonth_thenReturnThrowInvalidStepException(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String monthExpression = "*/13";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.MONTH, monthExpression));
        assertEquals("MONTH : Invalid step", exception.getMessage());
    }


    @Test
    void givenSingleValueExpression_whenParseDayOfWeek_thenReturnSpecificDay() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "3";
        String expectedOutput = "3";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenSingleValueExpression_whenParseDayOfWeek_thenThrowsInvalidNumberException(){
        String dayOfWeekExpression = "0";
        Exception exception = Assertions.assertThrows(InvalidNumberException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression));
        assertEquals("DAY_OF_WEEK : Invalid number", exception.getMessage());
    }

    @Test
    void givenRangeExpression_whenParseDayOfWeek_thenReturnRangeOfDays() throws CronException{
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "2-4";
        String expectedOutput = "2 3 4";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidRangeExpression_whenParseDayOfWeek_thenShouldThrowInvalidRangeException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "0-15";
        Assertions.assertThrows(InvalidRangeException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression));
    }

    @Test
    void givenListExpression_whenParseDayOfWeek_thenReturnSpecificDays() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "1,3,5";
        String expectedOutput = "1 3 5";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidListExpression_whenParseDayOfWeek_thenShouldThrowInvalidListExceptionWithMessageInvalidListRange(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "2,5,8";
        Exception exception = Assertions.assertThrows(InvalidListException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression));
        assertEquals("DAY_OF_WEEK : Invalid list range", exception.getMessage());
    }

    @Test
    void givenStepExpression_whenParseDayOfWeek_thenReturnSteppedDays() throws CronException {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "*/2";
        String expectedOutput = "1 3 5 7";
        String actualOutput = tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void givenInvalidStarSlashExpression_whenParseDayOfWeek_thenShouldThrowInvalidStepException() {
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "*/0";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression));
        assertEquals("DAY_OF_WEEK : Invalid step", exception.getMessage());
    }

    @Test
    void givenInvalidStepExpression_whenParseDayOfWeek_thenReturnThrowInvalidStepException(){
        CronTokenizerAndParser tokenizeAndParse = new CronTokenizerAndParser();
        String dayOfWeekExpression = "*/8";
        Exception exception = Assertions.assertThrows(InvalidStepException.class, () -> tokenizeAndParse.parse(FieldType.DAY_OF_WEEK, dayOfWeekExpression));
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