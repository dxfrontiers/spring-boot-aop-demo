package de.dxfrontiers.demo.spring.aop.domain.todo;

import de.dxfrontiers.demo.spring.aop.aspects.analytics.AnalyticEvent;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Collection<TodoEntity> listTodos() {
        LOG.trace("entering method listTodos");
        List<TodoEntity> todos = todoRepository.findAll();
        LOG.trace("leaving method listTodos, returning: " + todos);
        return todos;
    }

    @AnalyticEvent("TodoCreated")
    public TodoEntity createTodo(String todo) {
        LOG.trace("entering method createTodo");
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodo(todo);
        todoRepository.save(todoEntity);
        LOG.trace("leaving method createTodo, returning: " + todoEntity);
        return todoEntity;
    }

    @AnalyticEvent("TodoMarkedAsDone")
    @Transactional
    public void markAsDone(long todoId) {
        LOG.trace("entering method markAsDone");
        TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo not found: " + todoId));
        todoEntity.setDone(true);
        todoRepository.save(todoEntity);
        LOG.trace("leaving method markAsDone");
    }

    @AnalyticEvent("TodoDeleted")
    public void deleteTodo(long todoId) {
        LOG.trace("entering method deleteTodo");
        todoRepository.deleteById(todoId);
        LOG.trace("leaving method deleteTodo");
    }

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TodoService.class);
}
