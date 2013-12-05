package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class BattleshipFrameMaker {
    private BattleshipFrameMaker() {}
    
    public static JFrame createFrame() {
        JFrame battleshipFrame = new JFrame("Battleship");
        battleshipFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        battleshipFrame.setSize(400, 400);
        battleshipFrame.setResizable(false);
        
        GridBagLayout layout = new GridBagLayout();
        battleshipFrame.setLayout(layout);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        
        constraints.fill = GridBagConstraints.BOTH;
        
        JButton b1 = new JButton("b1");
        JButton b2 = new JButton("b2");

        constraints.gridwidth = 1;
        layout.setConstraints(b1, constraints);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(b2, constraints);
        
        battleshipFrame.add(b1);
        battleshipFrame.add(b2);
        
        constraints.weighty = 1;
        constraints.gridheight = 10;
        constraints.gridwidth = 10;
        JPanel battlefield = createBattlefield(false);
        layout.setConstraints(battlefield, constraints);
        battleshipFrame.add(battlefield);
        
        
        return battleshipFrame;
    }
    
    private static JPanel createBattlefield(boolean enabled) {
        JPanel battlefield = new JPanel(new GridLayout(10,10));
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton newButton = new JButton(i + ", " + j);
                newButton.setEnabled(enabled);
                battlefield.add(newButton);
            }
        }
        
        return battlefield;
    }
}
