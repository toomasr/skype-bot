package org.zeroturnaround.commands;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This parses Jenkins rss for LR-dev view and can say which projects are not green now
 * 
 * to use write 'lrdev' to a conversation with the bot.
 * 
 * @author shelajev
 */
public class LrDevTests extends DefaultHandler implements Command {

  private static final Logger log = LoggerFactory.getLogger(LrDevTests.class);

  public static final String COMMAND = "lrdev";
  private static final String RSS_LATEST_URL = "http://kaban/jenkins/view/LR%20Dev/rssLatest";

  private static final SAXParserFactory spf = SAXParserFactory.newInstance();

  @Override
  public String execute(String cmdStr) {
    if (!COMMAND.equalsIgnoreCase(cmdStr)) {
      return null;
    }
    final List<String> failed = new ArrayList<String>();
    try {
      SAXParser sp = spf.newSAXParser();
      sp.parse(RSS_LATEST_URL, new DefaultHandler() {

        private boolean titleElement;
        private boolean entryElement;

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          if (qName.equalsIgnoreCase("title")) {
            titleElement = true;
          }
          if (qName.equalsIgnoreCase("entry")) {
            entryElement = true;
          }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
          if (!(titleElement && entryElement))
            return;
          String title = new String(ch, start, length);

          if (!(title.endsWith("(stable)") || title.endsWith("(back to normal)"))) {
            log.debug("failed = " + title);
            failed.add(title.substring(0, title.indexOf("#") - 1));
          }
          titleElement = false;
          entryElement = false;
        }
      });
    }
    catch (Exception e) {
      log.error("Got an exception at fetching Jenkins 'LR dev': " + e, e);
      return "Got an exception at fetching Jenkins 'LR dev': " + e;
    }
    if (failed.isEmpty()) {
      return "Everything is fine, my master! :)";
    }
    
    StringBuilder sb = new StringBuilder("Following projects are not green:\n");
    for (String fail : failed) {
      sb.append(fail + "\n");
    }
    
    return sb.toString();
  }

}
