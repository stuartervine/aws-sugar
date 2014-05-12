package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.github.stuartervine.awssugar.ErrorHandler;
import com.github.stuartervine.awssugar.MessageHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.stuartervine.awssugar.sqs.SqsPollingTime.sqsPollingTime;
import static java.lang.Thread.sleep;

public class SerialSqsMessageProcessor {
    private final AmazonSQS amazonSQS;
    private final MessageHandler messageHandler;
    private final ErrorHandler errorHandler;
    private final SqsPollingTime sqsPollingTime;
    private final ExecutorService executorService;

    public SerialSqsMessageProcessor(AmazonSQS amazonSQS, MessageHandler messageHandler, ErrorHandler errorHandler, SqsPollingTime sqsPollingTime) {
        this.amazonSQS = amazonSQS;
        this.messageHandler = messageHandler;
        this.errorHandler = errorHandler;
        this.sqsPollingTime = sqsPollingTime;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static SerialSqsMessageProcessor serialSqsMessageProcessor(AmazonSQS amazonSQS, MessageHandler messageHandler, ErrorHandler errorHandler) {
        return new SerialSqsMessageProcessor(amazonSQS, messageHandler, errorHandler, sqsPollingTime(5000));
    }

    public void startProcessing(final String queueName) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<Message> msgs = amazonSQS.receiveMessage(new ReceiveMessageRequest(queueName).withMaxNumberOfMessages(1)).getMessages();
                        if (msgs.size() > 0) {
                            Message message = msgs.get(0);
                            messageHandler.onMessage(message.getBody());
                            amazonSQS.deleteMessage(new DeleteMessageRequest(queueName, message.getReceiptHandle()));
                        } else {
                            //log?
                            sleep(sqsPollingTime.millis);
                        }
                    } catch (Exception e) {
                        errorHandler.handle(e);
                        executorService.shutdown();
                    }
                }
            }
        });

    }

    public void shutdown() {
        executorService.shutdown();
    }
}
