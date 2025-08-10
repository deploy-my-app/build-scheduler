package com.deploymyapp.build_scheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubEventController {

    @PostMapping("/webhooks/github")
    public ResponseEntity<Void> handleGithubWebhook(
            @RequestBody final String payload,
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType) {
        System.out.println("Event Type: " + eventType);
        System.out.println("Payload: " + payload);
        return ResponseEntity.ok().build();
    }
}
