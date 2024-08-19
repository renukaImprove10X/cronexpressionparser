package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.models.CronData;
import com.deliveroo.renuka.parsers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.deliveroo.renuka.exceptions.CronException.ErrorCode.*;
import static com.deliveroo.renuka.parsers.FieldType.CRON_EXPRESSION;

public class CronTokenizerAndParser {
    public List<CronException> exceptions = new ArrayList<>();

    protected String cronExpression = "";
    protected CronData cronData;
    protected CronData expandedCronData;

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
        String minutesDescription = new MinutesParser(cronData.getMinutes()).parseAndHandle(e -> exceptions.add(e));
        String hourDescription = new HoursParser(cronData.getHours()).parseAndHandle(e -> exceptions.add(e));
        String dayOfMonthDescription = new DayOfMonthParser(cronData.getDayOfMonth()).parseAndHandle(e -> exceptions.add(e));
        String monthDescription = new MonthParser(cronData.getMonth()).parseAndHandle(e -> exceptions.add(e));
        String dayOfWeekDescription = new DayOfWeekParser(cronData.getDayOfWeek()).parseAndHandle(e -> exceptions.add(e));
        expandedCronData = new CronData(minutesDescription, hourDescription, dayOfMonthDescription,
                monthDescription, dayOfWeekDescription, cronData.getCommand());
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
}