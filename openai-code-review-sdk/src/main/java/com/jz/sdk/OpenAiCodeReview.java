package com.jz.sdk;

import com.alibaba.fastjson2.JSON;
import com.jz.sdk.infrastructure.openao.dto.ChatCompletionRequestDTO;
import com.jz.sdk.infrastructure.openao.dto.ChatCompletionSyncResponseDTO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 代码评审工具
 */
public class OpenAiCodeReview {

  public static void main(String[] args) throws Exception {
    System.out.println("测试执行");

    // 1. 代码检出
    ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
    processBuilder.directory(new File("."));

    Process process = processBuilder.start();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future<String> stdoutFuture = executor.submit(() -> readStream(process.getInputStream()));
    Future<String> stderrFuture = executor.submit(() -> readStream(process.getErrorStream()));

    int exitCode = process.waitFor();
    executor.shutdown();

    if (exitCode != 0) {
      System.out.println("Git diff 失败: " + stderrFuture.get());
      return;
    }

    String diffCode = stdoutFuture.get();
    System.out.println("diff code:\n" + diffCode);

    // 2. DeepSeek 代码评审
    String log = codeReview(diffCode);
    System.out.println("code review:\n" + log);
  }

  /**
   * 代码评审请求
   */
  private static String codeReview(String diff) throws Exception {
    URL url = new URL("https://api.deepseek.com/chat/completions");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

    // 使用环境变量读取 API Key
    String apiKey = "sk-0f0e490708134fb487caa409e0f48659";
    if (apiKey == null || apiKey.isEmpty()) {
      throw new RuntimeException("API Key is missing");
    }
    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
    connection.setDoOutput(true);


    ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
    chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>() {
      private static final long serialVersionUID = -7988151926241837899L;

      {
        add(new ChatCompletionRequestDTO.Prompt("user", "你是一位资深编程专家，拥有深厚的编程基础和广泛的技术栈知识。你的专长在于识别代码中的低效模式、安全隐患、以及可维护性问题，并能提出针对性的优化策略。你擅长以易于理解的方式解释复杂的概念，确保即使是初学者也能跟随你的指导进行有效改进。在提供优化建议时，你注重平衡性能、可读性、安全性、逻辑错误、异常处理、边界条件，以及可维护性方面的考量，同时尊重原始代码的设计意图。\n" +
            "你总是以鼓励和建设性的方式提出反馈，致力于提升团队的整体编程水平，详尽指导编程实践，雕琢每一行代码至臻完善。用户会将仓库代码分支修改代码给你，以git diff 字符串的形式提供，你需要根据变化的代码，帮忙review本段代码。然后你review内容的返回内容必须严格遵守下面我给你的格式，包括标题内容。\n" +
            "模板中的变量内容解释：\n" +
            "变量1是给review打分，分数区间为0~100分。\n" +
            "变量2 是code review发现的问题点，包括：可能的性能瓶颈、逻辑缺陷、潜在问题、安全风险、命名规范、注释、以及代码结构、异常情况、边界条件、资源的分配与释放等等\n" +
            "变量3是具体的优化修改建议。\n" +
            "变量4是你给出的修改后的代码。 \n" +
            "变量5是代码中的优点。\n" +
            "变量6是代码的逻辑和目的，识别其在特定上下文中的作用和限制\n" +
            "\n" +
            "必须要求：\n" +
            "1. 以精炼的语言、严厉的语气指出存在的问题。\n" +
            "2. 你的反馈内容必须使用严谨的markdown格式\n" +
            "3. 不要携带变量内容解释信息。\n" +
            "4. 有清晰的标题结构\n" +
            "返回格式严格如下：\n" +
            "# OpenAi 代码评审.\n" +
            "### \uD83D\uDE00代码评分：{变量1}\n" +
            "#### \uD83D\uDE00代码逻辑与目的：\n" +
            "{变量6}\n" +
            "#### ✅代码优点：\n" +
            "{变量5}\n" +
            "#### \uD83E\uDD14问题点：\n" +
            "{变量2}\n" +
            "#### \uD83C\uDFAF修改建议：\n" +
            "{变量3}\n" +
            "#### \uD83D\uDCBB修改后的代码：\n" +
            "{变量4}\n" +
            "`;代码如下:\n" + diff));
//        add(new ChatCompletionRequestDTO.Prompt("user", diff));
      }
    });


    try (OutputStream os = connection.getOutputStream()) {
      byte[] input = JSON.toJSONString(chatCompletionRequest).getBytes(StandardCharsets.UTF_8);
      os.write(input);
    }

    int responseCode = connection.getResponseCode();
    System.out.println("HTTP Response Code: " + responseCode);

    BufferedReader reader;
    if (responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
    }

    StringBuilder content = new StringBuilder();
    String inputLine;
    while ((inputLine = reader.readLine()) != null) {
      content.append(inputLine);
    }

    reader.close();
    connection.disconnect();
    System.out.println(content);

    // 解析 JSON 响应
    ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
    return response.toString();
  }

  /**
   * 读取输入流，防止阻塞
   */
  private static String readStream(InputStream inputStream) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder result = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      result.append(line).append("\n");
    }
    return result.toString();
  }
}
