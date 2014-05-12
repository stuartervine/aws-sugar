package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.github.stuartervine.awssugar.ErrorHandler;
import com.github.stuartervine.awssugar.ExplicitSender;
import com.github.stuartervine.awssugar.MessageHandler;
import com.github.stuartervine.awssugar.MessageQueue;
import com.github.stuartervine.awssugar.sqs.ExplicitSqsReceiver;
import com.github.stuartervine.awssugar.sqs.ExplicitSqsSender;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

public class SqsQueue implements MessageQueue {
    private final AmazonSQS amazonSQS;
    private final String queueName;

    public SqsQueue(AmazonSQS amazonSQS, String queueName) {
        this.amazonSQS = amazonSQS;
        this.queueName = queueName;
    }

    public ExplicitSender createSender() {
        return new ExplicitSqsSender(amazonSQS, queueName);
    }

    public ExplicitSqsReceiver createReceiver(MessageHandler messageHandler, ErrorHandler errorHandler) {
        return ExplicitSqsReceiver.sqsReceiver(amazonSQS, queueUrl(), messageHandler, errorHandler);
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
