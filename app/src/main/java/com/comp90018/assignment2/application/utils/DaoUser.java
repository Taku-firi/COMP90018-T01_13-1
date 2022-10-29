package com.comp90018.assignment2.application.utils;

import com.comp90018.assignment2.application.objects.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DaoUser {
    private DatabaseReference databaseReference;
    public DaoUser(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public Task<Void> add(User user){
        return databaseReference.push().setValue(user);
    }
}
