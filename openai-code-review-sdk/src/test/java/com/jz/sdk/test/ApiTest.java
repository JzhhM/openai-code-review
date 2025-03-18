package com.jz.sdk.test;

import com.alibaba.fastjson2.JSON;
import com.jz.sdk.infrastructure.openao.dto.ChatCompletionSyncResponseDTO;
import org.junit.Test;

import java.io.BufferedReader;
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
public class ApiTest {

  public static void main(String[] args) {
    System.out.println("测试执行");
  }

  @Test
  public void test_http() throws IOException {

    URL url = new URL("https://api.deepseek.com/chat/completions");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Authorization", "Bearer sk-0f0e490708134fb487caa409e0f48659");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
    connection.setDoOutput(true);

    String code = "1+1";

    String jsonInpuString = "{"
        + "\"model\":\"deepseek-reasoner\","
        + "\"messages\": ["
        + "    {"
        + "        \"role\": \"user\","
        + "        \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + code + "\""
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
    System.out.println(content.toString());

    ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    System.out.println(response.getChoices().get(0).getMessage().getContent());

  }


}
