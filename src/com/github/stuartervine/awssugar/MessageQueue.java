package com.github.stuartervine.awssugar;

public interface MessageQueue {

    ExplicitSender createSender();

    ExplicitReceiver createReceiver(MessageHandler messageHandler, ErrorHandler errorHandler);

}
