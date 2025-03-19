package com.jz.sdk.infrastructure.openao.dto.openai.impl;

import com.alibaba.fastjson2.JSON;
import com.jz.sdk.infrastructure.openao.dto.openai.IOpenAI;
import com.jz.sdk.infrastructure.openao.dto.openai.dto.ChatCompletionRequestDTO;
import com.jz.sdk.infrastructure.openao.dto.openai.dto.ChatCompletionSyncResponseDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-19 10:44
 */
public class DeepSeek implements IOpenAI {

  private final String apiHost;
  private final String apiKey;

  public DeepSeek(String apiHost, String apiKey) {
    this.apiHost = apiHost;
    this.apiKey = apiKey;
  }


  @Override
  public ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception {
    URL url = new URL(apiHost);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = JSON.toJSONString(requestDTO).getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }

    in.close();
    connection.disconnect();

    return JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
  }
}
