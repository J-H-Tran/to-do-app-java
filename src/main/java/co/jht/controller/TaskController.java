//package co.jht.controller;
//
//import co.jht.model.domain.persist.tasks.TaskItem;
//import co.jht.model.domain.response.mapper.TaskItemMapper;
//import co.jht.model.domain.response.tasks.TaskItemCreateDTO;
//import co.jht.model.domain.response.tasks.TaskItemCreatedDTO;
//import co.jht.model.domain.response.tasks.TaskItemDTO;
//import co.jht.model.domain.response.tasks.TaskItemIdDTO;
//import co.jht.model.domain.response.tasks.TaskItemListedDTO;
//import co.jht.model.domain.response.tasks.TaskItemUpdateDTO;
//import co.jht.model.domain.response.tasks.TaskItemUserIdDTO;
//import co.jht.service.TaskService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotaNtion.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/tasks")
//public class TaskController {
//
//    private final TaskService taskService;
//    private final TaskItemMapper taskItemMapper;
//
//    @Autowired
//    public TaskController(
//            TaskService taskService,
//            TaskItemMapper taskItemMapper
//    ) {
//        this.taskService = taskService;
//        this.taskItemMapper = taskItemMapper;
//    }
//
//    @GetMapping("/user")
//    public ResponseEntity<List<TaskItemListedDTO>> getTasksByUserId(@RequestBody TaskItemUserIdDTO taskItemUserIdDTO) {
//        Long id = taskItemUserIdDTO.getUserId();
//        List<TaskItem> tasks = taskService.getTasksByUserId(id);
//        List<TaskItemListedDTO> taskDTOs = taskItemMapper.toDTO(tasks);
//        return ResponseEntity.ok(taskDTOs);
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<TaskItemCreatedDTO> createTask(@RequestBody TaskItemCreateDTO taskDTO) {
//        TaskItem task = taskItemMapper.toEntity(taskDTO);
//        TaskItem createdTask = taskService.createTask(task);
//        return ResponseEntity.ok(taskItemMapper.toCreatedDTO(createdTask));
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<TaskItemDTO> updateTask(@RequestBody TaskItemUpdateDTO taskDTO) {
//        TaskItem task = taskItemMapper.toEntity(taskDTO);
//        TaskItem updatedTask = taskService.updateTask(task);
//        return ResponseEntity.ok(taskItemMapper.toDTO(updatedTask));
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteTask(@RequestBody TaskItemIdDTO taskDTO) {
//        TaskItem task = taskItemMapper.toEntity(taskDTO);
//        taskService.deleteTask(task);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/update/due_date") //TODO:
//    public ResponseEntity<TaskItemDTO> updateTaskDueDate(@PathVariable("taskId") Long taskId, @RequestBody TaskItemDTO taskDTO) {
//        TaskItem updateTask = taskService.updateTaskDueDate(taskId, taskDTO.getDueDate());
//        return ResponseEntity.ok(taskItemMapper.toDTO(updateTask));
//    }
//
//    @PutMapping("/update/complete_status") //TODO:
//    public ResponseEntity<TaskItemDTO> updateCompleteStatus(@PathVariable Long taskId, @RequestBody TaskItemDTO taskDTO) {
//        TaskItem updateTask = taskService.updateTaskCompleteStatus(taskId, taskDTO.getCompleteStatus());
//        return ResponseEntity.ok(taskItemMapper.toDTO(updateTask));
//    }
//}