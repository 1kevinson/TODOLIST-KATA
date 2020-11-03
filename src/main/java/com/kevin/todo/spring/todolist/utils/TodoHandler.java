package com.kevin.todo.spring.todolist.utils;

import com.kevin.todo.spring.todolist.model.TodoItemDao;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.view.TodoItemView;

public interface TodoHandler {

    static TodoItemView renderViewFromTodoDao(TodoItemDao todo, String url) {
        return new TodoItemView(todo.getId(), todo.getTitle(), todo.isCompleted(), todo.getOrders(), url);
    }

    static TodoItemDao getTodoDaoFromTodoModel(TodoItemModel todo) {
        return new TodoItemDao(todo.getTitle(), todo.getOrder());
    }

}
