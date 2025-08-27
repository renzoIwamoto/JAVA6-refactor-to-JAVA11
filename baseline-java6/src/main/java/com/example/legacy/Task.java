
package com.example.legacy;

import java.util.Date;

public class Task {
    private final String id;
    private final String title;
    private final String description;
    private final Date createdAt;

    public Task(String id, String title, String description, Date createdAt) {
        this.id = id; this.title = title; this.description = description; this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getCreatedAt() { return createdAt; }
}
