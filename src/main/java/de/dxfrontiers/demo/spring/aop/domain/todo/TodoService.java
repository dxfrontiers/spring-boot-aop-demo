package de.dxfrontiers.demo.spring.aop.domain.todo;

import de.dxfrontiers.demo.spring.aop.adapter.analytics.AnalyticsInterface;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final AnalyticsInterface analyticsInterface;

    public TodoService(TodoRepository todoRepository, AnalyticsInterface analyticsInterface) {
        this.todoRepository = todoRepository;
        this.analyticsInterface = analyticsInterface;
    }

    public Collection<TodoEntity> listTodos() {
        LOG.trace("entering method listTodos");
        List<TodoEntity> todos = todoRepository.findAll();
        LOG.trace("leaving method listTodos, returning: " + todos);
        return todos;
    }

    public TodoEntity createTodo(String todo) {
        LOG.trace("entering method createTodo");
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodo(todo);
        todoRepository.save(todoEntity);
        analyticsInterface.triggerEvent("TodoCreated");
        LOG.trace("leaving method createTodo, returning: " + todoEntity);
        return todoEntity;
    }

    @Transactional
    public void markAsDone(long todoId) {
        LOG.trace("entering method markAsDone");
        TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo not found: " + todoId));
        todoEntity.setDone(true);
        todoRepository.save(todoEntity);
        analyticsInterface.triggerEvent("TodoMarkedAsDone");
        LOG.trace("leaving method markAsDone");
    }

    public void deleteTodo(long todoId) {
        LOG.trace("entering method deleteTodo");
        todoRepository.deleteById(todoId);
        analyticsInterface.triggerEvent("TodoDeleted");
        LOG.trace("leaving method deleteTodo");
    }

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TodoService.class);
}
