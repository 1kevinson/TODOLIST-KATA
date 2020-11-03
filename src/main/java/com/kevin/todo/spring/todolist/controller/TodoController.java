package com.kevin.todo.spring.todolist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.service.TodoListService;
import com.kevin.todo.spring.todolist.view.TodoItemView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {

    public static final String TODOS_BASE_PATH = "/todos/";
    private final TodoListService todoListService;

    public TodoController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping
    @CrossOrigin(methods = POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TodoItemView createTodo(@Valid @RequestBody TodoItemModel todo, UriComponentsBuilder builder) {
        final var savedTodoItemView = todoListService.addTodo(todo);
        return getTodoItemView(builder, savedTodoItemView, savedTodoItemView.getId());
    }

    @GetMapping
    @CrossOrigin(methods = GET)
    public List<TodoItemView> getAllTodos(UriComponentsBuilder builder) {
        return todoListService.retrieveAllTodos()
                .stream()
                .map(todoItemView -> new TodoItemView(
                        todoItemView.getId(),
                        todoItemView.getTitle(),
                        todoItemView.isCompleted(),
                        todoItemView.getOrder(),
                        builder.toUriString().concat(TODOS_BASE_PATH + todoItemView.getId())))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @CrossOrigin(methods = GET)
    public TodoItemView getOneTodo(@PathVariable("id") int id, UriComponentsBuilder builder) {
        final var foundedTodoView = todoListService.retrieveOneTodo(id);
        return getTodoItemView(builder, foundedTodoView, id);
    }

    @PutMapping("/{id}")
    @CrossOrigin(methods = PUT)
    public TodoItemView updateOneTodo(@Valid @RequestBody TodoItemModel todoItemModel,
                                      @PathVariable("id") int todoId,
                                      UriComponentsBuilder builder) {
        final var todoUpdatedView = todoListService.updateAllFieldsOfTodo(todoId, todoItemModel.getTitle(), todoItemModel.isCompleted(), todoItemModel.getOrder());
        return getTodoItemView(builder, todoUpdatedView, todoUpdatedView.getId());
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    @CrossOrigin(methods = PATCH)
    public TodoItemView partialUpdateTodo(@PathVariable("id") int todoId,
                                          @RequestBody JsonPatch jsonPatch,
                                          UriComponentsBuilder builder) throws JsonPatchException, JsonProcessingException {
        final var todoPatchedView = todoListService.patchOneFieldOfTodo(todoId, jsonPatch);
        return getTodoItemView(builder, todoPatchedView, todoPatchedView.getId());
    }

    private TodoItemView getTodoItemView(UriComponentsBuilder builder, TodoItemView view, int id) {
        return new TodoItemView(
                view.getId(),
                view.getTitle(),
                view.isCompleted(),
                view.getOrder(),
                builder.path(TODOS_BASE_PATH + id).toUriString());
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(methods = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOneTodo(@PathVariable("id") int id) {
        todoListService.removeTodo(id);
    }

    @DeleteMapping
    @CrossOrigin(methods = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTodos() {
        todoListService.removeAllTodos();
    }

}
