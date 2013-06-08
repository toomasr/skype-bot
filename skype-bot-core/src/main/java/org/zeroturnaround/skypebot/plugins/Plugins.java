package org.zeroturnaround.skypebot.plugins;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Plugins {
  private static final Logger log = LoggerFactory.getLogger(Plugins.class);

  private static final PluginFileRepository repo = new LocalDirPluginFileRepository("plugins");

  private static final Map<String, URLClassLoader> pluginClassloaders = new LinkedHashMap<String, URLClassLoader>();

  public static void reload() {
    closeClassLoaders();
    log.info("reloading plugins");
    Collection<File> pluginFiles = repo.listPluginFiles();
    log.info("Got some plugin files: " + pluginFiles);
    Commands.clear();
    PluginScanner scanner = new PluginScanner();
    for (File file : pluginFiles) {
      Collection<BotPlugin> plugins = scanner.scan(file);
      for (BotPlugin botPlugin : plugins) {
        Commands.add(botPlugin.getCommands());
      }
    }
  }

  private static void closeClassLoaders() {
    for (Map.Entry<String, URLClassLoader> me : pluginClassloaders.entrySet()) {
      try {
        me.getValue().close();
      }
      catch (IOException e) {
        log.error("Cannot close classloader for file: {}", me.getKey(), e);
      }
    }
    pluginClassloaders.clear();
  }

  public static void registerPluginClassloader(File file, URLClassLoader classloader) {
    pluginClassloaders.put(file.getAbsolutePath(), classloader);
  }
}
