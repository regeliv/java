package java_hell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
class User {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Getter
  @Setter
  @Column(length = 255)
  @Size(min = 1, max = 255)
  @NotNull
  private String username;

  @ManyToMany(mappedBy = "users")
  private Set<Project> projects = new HashSet<>();

  public Set<Project> getProjects() {
    return Collections.unmodifiableSet(projects);
  }

  void addProjectInternal(Project project) {
    projects.add(project);
  }

  void removeProjectInternal(Project project) {
    projects.remove(project);
  }
}
