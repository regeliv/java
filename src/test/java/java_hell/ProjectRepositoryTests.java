package java_hell;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class ProjectRepositoryTest {

  @Autowired
  private ProjectRepository projectRepository;

  @Test
  void savesAndFinds() {
    Project p = new Project();
    p.setName("Java Hell");
    p.setDescription("🤮");

    Project saved = projectRepository.save(p);

    Optional<Project> found = projectRepository.findById(saved.getId());

    assertTrue(found.isPresent());
    assertEquals(found.orElseThrow().getName(), "Java Hell");
    assertEquals(found.orElseThrow().getDescription(), "🤮");
  }

}
