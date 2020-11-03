package com.kevin.todo.spring.todolist.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TODOS")
public class TodoItemDao {

    private final LocalDateTime creationDate = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private boolean completed;
    private int orders;


    public TodoItemDao(String title, int order) {
        this.title = title;
        this.orders = order;
    }

    public TodoItemDao(int id, String title, boolean completed, int orders) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.orders = orders;
    }

    public TodoItemDao() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getOrders() {
        return orders;
    }
}
