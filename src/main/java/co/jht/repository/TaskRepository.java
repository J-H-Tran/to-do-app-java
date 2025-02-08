package co.jht.repository;

import co.jht.model.domain.persist.tasks.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findByUserIdOrderById(Long userId);
    Optional<TaskItem> findByTaskCode(String taskCode);
}