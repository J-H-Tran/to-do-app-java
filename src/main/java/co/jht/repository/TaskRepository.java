package co.jht.repository;

import co.jht.entity.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findByUserId(Long userId);
}