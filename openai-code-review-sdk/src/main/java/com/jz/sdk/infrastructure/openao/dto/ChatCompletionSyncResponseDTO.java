package com.jz.sdk.infrastructure.openao.dto;

import java.util.List;

public class ChatCompletionSyncResponseDTO {

  private List<Choice> choices;

  public static class Choice {
    private Message message;

    public Message getMessage() {
      return message;
    }

    public void setMessage(Message message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "Choice{" +
          "message=" + message +
          '}';
    }
  }

  public static class Message {
    private String role;
    private String content;

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

    @Override
    public String toString() {
      return "Message{" +
          "role='" + role + '\'' +
          ", content='" + content + '\'' +
          '}';
    }
  }

  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  @Override
  public String toString() {
    return "ChatCompletionSyncResponseDTO{" +
        "choices=" + choices +
        '}';
  }
}
