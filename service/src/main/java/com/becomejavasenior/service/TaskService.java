package com.becomejavasenior.service;

import com.becomejavasenior.entity.Task;
import com.becomejavasenior.jdbc.entity.TaskDAO;

import java.util.List;

public interface TaskService extends GenericService<Task> {

    List<Task> getUncompletedTasks();

    void setTaskDAO(TaskDAO taskDAO);
}
