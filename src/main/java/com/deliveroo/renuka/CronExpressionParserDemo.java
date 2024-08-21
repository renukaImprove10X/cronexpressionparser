package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;

public class CronExpressionParserDemo {
    public static void main(String[] args) {
        CronTokenizerAndParser cronTokenizerAndParser = new CronTokenizerAndParser();
        String cronExp = "*/15 1 1,15 * 1-5 /usr/bin/find";
        if (args.length > 0) {
            cronExp = args[0];
        }
        cronTokenizerAndParser.cronExpression = cronExp;
        try {
            cronTokenizerAndParser.tokenizeExpression();
            cronTokenizerAndParser.parseExpression().print();
        } catch (CronException e) {
            System.err.println(e.getMessage());
        }
    }
}








//        cronTokenizerAndParser.cronExpression = "*/15 1 1,15 * 1-5 /usr/bin/find";

//            cronTokenizerAndParser.expandedCronData.getExpandedCronList();
//            System.out.println(cronTokenizerAndParser.expandedCronData.getMinutesList());
//            System.out.println(cronTokenizerAndParser.expandedCronData.getHoursList());
//            System.out.println(cronTokenizerAndParser.expandedCronData.getDayOfMonthList());
//            System.out.println(cronTokenizerAndParser.expandedCronData.getMonthList());
//            System.out.println(cronTokenizerAndParser.expandedCronData.getDayOfWeekList());