package com.becomejavasenior.schedule;

import com.becomejavasenior.entity.Task;
import com.becomejavasenior.service.EmailService;
import com.becomejavasenior.service.TaskService;
import com.becomejavasenior.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DailyUncompletedTasksReminding {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private static final String EMAIL_SUBJECT = "Uncompleted tasks reminding";
    @Autowired
    private static final String EMAIL_FROM = "Office reminder";

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeInternal() {
        List<Task> taskList = taskService.getUncompletedTasks();
        Map<Integer, List<Task>> tableUserIdTaskList = getTableOfRespUserIdAndTaslList(taskService.getAll());
        Set<Integer> respUserIdList = tableUserIdTaskList.keySet();
        Map<Integer, String> tableUserIdEmail = getTableOfUserIdAndEmail(tableUserIdTaskList.keySet());
        List<SimpleMailMessage> emailList = createEmailList(tableUserIdTaskList, tableUserIdEmail);
        emailService.sendEmails(emailList);
        System.out.println("Send remindings");
    }

    private Map<Integer, List<Task>> getTableOfRespUserIdAndTaslList(List<Task> taskList) {
        Map<Integer, List<Task>> tableUserIdTaskList = new HashMap<>();
        for (Task task : taskList) {
            int respUserId = task.getResponsibleUser().getId();
            if (!tableUserIdTaskList.containsKey(tableUserIdTaskList)) {
                tableUserIdTaskList.put(respUserId, new ArrayList<>());
            }
            List<Task> respUserTaskList = tableUserIdTaskList.get(respUserId);
            respUserTaskList.add(task);
            tableUserIdTaskList.put(respUserId, respUserTaskList);
        }
        return tableUserIdTaskList;
    }

    private Map<Integer, String> getTableOfUserIdAndEmail(Set<Integer> respUserIdList) {
        Map<Integer, String> tableUserIdEmail = new HashMap<>();
        for (int id : respUserIdList) {
            tableUserIdEmail.put(id, userService.getById(id).getEmail());
        }
        return tableUserIdEmail;
    }

    private List<SimpleMailMessage> createEmailList(Map<Integer, List<Task>> tableIdTasks,
                                                    Map<Integer, String> tableIdEmail) {
        List<SimpleMailMessage> emails = new ArrayList<>(tableIdTasks.size());
        for (int id : tableIdTasks.keySet()) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("Office reminder");
            email.setSubject(EMAIL_SUBJECT);
            StringBuffer emailBody = new StringBuffer("Good evening,\n");
            emailBody.append("You have ").append(tableIdTasks.get(id).size()).append(" not finished tasks:\n");
            List<Task> tasks = tableIdTasks.get(id);
            for (int i = 0; i < tasks.size(); i++) {
                emailBody.append(id).append(" task: ").append(tasks.get(i).toString()).append("\n");
            }
            emailBody.append("Goog night.");
            email.setText(emailBody.toString());
            email.setTo(tableIdEmail.get(id));
            emails.add(email);
        }
        return emails;
    }
}
