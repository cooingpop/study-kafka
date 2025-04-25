package com.example.study_kafka;

public class MyMessage {
    private String name;

    public MyMessage() {
    }

    public MyMessage(String name) {
        this.name = name;
    }

    // getter, setter 필수!
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}