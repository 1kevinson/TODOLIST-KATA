package com.kevin.todo.spring.todolist.repository;

import com.kevin.todo.spring.todolist.model.TodoItemDao;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<TodoItemDao, Integer> {

}
