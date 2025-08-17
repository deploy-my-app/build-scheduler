package com.deploymyapp.build_scheduler.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.deploymyapp.build_scheduler.controller.GithubEventController;
import com.deploymyapp.build_scheduler.controller.GithubProperties;

class GithubEventControllerTest {

    @Test
    void validatesSignatureCorrectly() throws Exception {
        String payload = "{\"some\":\"payload\"}";
        String secret = "test-secret";

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        String signature = "sha256=" + Hex.encodeHexString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

        GithubEventController controller = new GithubEventController(new GithubProperties(), new RestTemplate());
        Method m = GithubEventController.class.getDeclaredMethod("isSignatureValid", String.class, String.class, String.class);
        m.setAccessible(true);

        boolean valid = (boolean) m.invoke(controller, payload, signature, secret);
        assertTrue(valid);

        boolean invalid = (boolean) m.invoke(controller, payload, signature + "0", secret);
        assertFalse(invalid);
    }
}

