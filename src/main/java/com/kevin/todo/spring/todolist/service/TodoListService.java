package com.kevin.todo.spring.todolist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.kevin.todo.spring.todolist.exceptions.TodoNotFoundException;
import com.kevin.todo.spring.todolist.model.TodoItemDao;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.repository.TodoRepository;
import com.kevin.todo.spring.todolist.utils.TodoHandler;
import com.kevin.todo.spring.todolist.view.TodoItemView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TodoListService implements TodoItemService {

    private static final String TODO_NOT_FOUND = "Sorry, Todo Not founded with id : ";
    private static final String EMPTY_STRING = "";
    private final TodoRepository repository;
    private final ObjectMapper objectMapper;

    public TodoListService(TodoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public TodoItemView addTodo(TodoItemModel todoItemModel) {
        final var todo = TodoHandler.getTodoDaoFromTodoModel(todoItemModel);
        final var todoSaved = repository.save(todo);

        return TodoHandler.renderViewFromTodoDao(todoSaved, EMPTY_STRING);
    }

    @Override
    public TodoItemView retrieveOneTodo(int todoId) {
        final var searchedTodo = repository.findById(todoId).orElseThrow(() -> triggerTodoNotFoundException(todoId));

        return TodoHandler.renderViewFromTodoDao(searchedTodo, EMPTY_STRING);
    }

    private TodoNotFoundException triggerTodoNotFoundException(int todoId) {
        throw new TodoNotFoundException(TODO_NOT_FOUND + todoId);
    }

    @Override
    public TodoItemView updateAllFieldsOfTodo(int todoId, String todoTitle, boolean totoIsCompleted, int order) {
        final var todoToUpdateId = repository.findById(todoId).orElseThrow(() -> triggerTodoNotFoundException(todoId)).getId();
        final var updatedTodo = new TodoItemDao(todoToUpdateId, todoTitle, totoIsCompleted, order);
        repository.save(updatedTodo);

        return TodoHandler.renderViewFromTodoDao(updatedTodo, EMPTY_STRING);
    }

    @Override
    public TodoItemView patchOneFieldOfTodo(int todoId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        final var todoToPatch = repository.findById(todoId).orElseThrow(() -> triggerTodoNotFoundException(todoId));
        final var patchedTodoItem = patch.apply(objectMapper.convertValue(todoToPatch, JsonNode.class));
        final var updatedTodoItem = objectMapper.treeToValue(patchedTodoItem, TodoItemDao.class);

        repository.save(updatedTodoItem);

        return TodoHandler.renderViewFromTodoDao(updatedTodoItem, EMPTY_STRING);
    }

    @Override
    public List<TodoItemView> retrieveAllTodos() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(todoItemData -> TodoHandler.renderViewFromTodoDao(todoItemData, EMPTY_STRING))
                .collect(Collectors.toList());
    }

    @Override
    public void removeTodo(int todoId) {
        final var todoItem = repository.findById(todoId).orElseThrow(() -> triggerTodoNotFoundException(todoId));
        repository.delete(todoItem);
    }

    @Override
    public void removeAllTodos() {
        repository.deleteAll();
    }

}
