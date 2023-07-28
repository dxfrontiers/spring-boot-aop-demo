package de.dxfrontiers.demo.spring.aop.domain.todo;

import de.dxfrontiers.demo.spring.aop.adapter.web.TodoInterface;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public final class TodoEntity implements TodoInterface {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "todo")
    private String todo;

    @Column(name = "done")
    private boolean done = false;

    // BOILERPlATE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoEntity that = (TodoEntity) o;
        return done == that.done && Objects.equals(id, that.id) && Objects.equals(todo, that.todo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, todo, done);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
