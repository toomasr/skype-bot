package org.zeroturnaround.skypebot.plugins;

/**
 * Command that reacts to a message by it's classname
 *
 * @author shelajev
 *
 */
public abstract class NameCommand implements ReactiveCommand {

  @Override
  public String getName() {
    return this.getClass().getSimpleName().toLowerCase();
  }

  @Override
  public final String react(String conversationName, String author, String message) {
    if (!getName().equalsIgnoreCase(message)) {
      return null;
    }
    return reactInternal(conversationName, author, message);
  }

  protected abstract String reactInternal(String conversationName, String author, String message);

  @Override
  public String toString() {
    return "NameCommand " + this.getClass().getSimpleName();
  }
}
