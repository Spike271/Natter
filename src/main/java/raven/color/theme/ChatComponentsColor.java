package raven.color.theme;

import java.awt.Color;

import raven.resource.swing.GetAndSetColor;

public interface ChatComponentsColor
{
	String chatBackgroundImage = GetAndSetColor.getSettings(Theme.mode, "chatBackgroundImage").orElse("");
	
	Color userNameBarColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "userNameBarColor")).orElse("#F2F2F2"));
	Color userNameColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "userNameColor")).orElse("#121212"));
	Color chatInputBoxColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "chatInputBoxColor")).orElse("#F1F1F1"));
	Color chatInputBoxTextColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "chatInputBoxTextColor")).orElse("#121212"));
	Color messageTextColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "messageTextColor")).orElse("#FFFFFFF"));
	Color dateColor = Color.decode((GetAndSetColor.getSettings(Theme.mode, "dateColor")).orElse("#121212"));
	Color placeholderText = Color.decode((GetAndSetColor.getSettings(Theme.mode, "placeholderText")).orElse("#000000"));
	Color leftChatBubbleFrom = Color.decode((GetAndSetColor.getSettings(Theme.mode, "leftChatBubbleFrom")).orElse("#00B4DB"));
	Color leftChatBubbleTo = Color.decode((GetAndSetColor.getSettings(Theme.mode, "leftChatBubbleTo")).orElse("#0083B0"));
	Color rightChatBubbleFrom = Color.decode((GetAndSetColor.getSettings(Theme.mode, "rightChatBubbleFrom")).orElse("#8E2DE2"));
	Color rightChatBubbleTo = Color.decode((GetAndSetColor.getSettings(Theme.mode, "rightChatBubbleTo")).orElse("#4A00E0"));
	Color Color1 = Color.decode((GetAndSetColor.getSettings(Theme.mode, "Color1")).orElse("#74B4E0"));
	Color Color2 = Color.decode((GetAndSetColor.getSettings(Theme.mode, "Color2")).orElse("#74B4E0"));
}