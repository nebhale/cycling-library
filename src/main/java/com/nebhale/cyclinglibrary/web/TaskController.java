
package com.nebhale.cyclinglibrary.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nebhale.cyclinglibrary.model.Task;
import com.nebhale.cyclinglibrary.repository.TaskRepository;

@Controller
@RequestMapping("/tasks")
final class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "", produces = ApplicationMediaType.TASK_VALUE)
    @ResponseBody
    Set<Task> find() {
        return this.taskRepository.find();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{taskId}", produces = ApplicationMediaType.TASK_VALUE)
    @ResponseBody
    Task read(@PathVariable Long taskId) {
        return this.taskRepository.read(taskId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{taskId}")
    void delete(@PathVariable Long taskId) {
        this.taskRepository.delete(taskId);
    }

}
