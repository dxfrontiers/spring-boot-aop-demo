package de.dxfrontiers.demo.spring.aop.adapter.web;

import de.dxfrontiers.demo.spring.aop.domain.todo.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public Collection<? extends TodoInterface> getAllTodos() {
        return todoService.listTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoInterface createTodo(@RequestParam(name = "todo") String todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}/done")
    public void markTodoAsDone(@PathVariable(name = "id") long id) {
        todoService.markAsDone(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable(name = "id") long id) {
        todoService.deleteTodo(id);
    }
}
