package client;

import client.battleship.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

public class ClientFrame extends JFrame {
    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            battleshipFrame = null;
        }
    }

    private BattleshipFrame battleshipFrame;
    private final BattleshipWindowListener battleshipWindowListener = new BattleshipWindowListener();
    
    public ClientFrame() {
        super();
        configureFrame();
        
        JList<String> list = new JList<>(new String[] {"asd", "asd", "asdasd qw"});
        add(list);
        
        /*JButton b = new JButton("open bs");
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                battleshipFrame = new BattleshipFrame();
                battleshipFrame.addWindowListener(battleshipWindowListener);
                setVisible(false);
            }
        });
        add(b);*/
        
        setVisible(true);
    }
    
    private void configureFrame() {
        int screenHeight;
        int screenWidth;
        int frameHeihgt;
        int frameWidth;
        {
            Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
            screenHeight = screenDim.height;
            screenWidth = screenDim.width;
        }
        
        frameHeihgt = screenHeight/2;
        frameWidth = frameHeihgt*2/3;
        
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameWidth, frameHeihgt);
        setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeihgt/2);
        setResizable(false);
    }
}
