package java_hell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
  private String name;

  @Getter
  @Setter
  private String description;

  @ManyToMany
  @JoinTable(name = "project_users", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> users = new HashSet<>();

  public void addUser(User user) {
    users.add(user);
    user.addProjectInternal(this);
  }

  public void removeUser(User user) {
    users.remove(user);
    user.removeProjectInternal(this);

  }

  public Set<User> getUsers() {
    return Collections.unmodifiableSet(users);
  }
}
