package com.bolotov.oraclebot.exception;

public class AddSourceException extends Exception{


    public AddSourceException(){
        super();
    }

    public AddSourceException(String message) {
        super(message);
    }

    public AddSourceException(Exception e) {
        super(e.getMessage());
        setStackTrace(e.getStackTrace());
    }
}
