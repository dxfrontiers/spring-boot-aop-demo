package de.dxfrontiers.demo.spring.aop.domain.todo;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Collection;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final PlatformTransactionManager txManager;

    public TodoService(TodoRepository todoRepository, PlatformTransactionManager txManager) {
        this.todoRepository = todoRepository;
        this.txManager = txManager;
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
        LOG.trace("leaving method createTodo, returning: " + todoEntity);
        return todoEntity;
    }

    public void markAsDone(long todoId) {
        LOG.trace("entering method markAsDone");
        try {
            DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            transactionDefinition.setName("markAsDoneTx-" + todoId);
            transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = txManager.getTransaction(transactionDefinition);
            try {
                TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() -> new RuntimeException("Todo not found: " + todoId));
                todoEntity.setDone(true);
                todoRepository.save(todoEntity);
            } catch (Exception e) {
                LOG.warn("An SQL exception occurred", e);
                try {
                    txManager.rollback(status);
                } catch (Exception transactionException) {
                    LOG.trace("leaving method markAsDone");
                    throw transactionException;
                }
                throw e;
            }
            txManager.commit(status);
        }
        finally {
            LOG.trace("leaving method markAsDone");
        }
    }

    public void deleteTodo(long todoId) {
        LOG.trace("entering method deleteTodo");
        todoRepository.deleteById(todoId);
        LOG.trace("leaving method deleteTodo");
    }

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(TodoService.class);
}
