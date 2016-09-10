package com.becomejavasenior.service;

import com.becomejavasenior.entity.Task;

import java.util.List;

public interface TaskService extends GenericService<Task> {

    List<Task> getUncompletedTasks();

}
