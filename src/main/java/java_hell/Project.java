package java_hell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
class Project {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Getter
  @Setter
  @NotNull
  @Column(length = 255)
  @Size(min = 1, max = 255)
  private String name;

  @Getter
  @Setter
  private String description;

  @ManyToMany
  @JoinTable(name = "project_users", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> users = new HashSet<>();

  @OneToMany(mappedBy = "project")
  private Set<Task> tasks = new HashSet<>();

  public void addUser(User user) {
    users.add(user);
    user.addProjectInternal(this);
  }

  public void removeUser(User user) {
    users.remove(user);
    user.removeProjectInternal(this);
  }

  public void addTask(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("Cannot add null task");
    }

    if (task.getProject() != null) {
      task.getProject().removeTask(task);
    }

    tasks.add(task);
    task.setProjectInternal(this);
  }

  public void removeTask(Task task) {
    if (!tasks.remove(task)) {
      throw new IllegalArgumentException("The task is not part of the project");
    }

    task.setProjectInternal(null);
  }

  public Set<User> getUsers() {
    return Collections.unmodifiableSet(users);
  }

  public Set<Task> getTasks() {
    return Collections.unmodifiableSet(tasks);
  }

}
