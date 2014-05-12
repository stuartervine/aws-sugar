package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.github.stuartervine.awssugar.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.stuartervine.awssugar.sqs.SerialSqsMessageProcessor.serialSqsMessageProcessor;

public class SqsReceiver implements Receiver {
    private final AmazonSQS amazonSQS;
    private final MessageHandler messageHandler;
    private final ErrorHandler errorHandler;
    private final SerialSqsMessageProcessor messageProcessor;

    public SqsReceiver(AmazonSQS amazonSQS, MessageHandler messageHandler, ErrorHandler errorHandler) {
        this.amazonSQS = amazonSQS;
        this.messageHandler = messageHandler;
        this.errorHandler = errorHandler;
        this.messageProcessor = serialSqsMessageProcessor(amazonSQS, messageHandler, errorHandler);
    }

    @Override
    public void start(final String queueName) {
        messageProcessor.startProcessing(queueName);
    }

    @Override
    public void stop() {
        messageProcessor.shutdown();
    }

    @Override
    public ExplicitReceiver explicit(String queueName) {
        return new ExplicitSqsReceiver(amazonSQS, queueName, messageHandler, errorHandler);
    }
}
