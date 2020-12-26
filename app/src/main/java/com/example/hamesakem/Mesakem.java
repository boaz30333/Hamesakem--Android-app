package com.example.hamesakem;

public class Mesakem extends User {

    public int Rank;
    public Mesakem(String userID, String name, String email, String phoneNumber,String type, int rank) {
//        super(userID, name, email, phoneNumber,type);
        Rank = rank;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }
}
