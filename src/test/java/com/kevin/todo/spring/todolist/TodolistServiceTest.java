package com.kevin.todo.spring.todolist;

import com.kevin.todo.spring.todolist.exceptions.TodoNotFoundException;
import com.kevin.todo.spring.todolist.model.TodoItemDao;
import com.kevin.todo.spring.todolist.model.TodoItemModel;
import com.kevin.todo.spring.todolist.repository.TodoRepository;
import com.kevin.todo.spring.todolist.service.TodoListService;
import com.kevin.todo.spring.todolist.view.TodoItemView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodolistServiceTest {

    private final static String EMPTY_URL_STRING = "";
    @InjectMocks
    private TodoListService service;
    @Mock
    private TodoRepository repository;

    @Test
    @DisplayName("Should be able to create a todo")
    void shouldBeAbleToAddATodo() {
        // Arrange
        when(repository.save(any(TodoItemDao.class))).thenReturn(new TodoItemDao(1, "dump todo", false, 15));

        // Act
        final var actual = service.addTodo(new TodoItemModel());

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new TodoItemView(1, "dump todo", false, 15, EMPTY_URL_STRING));

        verify(repository).save(any(TodoItemDao.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should be able to retrieve an existing todo")
    void shouldBeAbleToRetrieveOneTodo() {
        // Arrange
        when(repository.findById(anyInt())).thenReturn(Optional.of(new TodoItemDao(1, "dump todo", false, 15)));

        // Act
        final var actual = service.retrieveOneTodo(1);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new TodoItemView(1, "dump todo", false, 15, EMPTY_URL_STRING));

        verify(repository).findById(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should Not be able to retrieve a todo that doesn't exist")
    void shouldNotBeAbleToRetrieveATodoThatNotExist() {
        // Arrange
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        // Assert
        Assertions.assertThrows(TodoNotFoundException.class, () -> service.retrieveOneTodo(1));

        verify(repository).findById(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should be able to retrieve all todos")
    void shouldBeAbleToRetrieveAllTodo() {
        // Arrange
        final var todos = List.of(
                new TodoItemDao(1, "dump todo 1", false, 11),
                new TodoItemDao(2, "dump todo 2", false, 12)
        );

        // Act
        when(repository.findAll()).thenReturn(todos);

        // Assert
        assertThat(service.retrieveAllTodos())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new TodoItemView(1, "dump todo 1", false, 11, EMPTY_URL_STRING),
                        new TodoItemView(2, "dump todo 2", false, 12, EMPTY_URL_STRING)
                ));

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should be able to update one todo")
    void shouldBeAbleToUpdateTodo() {
        // Arrange
        when(repository.findById(anyInt())).thenReturn(Optional.of(new TodoItemDao(1, "dump todo", false, 15)));

        // Act
        final var actual = service.updateAllFieldsOfTodo(1, "dump updated", false, 5);

        // Assert
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new TodoItemView(1, "dump updated", false, 5, EMPTY_URL_STRING));

        verify(repository).findById(anyInt());
        verify(repository).save(any(TodoItemDao.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should be able to delete one todo")
    void shouldBeAbleToRemoveTodo() {
        // Arrange
        when(repository.findById(anyInt())).thenReturn(Optional.of(new TodoItemDao(1, "dump todo", false, 15)));

        // Act
        service.removeTodo(1);

        // Assert
        verify(repository).delete(any(TodoItemDao.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should Be able to delete all todos")
    void shouldBeAbleToRemoveAllTodo() {
        // Arrange

        // Act
        service.removeAllTodos();

        // Assert
        verify(repository).deleteAll();
        verifyNoMoreInteractions(repository);
    }
}
