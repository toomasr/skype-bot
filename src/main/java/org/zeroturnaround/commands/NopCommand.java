package org.zeroturnaround.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NopCommand implements Command {
  private static final Logger log = LoggerFactory.getLogger(NopCommand.class);

  @Override
  public String execute(String cmdStr) {
    log.debug("NopCommand executed");
    return null;
  }

}
