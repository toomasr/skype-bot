package org.zeroturnaround.commands;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * "fun-trivia" command that gives a beginning of a random wikipedia article back.
 * 
 * @author shelajev
 *
 */
public class FunTrivia implements Command {

  public static final String COMMAND = "fun-trivia";
  
  private static final Logger log = LoggerFactory.getLogger(FunTrivia.class);
  
  private static final String BAD_DAY = "Today is a sad day, no fun trivia for you, my dear friend";
  private static final int NUMBER_OF_SENTENCES = 2;

  private static final String RANDOMS = "http://en.wikipedia.org/w/api.php?action=query&list=random&rnlimit=10&format=xml";
  private static final String PAGE = "http://en.wikipedia.org/w/api.php?action=parse&pageid=18097407&format=xml&prop=categories|text&pageid=%s";

  private static final SAXParserFactory spf = SAXParserFactory.newInstance();

  @Override
  public String execute(String cmdStr) {
    if (!COMMAND.equalsIgnoreCase(cmdStr)) {
      return null;
    }
    String info = null;
    for (int i = 0; i < 7; i++) {
      info = getRandomWikipediaInfo();
      if (!isLame(info)) {
        return info;
      }
    }
    return BAD_DAY;

  }

  private String getRandomWikipediaInfo() {
    try {
      SAXParser sp = spf.newSAXParser();
      RandomsHandler rh = new RandomsHandler();
      String id = null;
      do {
        sp.parse(RANDOMS, rh);
        id = rh.id;
      }
      while (id == null);
      String url = String.format(PAGE, id);
      PageHandler pageHandler = new PageHandler();
      sp.parse(url, pageHandler);

      String text = pageHandler.getText();
      return getFirstSentences(text, NUMBER_OF_SENTENCES);

    }
    catch (Exception e) {
      log.debug("Unalble to get fun trivia: " + e, e);
      return BAD_DAY;
    }
  }

  private String getFirstSentences(String text, int k) {
    if (text == null)
      return null;

    text = text.replaceAll("\\<.*?>", "");
    text = StringUtils.strip(text);

    int idx = 0;
    for (int i = 0; i < k; i++) {
      idx = text.indexOf(". ", idx + 1);
      if (idx == -1)
        return text;
    }
    text = text.substring(0, idx + 1);
    text = text.replaceAll("\\n+\\n*", "\n");
    return text;
  }

  private boolean isLame(String info) {
    if (info.contains("his article"))
      return true;

    if (info.contains("citations"))
      return true;

    if (info.contains("For the acronym for"))
      return true;

    if (info.contains("Genre"))
      return true;

    if (info.contains("For other"))
      return true;

    if (info.contains("This is a List"))
      return true;

    if (info.contains("[edit]"))
      return true;

    if (info.contains("<!--"))
      return true;

    if (info.contains("require cleanup"))
      return true;

    if (StringUtils.split(info, "\n").length > 3) {
      return true;
    }

    return false;
  }

  private static class RandomsHandler extends DefaultHandler {
    private String id;
    private boolean found;

    private static Set<String> bad = new HashSet<String>();
    static {
      bad.add("Category:");
      bad.add("Category talk:");
      bad.add("Conference:");
      bad.add("File:");
      bad.add("File talk:");
      bad.add("Portal talk:");
      bad.add("Talk:");
      bad.add("Template:");
      bad.add("User:");
      bad.add("User talk:");
      bad.add("Wikipedia:");
      bad.add("Wikipedia talk:");

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (found)
        return;

      if (qName.equalsIgnoreCase("page")) {
        String title = attributes.getValue("title");
        for (String s : bad) {
          if (title.startsWith(s))
            return;
        }
        found = true;
        id = attributes.getValue("id");
      }
    }
  }

  private static class PageHandler extends DefaultHandler {
    private StringBuilder text = new StringBuilder();
    private boolean textFound;
    private boolean textDone;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (qName.equalsIgnoreCase("text")) {
        textFound = true;
      }
    }

    public String getText() {
      return text.toString();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equalsIgnoreCase("text")) {
        textDone = true;
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (!textFound || textDone)
        return;
      text.append(ch, start, length);
    }
  }

}
