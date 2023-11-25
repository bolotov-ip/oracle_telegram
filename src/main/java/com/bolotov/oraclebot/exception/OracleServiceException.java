package com.bolotov.oraclebot.exception;

public class OracleServiceException extends Exception{

    public OracleServiceException(String message) {
        super(message);
    }

    public OracleServiceException(Exception e) {
        super(e.getMessage());
        setStackTrace(e.getStackTrace());
    }
}
