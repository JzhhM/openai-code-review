package com.jz.sdk.infrastructure.openao.dto.openai;

import com.jz.sdk.infrastructure.openao.dto.openai.dto.ChatCompletionRequestDTO;
import com.jz.sdk.infrastructure.openao.dto.openai.dto.ChatCompletionSyncResponseDTO;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-19 10:43
 */
public interface IOpenAI {
  ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO requestDTO) throws Exception;
}
