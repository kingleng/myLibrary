package com.example.leng.myapplication2.ui.server.socketserver.exception;

public class RequestException extends Exception{
    private String message;

    public RequestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
