package com.example.demo;


import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class FlowableController {

    @Autowired
    private FlowableService myService;

    @RequestMapping(value="/startProcess", method= RequestMethod.POST)
    public int startProcessInstance(@RequestBody JsonNode jsonNode) throws JSONException {
        return myService.startProcess(jsonNode);
    }

    @RequestMapping(value="/currentStatus")
    public HashMap<String, Object> currentProcessInstance(@RequestBody JsonNode jsonNode) {
        return myService.currentProcessStatus(jsonNode);
    }

    @RequestMapping(value="/endTask")
    public void endTask(@RequestBody JsonNode jsonNode) {
        myService.endCurrentTask(jsonNode);
    }

    @RequestMapping(value="/tasks", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public void getTasks(@RequestBody JsonNode jsonNode) {
        System.out.println("tasks called");
//        List<Task> tasks = myService.getTasks(assignee);
//        List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
//        for (Task task : tasks) {
//            dtos.add(new TaskRepresentation(task.getId(), task.getName()));
//        }
//        System.out.println(dtos);

        System.out.println(jsonNode);

    }



}

