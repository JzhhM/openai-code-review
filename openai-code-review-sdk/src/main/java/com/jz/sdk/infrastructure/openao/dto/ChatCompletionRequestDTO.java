package com.jz.sdk.infrastructure.openao.dto;

import java.util.List;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-18 14:57
 */
public class ChatCompletionRequestDTO {

  private String model = "deepseek-chat";
  private List<Prompt> messages;
  private Boolean stream = false;

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

  public Boolean getStream(){
    return stream;
  }

  private void setStream(Boolean stream){
    this.stream = stream;
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
