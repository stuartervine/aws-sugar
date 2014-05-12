package com.github.stuartervine.awssugar;

public interface QueueInfrastructure {

    Sender createSender();

    Receiver createReceiver(MessageHandler messageHandler, ErrorHandler errorHandler);

    MessageQueue createQueue(String queueName);

}
