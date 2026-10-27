package de.arbeeco.xjc.mixin;

import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = ChatListener.class, priority = 500)
public class ChatListenerMixin {
  @ModifyVariable(
          method = "showMessageToPlayer",
          at = @At("HEAD"),
          argsOnly = true,
          ordinal = 0
  )
  private Component replaceWaypointMessage(Component message) {
    System.out.println("TEST");
    return replaceWaypointComponent(message);
  }

  @ModifyVariable(
          method = "handleDisguisedChatMessage",
          at = @At("HEAD"),
          argsOnly = true,
          ordinal = 0
  )
  private Component replaceDisguisedWaypointMessage(Component message) {
    return replaceWaypointComponent(message);
  }

  @ModifyVariable(
          method = "handleSystemMessage",
          at = @At("HEAD"),
          argsOnly = true,
          ordinal = 0
  )
  private Component replaceSystemWaypointMessage(Component message) {
    return replaceWaypointComponent(message);
  }

  private Component replaceWaypointComponent(Component message) {
    Matcher matcher = matchMessage(message);
    return matcher.find() ? replaceMessage(matcher) : message;
  }

  private static Matcher matchMessage(Component message) {
    Pattern pattern = Pattern.compile("\\[name:\"(?<name>.*)\", x:(?<x>-?[0-9]*), y:(?<y>-?[0-9]*), z:(?<z>-?[0-9]*), dim:.*:(?<dim>.*)]");
    return pattern.matcher(message.getString());
  }

  private static Component replaceMessage(Matcher matcher) {
    return Component.translatable("xjc.xaero.waypoint", matcher.group("name"), matcher.group("name").charAt(0), matcher.group("x"), matcher.group("y"), matcher.group("z"), Math.round(Math.random() * 16), matcher.group("dim"));
  }
}
