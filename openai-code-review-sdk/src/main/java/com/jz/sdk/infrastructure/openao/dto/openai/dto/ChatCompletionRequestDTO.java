package com.jz.sdk.infrastructure.openao.dto.openai.dto;

import com.jz.sdk.domain.model.Model;

import java.util.List;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-18 14:57
 */
public class ChatCompletionRequestDTO {

  private String model = Model.DEEP_SEEK_REASONER.getCode();
  private List<Prompt> messages;
  private Integer maxTokens = 8192;

  public Integer getMaxTokens() {
    return maxTokens;
  }

  public void setMaxTokens(Integer maxTokens) {
    this.maxTokens = maxTokens;
  }

  public static class Prompt {
    private String role;
    private String content;

    public Prompt() {
    }

    public Prompt(String role, String content) {
      this.role = role;
      this.content = content;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<Prompt> getMessages() {
    return messages;
  }

  public void setMessages(List<Prompt> messages) {
    this.messages = messages;
  }

}
