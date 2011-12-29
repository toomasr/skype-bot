package org.zeroturnaround.commands;

import com.google.gag.annotation.disclaimer.HandsOff;
import com.google.gag.enumeration.Consequence;

@HandsOff(byOrderOf = "Toomas Römer", onPainOf = Consequence.SILENT_TREATMENT)
public class CoolTextCommand implements Command {

  @Override
  public String execute(String cmdStr) {
    if (cmdStr.toLowerCase().equals("tom"))
      return "Toomas is cool!";
    else if (cmdStr.toLowerCase().equals("kevin"))
      return "Kevin is cool!";
    else
      return cmdStr + " is not as cool";
  }

}
