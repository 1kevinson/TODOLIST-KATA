package com.kevin.todo.spring.todolist.model;

import javax.validation.constraints.NotNull;

public class TodoItemModel {

    @NotNull(message = "Title cannot be null")
    private String title;

    private boolean completed;

    private int order;

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getOrder() {
        return order;
    }
}
