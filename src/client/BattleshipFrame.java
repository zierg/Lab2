package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

class BattleshipFrame extends JFrame {
    private class PlayerFieldListener implements ActionListener {
        //private final Icon missIcon = new ImageIcon("data" + File.separator + "miss.png");
        

        @Override
        public void actionPerformed(ActionEvent ae) {
            JButton a = (JButton) ae.getSource();
            setTitle(a.getActionCommand());
            a.setBackground(Color.black);
            a.setEnabled(false);
        }
    }

    private static final int FIELDS_COUNT = 100;
    private static final int FIELD_WIDTH = 10;
    private static final Insets LEFT_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets RIGHT_INSETS = new Insets(0, 2, 0, 0);
    
    private final GridBagLayout layout;
    
    private JButton[] playerField = new JButton[FIELDS_COUNT];
    private JButton[] enemyField = new JButton[FIELDS_COUNT];
    
    private GridBagConstraints battlefieldConstraint;
    

    
    public BattleshipFrame() {
        layout = new GridBagLayout();
        configureFrame();
        initConstraints();
        createTitleLabels();
        createPlayerBattlefield(battlefieldConstraint);
        createEnemyBattlefield(battlefieldConstraint);
        
    }
    
    private void configureFrame() {
        int screenHeight, screenWidth, frameHeihgt, frameWidth;
        {
            Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
            screenHeight = screenDim.height;
            screenWidth = screenDim.width;
        }
        
        frameHeihgt = screenHeight/2;
        frameWidth = frameHeihgt * 4/3;
        
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameWidth, frameHeihgt);
        setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeihgt/2);
        setResizable(false);
        setLayout(layout);
    }
    
    private void initConstraints() {
        battlefieldConstraint = new GridBagConstraints();
        battlefieldConstraint.fill = GridBagConstraints.BOTH;
        battlefieldConstraint.weightx = 1;
        battlefieldConstraint.weighty = 1;
        battlefieldConstraint.gridheight = FIELD_WIDTH;
        battlefieldConstraint.gridwidth = FIELD_WIDTH;
    }
    
    private void createTitleLabels() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        
        gbc.gridwidth = FIELD_WIDTH;
        gbc.insets = LEFT_INSETS;
        JLabel playerLabel = new JLabel("Your ships will be here");
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        layout.setConstraints(playerLabel, gbc);
        add(playerLabel);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = RIGHT_INSETS;
        JLabel enemyLabel = new JLabel("Enemy ships will be here");
        enemyLabel.setHorizontalAlignment(JLabel.CENTER);
        layout.setConstraints(enemyLabel, gbc);
        add(enemyLabel);
    }
    
    private void createPlayerBattlefield(GridBagConstraints constraint) { 
        constraint.insets = LEFT_INSETS;
        JPanel battlefield = createBattlefield(playerField, false, Color.white);
        layout.setConstraints(battlefield, constraint);
        add(battlefield);
    }
    
    private void createEnemyBattlefield(GridBagConstraints constraint) {
        constraint.insets = RIGHT_INSETS;
        JPanel battlefield = createBattlefield(enemyField, true, new Color(0xF0F0F0));
        constraint.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(battlefield, constraint);
        add(battlefield);
    }
    
    private JPanel createBattlefield(JButton[] buttons, boolean enabled, Color color) {
        JPanel battlefield = new JPanel(new GridLayout(FIELD_WIDTH,FIELD_WIDTH));
                
        PlayerFieldListener listener = new PlayerFieldListener();
        for (int i = 0; i < FIELDS_COUNT; i++) {
            buttons[i] = new JButton();
            buttons[i].setBackground(color);
            buttons[i].setEnabled(enabled);
            buttons[i].setActionCommand(Integer.toString(i));
            if (enabled) {
                buttons[i].addActionListener(listener);
            }
            battlefield.add(buttons[i]);
        }
        
        return battlefield;
    }
    
    
}