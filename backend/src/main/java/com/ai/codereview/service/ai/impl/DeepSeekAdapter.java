package com.ai.codereview.service.ai.impl;

import com.ai.codereview.entity.AIModelConfig;
import com.ai.codereview.service.ai.AIModelAdapter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek模型适配器
 *
 * @author AI Code Review
 * @since 1.0.0
 */
@Slf4j
@Component
public class DeepSeekAdapter implements AIModelAdapter {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    public String reviewCode(String diff, AIModelConfig modelConfig) {
        try {
            String prompt = buildPrompt(diff);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-coder");
            requestBody.put("messages", new JSONArray()
                .fluentAdd(new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", prompt);
                }}));
            requestBody.put("max_tokens", modelConfig.getMaxTokens());
            requestBody.put("temperature", modelConfig.getTemperature());
            requestBody.put("stream", false);

            RequestBody body = RequestBody.create(
                JSON.toJSONString(requestBody),
                MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(modelConfig.getApiEndpoint())
                    .header("Authorization", "Bearer " + modelConfig.getApiKey())
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseBody = response.body().string();
                JSONObject result = JSON.parseObject(responseBody);

                if (result.containsKey("choices") && result.getJSONArray("choices").size() > 0) {
                    JSONObject choice = result.getJSONArray("choices").getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    return message.getString("content");
                }

                throw new RuntimeException("DeepSeek API返回格式异常");
            }
        } catch (Exception e) {
            log.error("DeepSeek模型审核代码失败", e);
            throw new RuntimeException("DeepSeek模型审核失败: " + e.getMessage());
        }
    }

    @Override
    public String getModelCode() {
        return "deepseek";
    }

    @Override
    public boolean testConnection(AIModelConfig modelConfig) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-coder");
            requestBody.put("messages", new JSONArray()
                .fluentAdd(new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", "test");
                }}));
            requestBody.put("max_tokens", 10);
            requestBody.put("stream", false);

            RequestBody body = RequestBody.create(
                JSON.toJSONString(requestBody),
                MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(modelConfig.getApiEndpoint())
                    .header("Authorization", "Bearer " + modelConfig.getApiKey())
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            log.error("DeepSeek模型连接测试失败", e);
            return false;
        }
    }

    /**
     * 构建提示词
     */
    private String buildPrompt(String diff) {
        return "你是一个专业的代码审核专家，请审核以下代码变更，给出具体的问题和建议：\n\n" +
               "代码变更内容：\n" + diff + "\n\n" +
               "请从以下几个方面进行审核：\n" +
               "1. 代码质量和可读性\n" +
               "2. 潜在的bug和问题\n" +
               "3. 性能优化建议\n" +
               "4. 安全问题\n" +
               "5. 最佳实践建议\n\n" +
               "请以JSON格式返回审核结果，格式如下：\n" +
               "{\n" +
               "  \"summary\": \"总体评价\",\n" +
               "  \"issues\": [\n" +
               "    {\n" +
               "      \"file\": \"文件路径\",\n" +
               "      \"line\": \"行号\",\n" +
               "      \"type\": \"问题类型（quality/bug/performance/security）\",\n" +
               "      \"severity\": \"严重程度（low/medium/high/critical）\",\n" +
               "      \"description\": \"问题描述\",\n" +
               "      \"suggestion\": \"改进建议\"\n" +
               "    }\n" +
               "  ],\n" +
               "  \"suggestions\": \"总体建议\"\n" +
               "}";
    }
}