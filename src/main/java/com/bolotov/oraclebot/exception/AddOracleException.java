package com.bolotov.oraclebot.exception;

public class AddOracleException extends Exception{

    public AddOracleException(){
        super();
    }

    public AddOracleException(String message) {
        super(message);
    }

    public AddOracleException(Exception e) {
        super(e.getMessage());
        setStackTrace(e.getStackTrace());
    }
}
