package com.comp90018.assignment2.application.utils;

import com.comp90018.assignment2.application.objects.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DaoUser {
    private DatabaseReference databaseReference;
    public DaoUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public Task<Void> add(User user){
        return databaseReference.child("Users").child(user.getEmail()).setValue(user);
//        return databaseReference.push().setValue(user);
    }

    public Task<Void> update(String key, HashMap<String,Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.removeValue();
    }

    public Query get(){
        return databaseReference.orderByKey();
    }
}
