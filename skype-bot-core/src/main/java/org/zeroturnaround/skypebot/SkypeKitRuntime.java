package org.zeroturnaround.skypebot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkypeKitRuntime {
  private static final Logger log = LoggerFactory.getLogger(SkypeKitRuntime.class);
  private static final String runtimeFileName = "linux-x86-skypekit";
  private Executor executor;
  private File file;

  private SkypeKitRuntime() {
    executor = new DefaultExecutor();
    executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
  }

  public void start() {
    new Thread(new Runnable() {
      public void run() {
        try {
          log.info("Starting runtime from file {}", file);
          executor.execute(new CommandLine("./" + runtimeFileName));
        }
        catch (Exception e) {
          log.info("Cannot run {}", runtimeFileName);
        }
      }
    }).start();
  }

  public static SkypeKitRuntime init() {
    SkypeKitRuntime runtime = new SkypeKitRuntime();
    try {
      runtime.prepare();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    return runtime;
  }

  private void prepare() throws IOException {
    this.file = new File(runtimeFileName);
    if (file.exists()) {
      log.info("Runtime file {} already exists, I'm deleting it", file);
      FileUtils.forceDelete(file);
    }
    InputStream is = this.getClass().getResourceAsStream("/" + runtimeFileName);
    if(is == null) {
      log.info("Cannot find runtime resource. It was not included here.");
      throw new UnsupportedOperationException("Runtime is not included, cannot start it");
    }
    FileUtils.copyInputStreamToFile(is, file);
    boolean executable = file.setExecutable(true, true);
    log.info("Extracted an executable runtime file {}, executable is {}", file, executable);
  }

}
