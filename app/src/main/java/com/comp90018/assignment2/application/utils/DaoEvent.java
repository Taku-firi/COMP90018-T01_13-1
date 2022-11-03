package com.comp90018.assignment2.application.utils;

import com.comp90018.assignment2.application.objects.Event;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

// Database object for manipulating events
public class DaoEvent {
    private DatabaseReference databaseReference;

    public DaoEvent(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Event.class.getSimpleName());
    }

    // Add
    public Task<Void> add(Event event){
        return databaseReference.push().setValue(event);
    }

    // Update
    public Task<Void> update(String key, HashMap<String,Object> hashMap){
        return databaseReference.child(key).updateChildren(hashMap);
    }

    //Remove
    public Task<Void> remove(String key)
    {
        return databaseReference.removeValue();
    }

    // Get all
    public Query get(){
        return databaseReference.orderByKey();
    }

    // Get by key
    public Query get(String key){
        if (key==null){
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);
    }
}
