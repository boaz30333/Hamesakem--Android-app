package com.example.hamesakem.Result;

public class Summary {
    public String lecturer;
    public String topic;
    public String university;
    public String uri;
    public String userId;
    public String semester;

    public Summary(){

    }

    public Summary(String lecturer, String semester, String topic, String university, String uri, String userId) {
        this.lecturer = lecturer;
        this.topic = topic;
        this.university = university;
        this.uri = uri;
        this.userId = userId;
        this.semester = semester;
    }
}
