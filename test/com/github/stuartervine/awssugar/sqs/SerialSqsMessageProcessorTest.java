package com.github.stuartervine.awssugar.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.github.stuartervine.awssugar.ErrorHandler;
import com.github.stuartervine.awssugar.MessageHandler;
import org.junit.Test;

import java.text.ParseException;

import static com.github.stuartervine.awssugar.sqs.SqsPollingTime.sqsPollingTime;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SerialSqsMessageProcessorTest {

    @Test
    public void removesMessageWhenHandledSuccessfully() {
        final AmazonSQS amazonSQS = mock(AmazonSQS.class);
        final SerialSqsMessageProcessor processor = new SerialSqsMessageProcessor(amazonSQS, successfulMessageHandler(), successfulErrorHandler(), sqsPollingTime(0));

        final Message message = new Message();
        message.setBody("Hello");
        final ReceiveMessageResult value = new ReceiveMessageResult().withMessages(message);
        when(amazonSQS.receiveMessage(new ReceiveMessageRequest("aQueue").withMaxNumberOfMessages(1))).thenReturn(value);


        processor.processMessage("aQueue");
    }

    private MessageHandler successfulMessageHandler() {
        return new MessageHandler() {
            @Override
            public void onMessage(String message) throws ParseException, Exception {
            }
        };
    }

    private ErrorHandler successfulErrorHandler() {
        return new ErrorHandler() {
            @Override
            public void handle(Exception e) {
            }
        };
    }

}
