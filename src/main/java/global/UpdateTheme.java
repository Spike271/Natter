package global;

import java.awt.*;

import static com.client.Application.currentTheme;
import static com.client.Application.natter;

public class UpdateTheme
{
    public static void update()
    {
        EventQueue.invokeLater(() -> {
            raven.chat.swing.TextField.switchTheme(currentTheme);
            raven.chat.component.ChatBox.switchTheme(currentTheme);
            raven.chat.swing.Background.switchTheme(currentTheme);
            natter.updateTheme();
            raven.chat.swing.scroll.ModernScrollBarUI.switchTheme(currentTheme);
        });
    }
}
