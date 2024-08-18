package com.deliveroo.renuka;

import com.deliveroo.renuka.exceptions.CronException;
import com.deliveroo.renuka.parsers.FieldType;

public record Rules(String regex, FieldType fieldType, CronException.ErrorCode errorCode) {}
