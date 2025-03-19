package com.jz.sdk.domain.service;

import com.jz.sdk.infrastructure.openao.dto.git.GitCommand;
import com.jz.sdk.infrastructure.openao.dto.openai.IOpenAI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-19 10:38
 */
public abstract class AbstractOpenAiCodeReviewService implements IOpenAiCodeReviewService {

  private final Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewService.class);

  protected final GitCommand gitCommand;
  protected final IOpenAI openAI;

  public AbstractOpenAiCodeReviewService(GitCommand gitCommand, IOpenAI openAI) {
    this.gitCommand = gitCommand;
    this.openAI = openAI;
  }

  @Override
  public void exec() {
    try {
      // 1. 获取提交代码
      String diffCode = getDiffCode();
      // 2. 开始评审代码
      String recommend = codeReview(diffCode);
      logger.info(recommend);
      // todo 保存评审记录、发送评审结果
    } catch (Exception e) {
      logger.error("openai-code-review error", e);
    }
  }

  protected abstract String getDiffCode() throws IOException, InterruptedException;

  protected abstract String codeReview(String diffCode) throws Exception;

}
