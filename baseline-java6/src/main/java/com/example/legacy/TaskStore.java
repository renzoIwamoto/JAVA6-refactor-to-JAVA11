
package com.example.legacy;

import java.util.*;

public class TaskStore {
    private final List tasks = Collections.synchronizedList(new ArrayList());

    public void save(Task t) {
        tasks.add(t);
    }

    public List findAll() {
        List copy = new ArrayList();
        synchronized (tasks) {
            for (int i = 0; i < tasks.size(); i++) {
                copy.add(tasks.get(i));
            }
        }
        return copy;
    }
}
