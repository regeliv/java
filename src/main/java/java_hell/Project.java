package java_hell;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
