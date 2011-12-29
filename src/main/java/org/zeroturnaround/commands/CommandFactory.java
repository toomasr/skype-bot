package org.zeroturnaround.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
  
  private static Map<String, Command> cmds = new HashMap<String, Command>();
  private static final Command NOP = new NopCommand();
  
  static {
    cmds.put("tom", new CoolTextCommand());
    cmds.put(LrDevTests.COMMAND, new LrDevTests());
    cmds.put(FunTrivia.COMMAND, new FunTrivia());
    cmds.put("time", new TimeCommand());
  }

  public static Command getCommand(String command) {
    command = command.toLowerCase();
    if(cmds.containsKey(command)) {
      return cmds.get(command);
    }
    
    return NOP;
  }

}
