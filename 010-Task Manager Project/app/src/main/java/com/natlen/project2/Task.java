package com.natlen.project2;

public class Task {

    long id;
    int priority;
    String name;
    String date_time;
    String location;
    String description;
    String status;

    public Task() {

    }
    public Task(long id, int priority, String name, String date_time, String location, String description, String status) {
        this.id = id;
        this.priority = priority;
        this.name = name;
        this.date_time = date_time;
        this.location = location;
        this.description = description;
        this.status = status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}