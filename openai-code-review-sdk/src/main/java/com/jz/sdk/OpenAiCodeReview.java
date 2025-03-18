package com.jz.sdk;

import com.alibaba.fastjson2.JSON;
import com.jz.sdk.infrastructure.openao.dto.ChatCompletionSyncResponseDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-12 18:09
 */
public class OpenAiCodeReview {


  public static void main(String[] args) throws Exception {


    System.out.println("测试执行");

    // 1. 代码检出
    ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
    processBuilder.directory(new File("."));

    Process process = processBuilder.start();

    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;

    StringBuilder diffCode = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      diffCode.append(line);
    }

    int exitCode = process.waitFor();
    System.out.println("Exited with code:" + exitCode);

    System.out.println("diff code：" + diffCode.toString());

    // 2. deepseek 代码评审
    String log = codeReview(String.valueOf(diffCode));
    System.out.println("code review:" + log);
  }


  private static String codeReview(String diff) throws Exception{
    URL url = new URL("https://api.deepseek.com/chat/completions");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Authorization", "Bearer sk-0f0e490708134fb487caa409e0f48659");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
    connection.setDoOutput(true);

    String jsonInpuString = "{"
        + "\"model\":\"deepseek-reasoner\","
        + "\"messages\": ["
        + "    {"
        + "        \"role\": \"user\","
        + "        \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + diff + "\""
        + "    }"
        + "]"
        + "}";


    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = jsonInpuString.getBytes(StandardCharsets.UTF_8);
      os.write(input);
    }

    int responseCode = connection.getResponseCode();
    System.out.println(responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;

    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    in.close();
    connection.disconnect();
    ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    return response.toString();
  }



}
