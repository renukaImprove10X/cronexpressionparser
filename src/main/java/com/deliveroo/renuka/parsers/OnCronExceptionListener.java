package com.deliveroo.renuka.parsers;

import com.deliveroo.renuka.exceptions.CronException;

public interface OnCronExceptionListener {

    void onException(CronException e);
}
