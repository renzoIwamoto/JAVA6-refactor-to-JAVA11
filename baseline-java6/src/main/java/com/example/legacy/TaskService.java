
package com.example.legacy;

import java.util.*;
import java.util.Calendar;

public class TaskService {
    private final TaskStore store;

    public TaskService(TaskStore store) {
        this.store = store;
    }

    public Task createTask(String title, String description) {
        String id = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        Task t = new Task(id, title, description, now);
        store.save(t);
        return t;
    }

    public List listTasks() {
        return store.findAll();
    }
}
