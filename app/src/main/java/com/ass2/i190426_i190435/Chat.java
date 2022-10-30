package com.ass2.i190426_i190435;

public class Chat {
    String sender, receiver, message;
    String date;

    public Chat(String sender, String receiver, String message, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date=date;
    }

    public Chat(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
