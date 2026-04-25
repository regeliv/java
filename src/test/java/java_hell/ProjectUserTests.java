package java_hell;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class ProjectUserTests {

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  UserRepository userRepository;

  @Test
  void addManyUsersToOneProject() {
    Project proj = new Project();
    proj.setName("Java Hell");
    proj.setDescription("🤮");

    User foo = new User();
    foo.setUsername("foo");

    User bar = new User();
    bar.setUsername("bar");

    foo = userRepository.save(foo);
    bar = userRepository.save(bar);
    proj = projectRepository.save(proj);

    proj.addUser(foo);
    proj.addUser(bar);

    proj = projectRepository.findById(proj.getId()).orElseThrow();
    assertEquals(2, proj.getUsers().size());
    assertEquals(1, foo.getProjects().size());
    assertEquals(1, bar.getProjects().size());
  }

  @Test
  void addOneUserToManyProjects() {
    Project fooProj = new Project();
    fooProj.setName("Foo");
    fooProj.setDescription("desc");

    Project barProj = new Project();
    barProj.setName("Bar");
    barProj.setDescription("desc");

    User bazUser = new User();
    bazUser.setUsername("baz");

    fooProj = projectRepository.save(fooProj);
    barProj = projectRepository.save(barProj);

    bazUser = userRepository.save(bazUser);

    fooProj.addUser(bazUser);
    barProj.addUser(bazUser);

    assertEquals(2, bazUser.getProjects().size());
    assertEquals(1, fooProj.getUsers().size());
    assertEquals(1, barProj.getUsers().size());
  }

  @Test
  void removeUserFromProject() {
    Project proj = new Project();
    proj.setName("Java Hell");
    proj.setDescription("🤮");

    User user = new User();
    user.setUsername("foo");

    user = userRepository.save(user);
    proj = projectRepository.save(proj);

    proj.addUser(user);
    assertEquals(1, user.getProjects().size());
    assertEquals(1, proj.getUsers().size());

    proj.removeUser(user);
    assertEquals(0, user.getProjects().size());
    assertEquals(0, proj.getUsers().size());
  }
}
