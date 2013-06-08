package org.zeroturnaround.skypebot.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDirPluginFileRepository implements PluginFileRepository {
  private static final Logger log = LoggerFactory.getLogger(LocalDirPluginFileRepository.class);

  protected File dir;

  public LocalDirPluginFileRepository(String dirname) {
    this.dir = new File(dirname);
  }

  @Override
  public Collection<File> listPluginFiles() {
    try {
      File[] files = dir.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
          log.info("Checking file " + name);
          return name.endsWith(".jar");
        }
      });
      if (files == null) {
        return Collections.emptyList();
      }
      return Arrays.asList(files);
    }
    catch (Exception e) {
      log.error("Cannot list plugin files from {}", dir.getAbsolutePath(), e);
      return Collections.emptyList();
    }
  }
}
