package com.example.demo;


import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.ValidationException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;

@RestController
public class FlowableController {

    @Autowired
    private FlowableService myService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/startProcess", method= RequestMethod.POST)
    public List<String> startProcessInstance(@RequestBody JsonNode jsonNode) throws JSONException {
        return myService.startProcess(jsonNode);
    }

    @RequestMapping(value="/currentStatus")
    public String currentProcessInstance(@RequestBody JsonNode jsonNode) {
        return myService.currentProcessStatus(jsonNode);
    }

    @RequestMapping(value="/endTask", method= RequestMethod.POST)
    public void endTask(@RequestBody JsonNode jsonNode) {
        System.out.println("end task method");
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
        myService.getTasks(jsonNode.get("assignee").asText());

    }


    @PostMapping(value = "/calendarinvitation")
    public ResponseEntity<String> calendarInvitation(@RequestBody JsonNode jsonNode) throws ValidationException, URISyntaxException, MessagingException, IOException {
        System.out.println("send invitation controller called");
        return new ResponseEntity<>(emailService.invitation(jsonNode), HttpStatus.CREATED);
    }
}

