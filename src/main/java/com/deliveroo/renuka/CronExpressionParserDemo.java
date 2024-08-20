package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;

public class CronExpressionParserDemo {
    public static void main(String[] args) {
        CronTokenizerAndParser cronTokenizerAndParser = new CronTokenizerAndParser();
        String cronExp = "*/15 1 1,15 * 1-5 /usr/bin/find";
        if (args.length > 0) {
            cronExp = args[0];
        }
//        cronTokenizerAndParser.cronExpression = "*/15 1 1,15 * 1-5 /usr/bin/find";
        cronTokenizerAndParser.cronExpression = cronExp;
        try {
            cronTokenizerAndParser.tokenizeExpression();
            cronTokenizerAndParser.parseExpression().print();
        } catch (CronException e) {
            System.err.println(e.getMessage());
        }
    }

}
