package com.jz.sdk.infrastructure.openao.dto.git;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author meizhihao@millionlab.cn
 * @date 2025-03-19 10:41
 */
public class GitCommand {

  private final Logger logger = LoggerFactory.getLogger(GitCommand.class);

  public String diff() throws IOException, InterruptedException {
    ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
    logProcessBuilder.directory(new File("."));
    Process logProcess = logProcessBuilder.start();

    BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
    String latestCommitHash = logReader.readLine();
    logReader.close();
    logProcess.waitFor();

    ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
    diffProcessBuilder.directory(new File("."));
    Process diffProcess = diffProcessBuilder.start();

    StringBuilder diffCode = new StringBuilder();
    BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()));
    String line;
    while ((line = diffReader.readLine()) != null) {
      diffCode.append(line).append("\n");
    }
    diffReader.close();

    int exitCode = diffProcess.waitFor();
    if (exitCode != 0) {
      throw new RuntimeException("Failed to get diff, exit code:" + exitCode);
    }

    return diffCode.toString();
  }

}
