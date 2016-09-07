package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.Task;
import com.becomejavasenior.jdbc.entity.TaskDAO;
import com.becomejavasenior.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDAO taskDAO;

    @Override
    public List<Task> getUncompletedTasks() {
        List<Task> taskList = new ArrayList<>();
        for (Task task : taskDAO.getAll()) {
            if (!task.isCompleted()) {
                taskList.add(task);
            }
        }
        return taskList;
    }

    @Override
    public int insert(Task task) {
        return taskDAO.insert(task);
    }

    @Override
    public void update(Task task) {
        taskDAO.update(task);
    }

    @Override
    public List<Task> getAll() {
        return taskDAO.getAll();
    }

    @Override
    public Task getById(int id) {
        return taskDAO.getById(id);
    }

    @Override
    public void delete(int id) {
        taskDAO.delete(id);
    }

    @Override
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
}
