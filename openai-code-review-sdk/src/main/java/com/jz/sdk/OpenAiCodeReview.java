package com.jz.sdk;

import com.jz.sdk.domain.service.impl.OpenAiCodeReviewService;
import com.jz.sdk.infrastructure.openao.dto.git.GitCommand;
import com.jz.sdk.infrastructure.openao.dto.openai.IOpenAI;
import com.jz.sdk.infrastructure.openao.dto.openai.impl.DeepSeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代码评审工具
 */
public class OpenAiCodeReview {

  private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

  public static void main(String[] args) throws Exception {
    GitCommand gitCommand = new GitCommand();

    IOpenAI openAI = new DeepSeek("https://api.deepseek.com/chat/completions", "sk-0f0e490708134fb487caa409e0f48659");

    OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, openAI);
    openAiCodeReviewService.exec();
    logger.info("openai-code-review done!");
  }

  private static String getEnv(String key) {
    String value = System.getenv(key);
    if (null == value || value.isEmpty()) {
      throw new RuntimeException("value is null");
    }
    return value;
  }
}
