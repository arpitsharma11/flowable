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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FlowableService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistoryService historyService;

    @Transactional
    public int startProcess(JsonNode jsonNode) {
        Map<String, Object> variables = new HashMap<String, Object>();
        //System.out.println("appraisal json "+id);
        //variables.put("id",id );
        //System.out.println("variables"+variables);
       System.out.println(jsonNode);
//
//        System.out.println("key" + jsonNode.get("key"));
//        System.out.println("value" + jsonNode.get("value"));
        variables.put("id",jsonNode);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("appraisalCycle", variables);
        System.out.println("process started");

        System.out.println("Number of tasks after process start: "
                + taskService.createTaskQuery().count());
        System.out.println("process instance name"+ processInstance.getProcessDefinitionName() + processInstance.getStartUserId() + processInstance.getProcessDefinitionKey());

        System.out.println("running process" + runtimeService.createProcessInstanceQuery().count());
        System.out.println("process instance id " + processInstance.getId());
        List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        System.out.println(task.getName());
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
        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }
        return 1;

    }

    @Transactional
    public  void endCurrentTask(JsonNode jsonNode){
        Task task = taskService.createTaskQuery()
                .processInstanceId(jsonNode.get("processId").asText())
                .singleResult();
        taskService.complete(task.getId());
        //return processInstance;
        System.out.println();
    }

    @Transactional
    public HashMap<String, Object> currentProcessStatus(JsonNode jsonNode){
        Task task = taskService.createTaskQuery()
                .processInstanceId(jsonNode.get("processId").asText())
                .singleResult();
        //taskService.complete(task.getId());
        //return JSONObject.quote(task.getName());
        HashMap<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("current_status", task.getName());


        return response;
    }

    @Transactional
    public List<Task> getTasks(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

}
