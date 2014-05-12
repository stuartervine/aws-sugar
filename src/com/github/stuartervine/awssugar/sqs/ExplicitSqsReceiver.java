package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.github.stuartervine.awssugar.ErrorHandler;
import com.github.stuartervine.awssugar.ExplicitReceiver;
import com.github.stuartervine.awssugar.MessageHandler;

import static com.github.stuartervine.awssugar.sqs.SerialSqsMessageProcessor.serialSqsMessageProcessor;

public class ExplicitSqsReceiver implements ExplicitReceiver {
    private final String queueUrl;
    private final SerialSqsMessageProcessor messageProcessor;

    public ExplicitSqsReceiver(AmazonSQS amazonSQS, String queueUrl, MessageHandler messageHandler, ErrorHandler errorHandler) {
        this.queueUrl = queueUrl;
        messageProcessor = serialSqsMessageProcessor(amazonSQS, messageHandler, errorHandler);
    }

    public static ExplicitSqsReceiver sqsReceiver(AmazonSQS amazonSQS, String queueUrl, MessageHandler messageHandler, ErrorHandler errorHandler) {
        return new ExplicitSqsReceiver(amazonSQS, queueUrl, messageHandler, errorHandler);
    }

    public void start() {
        messageProcessor.startProcessing(queueUrl);
    }

    public void stop() {
        messageProcessor.shutdown();
    }
}
