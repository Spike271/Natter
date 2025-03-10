package raven.color.theme;

import java.awt.Color;

import raven.resource.swing.GetAndSetColor;

public interface ChatComponentsColor
{
	String chatbackgroundImage = GetAndSetColor.getSettings(Theme.mode, "chatbackgroundImage");
	
	Color userNameBarColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "userNameBarColor"));
	Color userNameColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "userNameColor"));
	Color chatInputBoxColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "chatInputBoxColor"));
	Color chatInputBoxTextColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "chatInputBoxTextColor"));
	Color messageTextColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "messageTextColor"));
	Color dateColor = Color.decode(GetAndSetColor.getSettings(Theme.mode, "dateColor"));
	Color placeholderText = Color.decode(GetAndSetColor.getSettings(Theme.mode, "placeholderText"));
	Color leftChatBubbleFrom = Color.decode(GetAndSetColor.getSettings(Theme.mode, "leftChatBubbleFrom"));
	Color leftChatBubbleTo = Color.decode(GetAndSetColor.getSettings(Theme.mode, "leftChatBubbleTo"));
	Color rightChatBubbleFrom = Color.decode(GetAndSetColor.getSettings(Theme.mode, "rightChatBubbleFrom"));
	Color rightChatBubbleTo = Color.decode(GetAndSetColor.getSettings(Theme.mode, "rightChatBubbleTo"));
	Color Color1 = Color.decode(GetAndSetColor.getSettings(Theme.mode, "Color1"));
	Color Color2 = Color.decode(GetAndSetColor.getSettings(Theme.mode, "Color2"));
}