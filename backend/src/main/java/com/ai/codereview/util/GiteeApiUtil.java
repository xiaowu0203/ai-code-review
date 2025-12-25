package com.ai.codereview.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Gitee API工具类
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@Component
public class GiteeApiUtil {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final String GITEE_API_BASE = "https://api.gitee.com";

    /**
     * 获取PR的代码差异
     *
     * @param projectId Gitee项目ID
     * @param prNumber PR编号
     * @param token 访问令牌
     * @return 代码差异内容
     */
    public String getPullRequestDiff(Long projectId, Integer prNumber, String token) {
        try {
            // /enterprises/{enterprise_id}/projects/{project_id}/pull_requests/new/diff
            String url = String.format("%s/repos/%s/pulls/%d/diff",
                GITEE_API_BASE,
                projectId,
                prNumber);

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "token " + token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("获取PR差异失败，状态码: {}", response.code());
                    return null;
                }

                return response.body().string();
            }
        } catch (Exception e) {
            log.error("获取PR差异异常", e);
            return null;
        }
    }

    /**
     * 创建PR评论
     *
     * @param projectId Gitee项目ID
     * @param prNumber PR编号
     * @param comment 评论内容
     * @param token 访问令牌
     * @return 是否成功
     */
    public boolean createPRComment(Long projectId, Integer prNumber, String comment, String token) {
        try {
            String url = String.format("%s/repos/%s/pulls/%d/comments",
                GITEE_API_BASE,
                projectId,
                prNumber);

            JSONObject body = new JSONObject();
            body.put("body", comment);

            RequestBody requestBody = RequestBody.create(
                body.toJSONString(),
                MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "token " + token)
                    .header("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    log.info("创建PR评论成功，PR: {}", prNumber);
                    return true;
                } else {
                    log.error("创建PR评论失败，状态码: {}, 响应: {}",
                        response.code(), response.body().string());
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("创建PR评论异常", e);
            return false;
        }
    }

    /**
     * 获取PR信息
     *
     * @param projectId Gitee项目ID
     * @param prNumber PR编号
     * @param token 访问令牌
     * @return PR信息
     */
    public JSONObject getPullRequestInfo(Long projectId, Integer prNumber, String token) {
        try {
            String url = String.format("%s/repos/%s/pulls/%d",
                GITEE_API_BASE,
                projectId,
                prNumber);

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "token " + token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("获取PR信息失败，状态码: {}", response.code());
                    return null;
                }

                String responseBody = response.body().string();
                return JSON.parseObject(responseBody);
            }
        } catch (Exception e) {
            log.error("获取PR信息异常", e);
            return null;
        }
    }

    /**
     * 获取项目信息
     *
     * @param projectId Gitee项目ID
     * @param token 访问令牌
     * @return 项目信息
     */
    public JSONObject getProjectInfo(Long projectId, String token) {
        try {
            String url = String.format("%s/repos/%s",
                GITEE_API_BASE,
                projectId);

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "token " + token)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("获取项目信息失败，状态码: {}", response.code());
                    return null;
                }

                String responseBody = response.body().string();
                return JSON.parseObject(responseBody);
            }
        } catch (Exception e) {
            log.error("获取项目信息异常", e);
            return null;
        }
    }
}