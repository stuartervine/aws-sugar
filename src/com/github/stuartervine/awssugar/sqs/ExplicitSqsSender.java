package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.github.stuartervine.awssugar.ExplicitSender;

public class ExplicitSqsSender implements ExplicitSender {
    private final AmazonSQS amazonSQS;
    private final String queueName;

    public ExplicitSqsSender(AmazonSQS amazonSQS, String queueName) {
        this.amazonSQS = amazonSQS;
        this.queueName = queueName;
    }

    @Override
    public String send(String message) {
        String queueUrl = amazonSQS.createQueue(new CreateQueueRequest(queueName)).getQueueUrl();
        return amazonSQS.sendMessage(new SendMessageRequest(queueUrl, message)).getMessageId();
    }
}
