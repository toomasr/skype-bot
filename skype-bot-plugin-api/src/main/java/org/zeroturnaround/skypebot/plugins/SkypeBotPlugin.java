package org.zeroturnaround.skypebot.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SkypeBot finds jar files through some mechanism. It scans classes in them and finds classes in:
 * 'org.zeroturnaround.skypebot' and subpackages
 * takes classes marked with:
 * SkypeBotPlugin. If the type implements BotPlugin, it takes commands via getCommands.
 * 
 * Then it registers those commands in the CommandFactory.
 * 
 * Reactive commands react on a message: they can respond to the same conversation.
 * Cron commnads run repetitively.
 *
 * At any moment of time bot can drop all command definitions and load plugins again.
 *
 * @author shelajev
 *
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SkypeBotPlugin {

}
