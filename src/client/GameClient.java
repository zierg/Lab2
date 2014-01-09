package client;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import settings.SettingsHandler;
import settings.XMLSettingsHandler;

public class GameClient {
    public static void main(String[ ] args) {
        String sep = File.separator;
        String settingsPath = (new File(GameClient.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent())
                + sep + "settings.xml";
        String translationPath = (new File(GameClient.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent())
                + sep + "translation.xml";
        
        final SettingsHandler settings = new XMLSettingsHandler(settingsPath);
        final SettingsHandler translation = new XMLSettingsHandler(translationPath);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new ClientFrame(settings, translation);
            }
        });
    }
}
