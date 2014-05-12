package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.github.stuartervine.awssugar.ExplicitSender;
import com.github.stuartervine.awssugar.Sender;

public class SqsSender implements Sender {
    private final AmazonSQS amazonSQS;

    public SqsSender(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    public static SqsSender sqsSender(AmazonSQS amazonSQS) {
        return new SqsSender(amazonSQS);
    }

    @Override
    public String send(String message, String queueName) {
        return explicit(queueName).send(message);
    }

    @Override
    public ExplicitSender explicit(String queueName) {
        return new ExplicitSqsSender(amazonSQS, queueName);
    }

}
