package com.zeroturnaround.commands;

public class CoolTextCommand implements Command {

  @Override
  public String execute(String cmdStr) {
    if (cmdStr.toLowerCase().equals("tom"))
      return "Toomas is cool!";
    else
      return cmdStr+" is not as cool";
  }

}
