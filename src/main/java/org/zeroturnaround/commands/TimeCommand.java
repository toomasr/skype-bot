package org.zeroturnaround.commands;

import java.text.DateFormat;
import java.util.Date;

public class TimeCommand implements Command {

  @Override
  public String execute(String cmdStr) {
    DateFormat format = DateFormat.getDateTimeInstance();
    return "Current time: " + format.format(new Date());
  }

}
