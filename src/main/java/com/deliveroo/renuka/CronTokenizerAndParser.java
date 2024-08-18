package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.models.CronData;
import com.deliveroo.renuka.parsers.*;
import com.deliveroo.renuka.utils.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.*;
import static com.deliveroo.renuka.parsers.FieldType.CRON_EXPRESSION;
import static com.deliveroo.renuka.utils.Numbers.getRange;
import static com.deliveroo.renuka.utils.Numbers.isNumber;

public class CronTokenizerAndParser {
    protected String cronExpression = "";
    protected CronData cronData;
    protected CronData expandedCronData;
    protected List<CronException> exceptions = new ArrayList<>();

    public CronData tokenizeExpression() throws CronException {
        if (cronExpression == null) throw new CronException(CRON_EXPRESSION, NULL_INPUT);
        if (cronExpression.isBlank()) throw new CronException(CRON_EXPRESSION, EMPTY_INPUT);
        String[] tokens = cronExpression.trim().split("\\s", 6);
        if (tokens.length < 6) throw new CronException(CRON_EXPRESSION, INSUFFICIENT_PARAMS);
        cronData = new CronData(
                tokens[0],
                tokens[1],
                tokens[2],
                tokens[3],
                tokens[4],
                tokens[5]
        );
        return cronData;
    }

    public CronData parseExpression() throws CronException {
        String minutesDescription = new MinutesParser(cronData.getMinutes()).parse();
        String hourDescription = new HoursParser(cronData.getHours()).parse();
        String dayOfMonthDescription = new DayOfMonthParser(cronData.getDayOfMonth()).parse();
        String monthDescription = new MonthParser(cronData.getMonth()).parse();
        String dayOfWeekDescription = new DayOfWeekParser(cronData.getDayOfWeek()).parse();
        expandedCronData = new CronData(minutesDescription, hourDescription, dayOfMonthDescription, monthDescription, dayOfWeekDescription, cronData.getCommand());
        handleExceptions();
        return expandedCronData;
    }

    protected void handleExceptions() throws CronException {
        if (exceptions.size() == 1)
            throw exceptions.get(0);
        else if (exceptions.size() > 1) {
            String message = exceptions.stream()
                    .map(CronException::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new CronException(message);
        }
    }

    String parseAndHandle(FieldType fieldType, String token) {
        try {
            return parse(fieldType, token);
        } catch (CronException cronException) {
            exceptions.add(cronException);
        }
        return "";
    }

    String parse(FieldType fieldType, String token) throws CronException{
        if (token.equals("*")) {
            return parseStarExp(fieldType);
        } else if (token.contains("-")) {
            return parseRangeExp(fieldType, token);
        } else if (token.contains(",")) {
            return parseListExp(fieldType, token);
        } else if (token.contains("*/")) {
            return parseSlashStarExp(fieldType, token);
        } else {
            return parseNumber(fieldType, token);
        }
    }

    private String parseSlashStarExp(FieldType fieldType, String token) throws CronException {
        StringBuilder builder = new StringBuilder();
        int step = Integer.parseInt(token.split("\\*/")[1]);
        Map.Entry<Integer, Integer> range = getRange(fieldType);
        if (step <= range.getKey() || step > range.getValue()) {
            throw new CronException(fieldType, CronException.ErrorCode.INVALID_STEP);
        }
        for (int i = range.getKey(); i <= range.getValue(); i += step) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private String parseListExp(FieldType fieldType, String token) throws CronException {
        Map.Entry<Integer, Integer> rangeMap = getRange(fieldType);
        String[] numbers = token.split(",");
        for (String number : numbers) {
            int numeric = Integer.parseInt(number);
            if (numeric < rangeMap.getKey() || numeric > rangeMap.getValue()) {
                throw new CronException(fieldType, CronException.ErrorCode.LIST_RANGE_OUT_OF_BOUND);
            }
        }
        return token.replace(",", " ");
    }

    private String parseNumber(FieldType fieldType, String token) throws CronException {
        Map.Entry<Integer, Integer> rangeMap = getRange(fieldType);
        int number = Integer.parseInt(token);
        if (number < rangeMap.getKey() || number > rangeMap.getValue()) {
            throw new CronException(fieldType, CronException.ErrorCode.INVALID_NUMBER);
        }
        return token.trim();
    }

    private String parseRangeExp(FieldType fieldType, String token) throws CronException {
        StringBuilder builder = new StringBuilder();
        String[] interval = token.split("-");
        if (interval.length > 2)
            throw new CronException(fieldType, CronException.ErrorCode.TOO_MANY_ARGS);
        if(!isNumber(interval[0]) || !isNumber(interval[1])){
            throw new CronException(fieldType, CronException.ErrorCode.INVALID_RANGE);
        }
        Map.Entry<Integer, Integer> range = getRange(fieldType);
        int startInterval = Integer.parseInt(interval[0]);
        int endInterval = Integer.parseInt(interval[1]);
        if (startInterval < range.getKey() || endInterval > range.getValue()) {
            throw new CronException(fieldType, CronException.ErrorCode.INVALID_RANGE);
        }
        for (int i = startInterval; i <= endInterval; i++) {
            builder.append(i);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private String parseStarExp(FieldType fieldType) {
        return Numbers.getListString(fieldType);
    }

}
