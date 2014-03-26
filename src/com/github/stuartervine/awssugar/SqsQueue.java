package com.github.stuartervine.awssugar;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class SqsQueue {
    private final AmazonSQS amazonSQS;
    private final String queueName;

    public SqsQueue(AmazonSQS amazonSQS, String queueName) {
        this.amazonSQS = amazonSQS;
        this.queueName = queueName;
    }

    public SqsSender createSender() {
        return SqsSender.sqsSender(amazonSQS);
    }

    public SqsReceiver createReceiver(MessageHandler messageHandler, ErrorHandler errorHandler) {
        return SqsReceiver.sqsReceiver(amazonSQS, queueUrl(), messageHandler, errorHandler);
    }

    private String queueUrl() {
        return amazonSQS.createQueue(new CreateQueueRequest(queueName)).getQueueUrl();
    }

    public Sequence<Message> listMessages() {
        final ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(new ReceiveMessageRequest(queueUrl()));
        return Sequences.sequence(receiveMessageResult.getMessages());
    }


    public String queueName() {
        return queueName;
    }
}
