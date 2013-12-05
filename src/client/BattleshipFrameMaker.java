package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

class BattleshipFrameMaker {
    private static final int FIELDS_COUNT = 10;
    private static JButton[][] playerField;
    private static JButton[][] enemyField;
    
    private BattleshipFrameMaker() {}
    
    public static JFrame createFrame() {
        JFrame battleshipFrame = new JFrame("Battleship");
        battleshipFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        battleshipFrame.setSize(800, 400);
        battleshipFrame.setResizable(false);
        
        GridBagLayout layout = new GridBagLayout();
        battleshipFrame.setLayout(layout);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        
        constraints.fill = GridBagConstraints.BOTH;
        

        /*constraints.gridwidth = 1;
        layout.setConstraints(b1, constraints);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(b2, constraints);
        
        //battleshipFrame.add(b1);
        //battleshipFrame.add(b2);*/
        
        
        constraints.weighty = 1;
        constraints.gridheight = 5;
        constraints.gridwidth = 5;
        constraints.insets = new Insets(0, 0, 0, 5);
        
        JPanel battlefield = createBattlefield(true);
        layout.setConstraints(battlefield, constraints);
        battleshipFrame.add(battlefield);
        
        
        constraints.insets = new Insets(0, 5, 0, 0);
        JPanel battlefield2 = createBattlefield(false);
        layout.setConstraints(battlefield2, constraints);
        battleshipFrame.add(battlefield2);
        
        
        return battleshipFrame;
    }
    
    private static JPanel createBattlefield(boolean enabled) {
        JPanel battlefield = new JPanel(new GridLayout(10,10));
        
        
        ImageIcon icon = new ImageIcon("data" + File.separator + "1.png");
        
        Color color = enabled ? Color.white : new Color(0xF0F0F0);
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton newButton = new JButton();
                newButton.setBackground(color);
                newButton.setEnabled(enabled);
                battlefield.add(newButton);
            }
        }
        
        return battlefield;
    }
}
