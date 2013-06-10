package org.zeroturnaround.skypebot.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.Configuration;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;

public class DropboxDirPluginFileRepository extends LocalDirPluginFileRepository {
  private static final Logger log = LoggerFactory.getLogger(DropboxDirPluginFileRepository.class);
  private static DropboxAPI<WebAuthSession> api;

  public DropboxDirPluginFileRepository(String dirname) {
    super(dirname);
    initDropboxApi();
  }

  private void initDropboxApi() {
    AppKeyPair appKeys = new AppKeyPair(Configuration.getProperty("dropbox.appkey"), Configuration.getProperty("dropbox.appsecret"));
    WebAuthSession session = new WebAuthSession(appKeys, AccessType.DROPBOX,
        new AccessTokenPair(Configuration.getProperty("dropbox.token.key"), Configuration.getProperty("dropbox.token.secret")));
    api = new DropboxAPI<WebAuthSession>(session);
  }

  @Override
  public Collection<File> listPluginFiles() {
    boolean prepared = prepareLocalDir();
    if (!prepared) {
      return Collections.emptyList();
    }
    return super.listPluginFiles();
  }

  public boolean prepareLocalDir() {
    try {
      log.info("Cleaning plugin directory {}", dir);
      FileUtils.cleanDirectory(dir);
      downloadPluginFiles();
      return true;
    }
    catch (IOException e) {
      log.error("Cannot deal with directory {}", dir.getAbsolutePath(), e);
    }
    catch (DropboxException e) {
      log.error("Cannot dropbox!", e);
    }
    return false;
  }


  private void downloadPluginFiles() throws DropboxException, IOException {
    String rootPath = "/" + Configuration.getProperty("dropbox.plugin.dir");
    Entry metadata = api.metadata(rootPath, 0, null, true, false, null);
    log.info("Got a response from dropbox, list has {} entries", metadata.contents.size());
    for (Entry entry : metadata.contents) {
      if (!entry.fileName().endsWith(".jar")) {
        continue;
      }
      String filepath = rootPath + "/" + entry.fileName();
      log.info("Downloading file {} from dropbox", filepath);
      DropboxInputStream fileStream = api.getFileStream(filepath, null);
      FileUtils.copyInputStreamToFile(fileStream, new File(dir, entry.fileName()));
    }
  }
}
