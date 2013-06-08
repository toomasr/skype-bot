package org.zeroturnaround.skypebot.plugins;

import java.io.File;
import java.util.Collection;

/**
 * This class is responsible for providing plugin files to scan
 * Probably it can scan some local dir for file or maybe dropbox dir, download files and return them
 *
 * @author shelajev
 *
 */
public interface PluginFileRepository {
  Collection<File> listPluginFiles();
}
