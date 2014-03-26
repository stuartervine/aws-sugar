package com.github.stuartervine.awssugar;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

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
        String queueUrl = amazonSQS.createQueue(new CreateQueueRequest(queueName)).getQueueUrl();
        return amazonSQS.sendMessage(new SendMessageRequest(queueUrl, message)).getMessageId();
    }

}
