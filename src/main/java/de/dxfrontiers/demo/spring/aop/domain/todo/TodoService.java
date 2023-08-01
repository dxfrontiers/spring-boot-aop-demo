package de.dxfrontiers.demo.spring.aop.domain.todo;

import de.dxfrontiers.demo.spring.aop.aspects.analytics.AnalyticEvent;
import de.dxfrontiers.demo.spring.aop.aspects.logging.Log;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Log
    public Collection<TodoEntity> listTodos() {
        return todoRepository.findAll();
    }

    @Log
    @AnalyticEvent("TodoCreated")
    public TodoEntity createTodo(String todo) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodo(todo);
        todoRepository.save(todoEntity);
        return todoEntity;
    }

    @Log
    @AnalyticEvent("TodoMarkedAsDone")
    @Transactional
    public void markAsDone(long todoId) {
        TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo not found: " + todoId));
        todoEntity.setDone(true);
        todoRepository.save(todoEntity);
    }

    @Log
    @AnalyticEvent("TodoDeleted")
    public void deleteTodo(long todoId) {
        todoRepository.deleteById(todoId);
    }

}
