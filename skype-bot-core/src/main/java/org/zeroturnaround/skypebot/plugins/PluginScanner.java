package org.zeroturnaround.skypebot.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.plugins.BotPlugin;
import org.zeroturnaround.skypebot.plugins.SkypeBotPlugin;

public class PluginScanner {
  private static final Logger log = LoggerFactory.getLogger(PluginScanner.class);

  public Collection<BotPlugin> scan(File file) {
    List<BotPlugin> plugins = new ArrayList<BotPlugin>();

    try (JarFile jar = new JarFile(file)) {
      URLClassLoader classloader = new URLClassLoader(new URL[] { file.toURI().toURL() });
      Plugins.registerPluginClassloader(file, classloader);

      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.isDirectory() || !entry.getName().startsWith("org/zeroturnaround/skypebot") || !entry.getName().endsWith(".class")) {
          continue;
        }
        // we loaded all the classes from this jar file. we can close the classloader
        Class<?> clazz = classloader.loadClass(entry.getName().replace('/', '.').replace(".class", ""));
        if (clazz.getAnnotation(SkypeBotPlugin.class) != null) {
          Object pluginInstance = clazz.newInstance();
          if (BotPlugin.class.isInstance(pluginInstance)) {
            log.info("Found a plugin: {}", clazz.getSimpleName());
            plugins.add(BotPlugin.class.cast(pluginInstance));
          }
        }
      }
      return plugins;
    }
    catch (Exception e) {
      log.error("Failed to scan file " + file + " for plugin", e);
      return Collections.emptyList();
    }
  }
}
