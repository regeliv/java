package java_hell;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class ProjectService {
  private final ProjectRepository projectRepository;

  ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  List<Project> all() {
    return projectRepository.findAll();
  }

  Project createProject(Project project) {
    return projectRepository.save(project);
  }
}
