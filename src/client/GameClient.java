package client;

import client.battleship.BattleshipFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameClient {
    public static void main(String[ ] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f = new BattleshipFrame();
                f.setVisible(true);
            }
        });
    }
}
