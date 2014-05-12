package com.github.stuartervine.awssugar.sqs;

public class SqsPollingTime {
    public final int millis;

    public SqsPollingTime(int millis) {
        this.millis = millis;
    }

    public static SqsPollingTime sqsPollingTime(int timeInMillis) {
        return new SqsPollingTime(timeInMillis);
    }

}
