package com.github.stuartervine.awssugar;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqsReceiver {
    private final ErrorHandler errorHandler;
    private final AmazonSQS amazonSQS;
    private final String queueUrl;
    private final MessageHandler messageHandler;
    private ExecutorService executorService;

    public SqsReceiver(AmazonSQS amazonSQS, String queueUrl, MessageHandler messageHandler, ErrorHandler errorHandler) {
        this.amazonSQS = amazonSQS;
        this.queueUrl = queueUrl;
        this.messageHandler = messageHandler;
        this.errorHandler = errorHandler;
        executorService = Executors.newSingleThreadExecutor();
    }

    public static SqsReceiver sqsReceiver(AmazonSQS amazonSQS, String queueUrl, MessageHandler messageHandler, ErrorHandler errorHandler) {
        return new SqsReceiver(amazonSQS, queueUrl, messageHandler, errorHandler);
    }

    public void start() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<Message> msgs = amazonSQS.receiveMessage(new ReceiveMessageRequest(queueUrl).withMaxNumberOfMessages(1)).getMessages();
                        if (msgs.size() > 0) {
                            Message message = msgs.get(0);
                            messageHandler.onMessage(message.getBody());
                            amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
                        } else {
                            System.out.println("nothing found, trying again in 5 seconds");
                            Thread.sleep(5000);
                        }
                    } catch (Exception e) {
                        errorHandler.handle(e);
                        executorService.shutdown();
                    }
                }
            }
        });
    }

    public void stop() {
        executorService.shutdownNow();
    }
}
