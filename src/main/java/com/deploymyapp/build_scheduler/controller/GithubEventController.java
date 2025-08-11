package com.deploymyapp.build_scheduler.controller;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GithubEventController {

    private final GithubProperties githubProperties;
    private final RestTemplate restTemplate;

    public GithubEventController(final GithubProperties githubProperties, final RestTemplate restTemplate) {
        this.githubProperties = githubProperties;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/webhooks/github")
    public ResponseEntity<Void> handleGithubWebhook(
            @RequestBody final String payload,
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature256) {
        if (!isSignatureValid(payload, signature256, githubProperties.getWebhookSecret())) {
            log.info("Invalid signature for GitHub webhook");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GithubPushEventPayload pushEvent = parsePayload(payload);
        triggerGithubWorkflow(pushEvent.repository().name(), pushEvent.repository().owner().login(), pushEvent.ref());
        return ResponseEntity.ok().build();
    }

    private boolean isSignatureValid(String payload, String signature256, String secret) {
        if (signature256 == null || !signature256.startsWith("sha256=")) {
            return false;
        }
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmac.init(key);
            String expectedHex = Hex.encodeHexString(hmac.doFinal(payload.getBytes()));
            String receivedHex = signature256.substring("sha256=".length());
            return MessageDigest.isEqual(expectedHex.getBytes(), receivedHex.getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    private GithubPushEventPayload parsePayload(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload, GithubPushEventPayload.class);
        } catch (Exception e) {
            log.info("Failed to parse payload: {}", e.getMessage());
            throw new RuntimeException("Failed to parse payload", e);
        }
    }

    private void triggerGithubWorkflow(String repoName, String repoOwner, String ref) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(githubProperties.getOrgaPat());
        // Le paramètre "ref" doit être la branche à utiliser, par exemple "main"
        String body = String.format("{\"ref\": \"%s\",\"inputs\":{\"repo_name\":\"%s\",\"repo_owner\":\"%s\"}}", ref,
                repoName, repoOwner);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            restTemplate.postForEntity(githubProperties.getJobUrl(), entity, String.class);
            log.info("GitHub workflow triggered successfully for repository: {}, owner: {}, ref: {}", repoName,
                    repoOwner, ref);
        } catch (Exception e) {
            log.info("Failed to trigger GitHub workflow: {}", e.getMessage());
        }
    }

}
