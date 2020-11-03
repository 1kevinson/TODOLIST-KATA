package com.kevin.todo.spring.todolist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.kevin.todo.spring.todolist.controller.TodoController;
import com.kevin.todo.spring.todolist.exceptions.TodoNotFoundException;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.service.TodoListService;
import com.kevin.todo.spring.todolist.view.TodoItemView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Controller Test")
@WebMvcTest(TodoController.class)
public class TodolistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoListService service;


    @Test
    @DisplayName("Should retrieve all the todos")
    void shouldRetrieveAllTodos() throws Exception {
        final var todoListResult = List.of(
                new TodoItemView(1, "Task one", false, 10, "http://localhost/todos/1"),
                new TodoItemView(2, "Task two", false, 15, "http://localhost/todos/2"),
                new TodoItemView(3, "Task three", false, 20, "http://localhost/todos/3")
        );

        when(service.retrieveAllTodos()).thenReturn(todoListResult);

        mockMvc.perform(get("/todos").accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TodoItemModel())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(todoListResult)));
    }


    @Test
    @DisplayName("Should retrieve one todo")
    void shouldRetrieveOneTodo() throws Exception {
        final var todo = new TodoItemView(1, "Task one", false, 10, "http://localhost/todos/1");

        when(service.retrieveOneTodo(1)).thenReturn(todo);

        mockMvc.perform(get("/todos/" + 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andExpect(jsonPath("$.order").value(todo.getOrder()))
                .andExpect(jsonPath("$.url").value(todo.getUrl()));
    }

    @Test
    @DisplayName("should return 404 Not Found when trying to get an unknown todo item")
    void shouldReturnNotFoundExceptionWhenTryingToGetUnknownItem() throws Exception {

        when(service.retrieveOneTodo(2)).thenThrow(TodoNotFoundException.class);

        mockMvc.perform(get("/todos/" + 2).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should create one Todo and return its value")
    void shouldCreateOneTodoAndReturnItsValue() throws Exception {
        final var todoCreated = new TodoItemView(1, "Task one", false, 10, "http://localhost/todos/1");

        when(service.addTodo(any(TodoItemModel.class))).thenReturn(todoCreated);

        mockMvc.perform(post("/todos").content("{\"title\":\"dump task\",\"completed\":false,\"order\":10}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(todoCreated.getId()))
                .andExpect(jsonPath("$.title").value(todoCreated.getTitle()))
                .andExpect(jsonPath("$.completed").value(todoCreated.isCompleted()))
                .andExpect(jsonPath("$.order").value(todoCreated.getOrder()))
                .andExpect(jsonPath("$.url").value(todoCreated.getUrl()));
    }

    @Test
    @DisplayName("Should not be able to create todo if null value is present in title")
    void shouldNotBeAbleToCreateOneTodoIfThereIsNullValueInTitle() throws Exception {
        final var todoCreated = new TodoItemView(1, "Task one", false, 10, "http://localhost/todos/1");

        when(service.addTodo(any(TodoItemModel.class))).thenReturn(todoCreated);

        mockMvc.perform(post("/todos").content("{\"title\":null,\"completed\":false,\"order\":0}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Should globally update one Todo and return the updated value")
    void updateOneTodoAndReturnItsValue() throws Exception {
        final var todoUpdated = new TodoItemView(1, "Updated Task", true, 12, "http://localhost/todos/1");

        when(service.updateAllFieldsOfTodo(
                todoUpdated.getId(),
                todoUpdated.getTitle(),
                todoUpdated.isCompleted(),
                todoUpdated.getOrder()
        )).thenReturn(todoUpdated);

        mockMvc.perform(put("/todos/" + 1).content("{\"title\":\"Updated Task\",\"completed\":true,\"order\":12}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(todoUpdated.getId()))
                .andExpect(jsonPath("$.title").value(todoUpdated.getTitle()))
                .andExpect(jsonPath("$.completed").value(todoUpdated.isCompleted()))
                .andExpect(jsonPath("$.order").value(todoUpdated.getOrder()))
                .andExpect(jsonPath("$.url").value(todoUpdated.getUrl()));
    }

    @Test  // This test is not passing 😓
    @DisplayName("Should partially update one Todo and return the patched value")
    void partialUpdateOneTodoAndReturnItsValue() throws Exception {
        final var oldTodo = new TodoItemView(1, "Updated Task", true, 12, "http://localhost/todos/1");
        final var todoPatched = new TodoItemView(1, "Updated Task", false, 101, "http://localhost/todos/1");

        when(service.patchOneFieldOfTodo(oldTodo.getId(), eq(any(JsonPatch.class)))).thenReturn(todoPatched);

        mockMvc.perform(patch("/todos/" + 1).content("[{\"op\":\"replace\",\"path\":\"/completed\",\"value\":false},{\"op\":\"replace\",\"path\":\"/orders\",\"value\":101}]")
                .contentType("application/json-patch+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(todoPatched.getId()))
                .andExpect(jsonPath("$.title").value(oldTodo.getTitle()))
                .andExpect(jsonPath("$.completed").value(todoPatched.isCompleted()))
                .andExpect(jsonPath("$.order").value(todoPatched.getOrder()))
                .andExpect(jsonPath("$.url").value(todoPatched.getUrl()));
    }


    @Test
    @DisplayName("should delete all the todos items")
    void shouldDeleteAllTheTodoItems() throws Exception {
        mockMvc.perform(delete("/todos/"))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("should delete one todo Update")
    void shouldDeleteOneTodoItem() throws Exception {
        mockMvc.perform(delete("/todos/{id}", 1))
                .andExpect(status().isNoContent());
    }


    @Test
    @DisplayName("should throw 404 not found exception when trying to delete one todo that not exists")
    void shouldThrowsANotFoundExceptionWhenTryingToDeleteOneTodoNotExist() throws Exception {

        doThrow(TodoNotFoundException.class).when(service).removeTodo(2);

        mockMvc.perform(delete("/todos/" + 2).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

