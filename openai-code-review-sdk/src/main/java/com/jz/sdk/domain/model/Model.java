package com.jz.sdk.domain.model;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-19 10:33
 */
public enum Model {
  DEEP_SEEK_CHAT("deepseek-chat", "DeepSeek-V3"),
  DEEP_SEEK_REASONER("deepseek-reasoner", "DeepSeek-R1")
  ;

  private final String code;
  private final String info;

  Model(String code, String info) {
    this.code = code;
    this.info = info;
  }

  public String getCode() {
    return code;
  }

  public String getInfo() {
    return info;
  }

}
