package com.example.hamesakem;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.sql.Struct;

public class User implements Serializable {
//    private String UserID;
    public String fullName;
    public String email;
    public String phone;
    public int num_of_rates;
    public int num_of_sum;
    public double rank;
    public String userId;


    public User(String fullName, String email, String phone, int num_of_rates, int num_of_sum, double rank, String userId) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.num_of_rates = num_of_rates;
        this.num_of_sum = num_of_sum;
        this.rank = rank;
        this.userId = userId;
    }

//    private String type;

    public User() {
    }

    public static User getUser(String id){
        final String[] name_from_id = new String[7];
        final User[] user = new User[1];
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users").document(id);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    name_from_id[0] = (String) document.getData().get("fullName");
                    name_from_id[1] = (String) document.getData().get("email");
                    name_from_id[2] = (String) document.getData().get("phone");
                    name_from_id[3] = new String (""+document.getData().get("num_of_rates"));
                    name_from_id[4] = new String (""+document.getData().get("num_of_sum"));
                    name_from_id[5] = new String (""+document.getData().get("rank"));
                    name_from_id[6] = (String) document.getData().get("userId");
                    user[0] = document.toObject(User.class);
//                    return user;
//                    holder.id_name.setText("" + name_from_id[0]);
                    Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d("TAG", "No such document");
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }
        });
//        user[0] = new User(name_from_id[0], name_from_id[1], name_from_id[2], Integer.parseInt(name_from_id[3]), Integer.parseInt(name_from_id[4]), Double.parseDouble(name_from_id[5]), name_from_id[6]);
        return user[0];
    }

    public void updateFirestore(){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        User x = new User(fullName, email, phone, num_of_rates, num_of_sum, rank, userId);
        documentReference.set(x);
    }

    public int computeRank(){
        if(num_of_rates==0) return 3;
        if(rank/num_of_rates<4.0||(num_of_rates<50 || num_of_sum<5)) return 3;
        if(num_of_rates>=150 && num_of_sum>=15) return 1;
        return 2;
    }
//    public User(String userID, String fullName, String email, String phone, String type) {
//        UserID = userID;
//        this.fullName = fullName;
//        Email = email;
//        phone = phone;
//        this.type = type;
//    }

//    public String getUserID() {
//        return UserID;
//    }
//
//    public void setUserID(String userID) {
//        UserID = userID;
//    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNum_of_rates() {
        return num_of_rates;
    }

    public void setNum_of_rates(int num_of_rates) {
        this.num_of_rates = num_of_rates;
    }

    public int getNum_of_sum() {
        return num_of_sum;
    }

    public void setNum_of_sum(int num_of_sum) {
        this.num_of_sum = num_of_sum;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
