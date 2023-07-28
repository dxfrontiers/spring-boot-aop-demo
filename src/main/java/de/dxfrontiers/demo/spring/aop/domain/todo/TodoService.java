package de.dxfrontiers.demo.spring.aop.domain.todo;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Collection<TodoEntity> listTodos() {
        return todoRepository.findAll();
    }

    public TodoEntity createTodo(String todo) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodo(todo);
        todoRepository.save(todoEntity);
        return todoEntity;
    }

    public void markAsDone(long todoId) {
        TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo not found: " + todoId));
        todoEntity.setDone(true);
        todoRepository.save(todoEntity);
    }
}
