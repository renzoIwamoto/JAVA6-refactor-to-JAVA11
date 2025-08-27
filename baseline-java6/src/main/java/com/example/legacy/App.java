
package com.example.legacy;

import java.util.*;

public class App {
    public static void main(String[] args) {
        TaskStore store = new TaskStore();
        TaskService service = new TaskService(store);

        service.createTask("Migrar a Java 21", "Refactor de APIs antiguas");
        service.createTask("Generar pruebas", "Agregar JUnit y Mockito");
        service.createTask("Medir métricas", "Definir cobertura y calidad");

        List tasks = service.listTasks(); // raw type deliberado
        for (int i = 0; i < tasks.size(); i++) {
            Task t = (Task) tasks.get(i);
            System.out.println(t.getId() + " - " + t.getTitle() + " [" + t.getCreatedAt() + "]");
        }

        System.out.println("Tareas creadas: " + tasks.size());
    }
}
