package java_hell;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
class Task {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Getter
  @Setter
  @Column(length = 255)
  @Size(min = 1, max = 255)
  @NotNull
  private String title;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private TaskPriority priority;

  @Getter
  @ManyToOne
  @JsonIgnore
  private Project project;

  void setProjectInternal(Project project) {
    this.project = project;
  }
}
