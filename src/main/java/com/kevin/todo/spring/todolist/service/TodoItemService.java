package com.kevin.todo.spring.todolist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.kevin.todo.spring.todolist.exceptions.TodoNotFoundException;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.view.TodoItemView;

import java.util.List;

public interface TodoItemService {

    TodoItemView addTodo(TodoItemModel todo);

    TodoItemView retrieveOneTodo(int todoId) throws TodoNotFoundException;

    List<TodoItemView> retrieveAllTodos();

    void removeTodo(int todoId);

    void removeAllTodos();

    TodoItemView updateAllFieldsOfTodo(int id, String title, boolean completed, int orders);

    TodoItemView patchOneFieldOfTodo(int id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
