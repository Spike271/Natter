package raven.chat.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public interface ChatEvent
{
	void mousePressedSendButton(ActionEvent evt);
	
	void mousePressedFileButton(ActionEvent evt);
	
	void keyTyped(KeyEvent evt);
	
	void keyPressed(KeyEvent evt);
}