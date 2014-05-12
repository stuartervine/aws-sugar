package com.github.stuartervine.awssugar;

public interface Sender {
    String send(String message, String queueName);

    ExplicitSender explicit(String queueName);
}
