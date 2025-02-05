package co.jht.repository;

import co.jht.entity.tasks.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findByUserIdOrderById(Long userId);
}