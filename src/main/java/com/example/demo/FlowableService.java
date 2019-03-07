package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Transactional
public class FlowableService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HistoryService historyService;

    @Transactional
    public List<String> startProcess(JsonNode jsonNode) {

        System.out.println(jsonNode);

        String subject = "Appraisal Cycle Stared";
        String body = "Zetalent";
        String managerEmail = "";
        StringBuffer managerBody = new StringBuffer("List of employees started appraisal cycle\n");
        List<String> processIds = new ArrayList<>();
        for (JsonNode employeeJson : jsonNode){
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("appraisal",employeeJson);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("appraisalCycle", variables);
            emailService.sendEmailConfirmation(employeeJson.get("employeeEmail").asText(),body,subject);
            managerBody.append(employeeJson.get("name") + "\n");
            processIds.add(processInstance.getId());

            Task task = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();
            System.out.println(task.getName());
            System.out.println("process started");

            System.out.println("Number of tasks after process start: "
                    + taskService.createTaskQuery().count());
            System.out.println("process instance name"+ processInstance.getProcessDefinitionName() + processInstance.getStartUserId() + processInstance.getProcessDefinitionKey());

            System.out.println("running process" + runtimeService.createProcessInstanceQuery().count());
            System.out.println("process instance id " + processInstance.getId());
            managerEmail = employeeJson.get("managerEmail").asText();
        }


        emailService.sendEmailConfirmation(managerEmail,String.valueOf(managerBody),subject);


//        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
//                        .processInstanceId(processInstance.getId())
//                        .finished()
//                        .orderByHistoricActivityInstanceEndTime().asc()
//                        .list();
//
//        for (HistoricActivityInstance activity : activities) {
//            System.out.println(activity.getActivityId() + " took "
//                    + activity.getDurationInMillis() + " milliseconds");
//        }


        //taskService.complete(task.getId());
//        Task task2 = taskService.createTaskQuery()
//                .processInstanceId(processInstance.getId())
//                .singleResult();
//        System.out.println(task2.getName());
//        taskService.complete(task2.getId());
//        Task task3 = taskService.createTaskQuery()
//                .processInstanceId(processInstance.getId())
//                .singleResult();
//        System.out.println(task3.getName());
//        taskService.complete(task3.getId());
//        Task task4 = taskService.createTaskQuery()
//                .processInstanceId(processInstance.getId())
//                .singleResult();
//        System.out.println(task4.getName());
//        taskService.complete(task4.getId());
//        Task task5 = taskService.createTaskQuery()
//                .processInstanceId(processInstance.getId())
//                .singleResult();
//        System.out.println(task5.getName());
//        taskService.complete(task5.getId());
//        List<Task> a =   taskService.createTaskQuery().processInstanceId(processInstance.getStartUserId()).list();
//        for( Task x : a ){
//            System.out.println(x.getName());
//       }
//        for (HistoricActivityInstance activity : activities) {
//            System.out.println(activity.getActivityId() + " took "
//                    + activity.getDurationInMillis() + " milliseconds");
//        }

        return processIds;
    }

    @Transactional
    public String endCurrentTask(JsonNode jsonNode){
        System.out.println("task json" + jsonNode);
        Task task = taskService.createTaskQuery()
                .processInstanceId(jsonNode.get("processId").asText())
                .singleResult();
        System.out.println("task name :" + task.getName());
        taskService.complete(task.getId());

        return task.getName();
    }

    @Transactional
    public String currentProcessStatus(JsonNode jsonNode){
        Task task = taskService.createTaskQuery()
                .processInstanceId(jsonNode.get("processId").asText())
                .singleResult();

        return task.getName();
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}
