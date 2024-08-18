package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.*;
import com.deliveroo.renuka.models.CronData;

import java.util.*;

public class CronExpressionParserDemo {
    public static void main(String[] args) {
        CronTokenizerAndParser cronTokenizerAndParser = new CronTokenizerAndParser();
        cronTokenizerAndParser.cronExpression = "*/15 1 1,15 * 1-5 /usr/bin/find";
        try {
            cronTokenizerAndParser.tokenizeExpression();
            cronTokenizerAndParser.parseExpression().print();
        } catch (CronException e) {
            System.err.println(e.getMessage());
        }
    }

}
