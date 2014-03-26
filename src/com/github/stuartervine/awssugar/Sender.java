package com.github.stuartervine.awssugar;

public interface Sender {
    String send(String message, String queueUrl);
}
