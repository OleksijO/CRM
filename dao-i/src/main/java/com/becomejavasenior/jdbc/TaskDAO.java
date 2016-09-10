package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.Task;

import java.util.Map;
import java.util.List;

public interface TaskDAO extends GenericDAO<Task> {

    List<String> getAllTaskStatus();

    List<String> getAllTaskType();

    Map<Integer, String> getTaskTypeList();
}
