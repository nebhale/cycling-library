/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
