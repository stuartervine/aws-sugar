package com.github.stuartervine.awssugar;

import java.text.ParseException;

public interface MessageHandler {
    void onMessage(String message) throws ParseException, Exception;
}
