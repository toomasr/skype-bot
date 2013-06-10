import junit.framework.TestCase;

import org.zeroturnaround.skypebot.Configuration;

public class ConfigurationTests extends TestCase {
  public void testIsAdmin() {
    boolean isAdmin = Configuration.isAdmin("toomasr");
    assertFalse(isAdmin);
  }
}
