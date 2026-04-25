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

  Project updateProject(Long id, Project updatedProject) {
    Project currentProject = projectRepository.findById(id).orElseThrow();

    currentProject.setDescription(updatedProject.getDescription());
    currentProject.setName(updatedProject.getName());

    return projectRepository.save(currentProject);
  }

  void deleteProject(Long id) {
    Project project = projectRepository.findById(id).orElseThrow();

    if (project.getTasks().size() != 0 || project.getUsers().size() != 0) {
      throw new IllegalStateException("Cannot delete a project if it has tasks or users assigned to it");
    }

    projectRepository.delete(project);
  }

}
