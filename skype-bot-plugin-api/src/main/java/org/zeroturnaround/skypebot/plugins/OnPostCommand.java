package org.zeroturnaround.skypebot.plugins;

import java.util.Map;

public interface OnPostCommand extends Command {
  Map<String, String[]> handle(Map<String, String[]> parameters, String requestBody);
}
