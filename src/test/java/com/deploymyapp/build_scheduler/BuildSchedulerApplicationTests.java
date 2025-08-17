package com.deploymyapp.build_scheduler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "github.webhook-secret=test-secret",
        "github.allowed-organizations=allowed-org"
})
@AutoConfigureMockMvc
class BuildSchedulerApplicationTests {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void contextLoads() {
        }

        @Test
        void invalidSignatureReturns401() throws Exception {
                mockMvc.perform(post("/webhooks/github")
                                .content("{}")
                                .header("X-GitHub-Event", "push")
                                .header("X-Hub-Signature-256", "sha256=invalid")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void validSignatureAndAllowedOrgReturns200() throws Exception {
                String payload = """
                                {
                                  \"ref\": \"refs/heads/main\",
                                  \"before\": \"\",
                                  \"after\": \"\",
                                  \"repository\": {
                                    \"name\": \"repo\",
                                    \"full_name\": \"allowed-org/repo\",
                                    \"owner\": { \"login\": \"allowed-org\" }
                                  },
                                  \"pusher\": { \"name\": \"\", \"email\": \"\" },
                                  \"organization\": { \"login\": \"allowed-org\", \"id\": 1, \"node_id\": \"\", \"url\": \"\", \"repos_url\": \"\", \"events_url\": \"\", \"hooks_url\": \"\", \"issues_url\": \"\", \"members_url\": \"\", \"public_members_url\": \"\", \"avatar_url\": \"\", \"description\": \"\" },
                                  \"sender\": { \"login\": \"\", \"id\": 1, \"node_id\": \"\", \"avatar_url\": \"\", \"gravatar_id\": \"\", \"url\": \"\", \"html_url\": \"\", \"followers_url\": \"\", \"following_url\": \"\", \"gists_url\": \"\", \"starred_url\": \"\", \"subscriptions_url\": \"\", \"organizations_url\": \"\", \"repos_url\": \"\", \"events_url\": \"\", \"received_events_url\": \"\", \"type\": \"\", \"user_view_type\": \"\", \"site_admin\": false },
                                  \"installation\": { \"id\": 1, \"node_id\": \"\" },
                                  \"created\": false,
                                  \"deleted\": false,
                                  \"forced\": false,
                                  \"base_ref\": \"\",
                                  \"compare\": \"\",
                                  \"commits\": [],
                                  \"head_commit\": {
                                    \"id\": \"\",
                                    \"tree_id\": \"\",
                                    \"distinct\": false,
                                    \"message\": \"\",
                                    \"timestamp\": \"\",
                                    \"url\": \"\",
                                    \"author\": { \"name\": \"\", \"email\": \"\", \"username\": \"\" },
                                    \"committer\": { \"name\": \"\", \"email\": \"\", \"username\": \"\" },
                                    \"added\": [],
                                    \"removed\": [],
                                    \"modified\": []
                                  }
                                }
                                """;
                String signature = "sha256=" + hmacSha256(payload, "test-secret");
                mockMvc.perform(post("/webhooks/github")
                                .content(payload)
                                .header("X-GitHub-Event", "push")
                                .header("X-Hub-Signature-256", signature)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        private String hmacSha256(String data, String secret) throws Exception {
                Mac hmac = Mac.getInstance("HmacSHA256");
                SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                return Hex.encodeHexString(hmac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        }
}
