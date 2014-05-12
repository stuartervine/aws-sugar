package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.github.stuartervine.awssugar.*;

public class SqsQueueInfrastructure implements QueueInfrastructure {

    private final AmazonSQS amazonSQS;

    public SqsQueueInfrastructure(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @Override
    public Sender createSender() {
        return new SqsSender(amazonSQS);
    }

    @Override
    public Receiver createReceiver(MessageHandler messageHandler, ErrorHandler errorHandler) {
        return new SqsReceiver(amazonSQS, messageHandler, errorHandler);
    }

    @Override
    public MessageQueue createQueue(String queueName) {
        return new SqsQueue(amazonSQS, queueName);
    }
}
