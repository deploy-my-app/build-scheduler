package com.deploymyapp.build_scheduler.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubPushEventPayload(
                String ref,
                String before,
                String after,
                Repository repository,
                Pusher pusher,
                Organization organization,
                Sender sender,
                Installation installation,
                boolean created,
                boolean deleted,
                boolean forced,
                String base_ref,
                String compare,
                List<Commit> commits,
                Commit head_commit) {
        public record Repository(
                        long id,
                        String node_id,
                        String name,
                        String full_name,
                        @JsonProperty("private") boolean private_,
                        Owner owner,
                        String html_url,
                        String description,
                        boolean fork,
                        String url,
                        String forks_url,
                        String keys_url,
                        String collaborators_url,
                        String teams_url,
                        String hooks_url,
                        String issue_events_url,
                        String events_url,
                        String assignees_url,
                        String branches_url,
                        String tags_url,
                        String blobs_url,
                        String git_tags_url,
                        String git_refs_url,
                        String trees_url,
                        String statuses_url,
                        String languages_url,
                        String stargazers_url,
                        String contributors_url,
                        String subscribers_url,
                        String subscription_url,
                        String commits_url,
                        String git_commits_url,
                        String comments_url,
                        String issue_comment_url,
                        String contents_url,
                        String compare_url,
                        String merges_url,
                        String archive_url,
                        String downloads_url,
                        String issues_url,
                        String pulls_url,
                        String milestones_url,
                        String notifications_url,
                        String labels_url,
                        String releases_url,
                        String deployments_url,
                        long created_at,
                        String updated_at,
                        long pushed_at,
                        String git_url,
                        String ssh_url,
                        String clone_url,
                        String svn_url,
                        String homepage,
                        int size,
                        int stargazers_count,
                        int watchers_count,
                        String language,
                        boolean has_issues,
                        boolean has_projects,
                        boolean has_downloads,
                        boolean has_wiki,
                        boolean has_pages,
                        boolean has_discussions,
                        int forks_count,
                        String mirror_url,
                        boolean archived,
                        boolean disabled,
                        int open_issues_count,
                        String license,
                        boolean allow_forking,
                        boolean is_template,
                        boolean web_commit_signoff_required,
                        List<String> topics,
                        String visibility,
                        int forks,
                        int open_issues,
                        int watchers,
                        String default_branch,
                        int stargazers,
                        String master_branch,
                        String organization,
                        Map<String, Object> custom_properties) {
        }

        public record Owner(
                        String name,
                        String email,
                        String login,
                        long id,
                        String node_id,
                        String avatar_url,
                        String gravatar_id,
                        String url,
                        String html_url,
                        String followers_url,
                        String following_url,
                        String gists_url,
                        String starred_url,
                        String subscriptions_url,
                        String organizations_url,
                        String repos_url,
                        String events_url,
                        String received_events_url,
                        String type,
                        String user_view_type,
                        boolean site_admin) {
        }

        public record Pusher(
                        String name,
                        String email) {
        }

        public record Organization(
                        String login,
                        long id,
                        String node_id,
                        String url,
                        String repos_url,
                        String events_url,
                        String hooks_url,
                        String issues_url,
                        String members_url,
                        String public_members_url,
                        String avatar_url,
                        String description) {
        }

        public record Sender(
                        String login,
                        long id,
                        String node_id,
                        String avatar_url,
                        String gravatar_id,
                        String url,
                        String html_url,
                        String followers_url,
                        String following_url,
                        String gists_url,
                        String starred_url,
                        String subscriptions_url,
                        String organizations_url,
                        String repos_url,
                        String events_url,
                        String received_events_url,
                        String type,
                        String user_view_type,
                        boolean site_admin) {
        }

        public record Installation(
                        long id,
                        String node_id) {
        }

        public record Commit(
                        String id,
                        String tree_id,
                        boolean distinct,
                        String message,
                        String timestamp,
                        String url,
                        Author author,
                        Committer committer,
                        List<String> added,
                        List<String> removed,
                        List<String> modified) {
        }

        public record Author(
                        String name,
                        String email,
                        String username) {
        }

        public record Committer(
                        String name,
                        String email,
                        String username) {
        }
}
