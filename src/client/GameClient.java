package client;

import javax.swing.SwingUtilities;
import settings.SettingsHandler;
import settings.XMLSettingsHandler;

public class GameClient {
    public static void main(String[ ] args) {
        final SettingsHandler settings = new XMLSettingsHandler("settings.xml");
        final SettingsHandler translation = new XMLSettingsHandler("translation.xml");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientFrame(settings, translation);
            }
        });
    }
}
