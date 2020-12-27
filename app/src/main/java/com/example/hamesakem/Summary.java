package com.example.hamesakem;

import java.io.Serializable;

public class Summary implements Serializable {
    public String lecturer;
    public String topic;
    public String university;
    public String uri;
    public String userId;
    public String semester;
    public int num_of_rates;
    public float sum_of_rate;
    public Summary(){

    }

    public Summary(String lecturer, String semester, String topic, String university, String uri, String userId) {
        this.lecturer = lecturer;
        this.topic = topic;
        this.university = university;
        this.uri = uri;
        this.userId = userId;
        this.semester = semester;
        this.num_of_rates=0;
        this.sum_of_rate=0;
    }

    public Summary(Summary summary) {
        this.lecturer = summary.lecturer;
        this.topic = summary.topic;
        this.university = summary.university;
        this.uri = summary.uri;
        this.userId = summary.userId;
        this.semester = summary.semester;
    }
    public float getRank(){
        if(num_of_rates==0)return 0;
        return sum_of_rate/num_of_rates;
    }
}
