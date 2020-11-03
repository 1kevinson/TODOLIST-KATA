package com.kevin.todo.spring.todolist.view;

public class TodoItemView {

    private final int id;
    private final String title;
    private final boolean completed;
    private final int order;
    private final String url;

    public TodoItemView(int id, String title, boolean completed, int order, String url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public int getOrder() {
        return order;
    }
}
