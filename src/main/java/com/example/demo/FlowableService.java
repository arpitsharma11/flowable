package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<String> processIds = new ArrayList<>();
        for (JsonNode employeeJson : jsonNode){
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("appraisal",employeeJson);
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("appraisalCycle", variables);
            processIds.add(processInstance.getId());
            System.out.println("process started");

            System.out.println("process instance name"+ processInstance.getProcessDefinitionName() + processInstance.getStartUserId() + processInstance.getProcessDefinitionKey());
            System.out.println("process instance id " + processInstance.getId());
        }


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
        System.out.println("task id " + task.getId());
        taskService.complete(task.getId());


        System.out.println("after task complete");
        return task.getName();
    }

    @Transactional
    public String currentProcessStatus(JsonNode jsonNode){
        Task task = taskService.createTaskQuery()
                .processInstanceId(jsonNode.get("processId").asText())
                .singleResult();

        List<Task> tasks = taskService.createTaskQuery().processInstanceId(jsonNode.get("processId").asText()).list();
        for (Task task1 : tasks){
            System.out.println("task name" + task1.getName());
        }
        return task.getName();
    }

    @Transactional
    public void getTasks(String assignee) {
        List<Task> tasks =  taskService.createTaskQuery().taskAssignee(assignee).list();
        for (Task task : tasks){
            System.out.println("assignee" + task.getAssignee());
            System.out.println("task name" + task.getName());
        }
    }

}
