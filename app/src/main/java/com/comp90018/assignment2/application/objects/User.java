package com.comp90018.assignment2.application.objects;

import java.util.ArrayList;

// The "User" object
// All the related data of a user can be get or set
public class User {

    private String password;
    private String name;
    private String email;
    private String gender = "Unknown";
    private String age = "18";
    private String location= "location";
    private String interest = "interest";
    private String detail = "self intro";
    // Store all the events the user joined
    private ArrayList<Event> events = new ArrayList<>();

    public User(){}

    public User(String name,String email){
        this.name = name;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}

