package com.github.stuartervine.awssugar;

public interface Receiver {
    void start(String queueName);
    void stop();
    ExplicitReceiver explicit(String queueName);
}
