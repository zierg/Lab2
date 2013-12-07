package client.battleship;

import client.battleship.events.BattlefieldActionEvent;
import client.battleship.events.BattlefieldActionListener;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class BattleshipFrame extends JFrame {
    

    private static final Insets LEFT_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets RIGHT_INSETS = new Insets(0, 2, 0, 0);
    private static final int BATTLEFIELD_SIDE = JButtonBattlefield.SIDE_FIELDS_COUNT;
    
    private final GridBagLayout layout;
    

    
    private GridBagConstraints battlefieldConstraint;
    
    private JTextArea chatOutput;
    
    public BattleshipFrame() {
        layout = new GridBagLayout();
        configureFrame();
        initConstraints();
        createTitleLabels();
        createPlayerBattlefield(battlefieldConstraint);
        createEnemyBattlefield(battlefieldConstraint);
        createChat();
        
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
        battlefieldConstraint.gridheight = BATTLEFIELD_SIDE;
        battlefieldConstraint.gridwidth = BATTLEFIELD_SIDE;
    }
    
    private void createTitleLabels() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        
        gbc.gridwidth = BATTLEFIELD_SIDE;
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
       // JPanel battlefield = createBattlefield(playerField, false, Color.white);
        JPanelBattlefield battlefield = new JButtonBattlefield(false);
        layout.setConstraints(battlefield, constraint);
        add(battlefield);
    }
    
    private void createEnemyBattlefield(GridBagConstraints constraint) {
        constraint.insets = RIGHT_INSETS;
        //JPanel battlefield = createBattlefield(enemyField, true, new Color(0xF0F0F0));
        JPanelBattlefield battlefield = new JButtonBattlefield(true);
        battlefield.addBattlefieldActionListener(new BattlefieldActionListener() {

            @Override
            public void actionPerformed(BattlefieldActionEvent e) {
                setTitle(e.getMessage());
            }
        });
        constraint.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(battlefield, constraint);
        add(battlefield);
    }
    
    /*private JPanel createBattlefield(JButtonField[] buttons, boolean enabled, Color color) {
        JPanel battlefield = new JPanel(new GridLayout(FIELD_WIDTH,FIELD_WIDTH));
                
        PlayerFieldListener listener = new PlayerFieldListener();
        for (int i = 0; i < FIELDS_COUNT; i++) {
            buttons[i] = new BackgroundViewField(enabled, i);
            buttons[i].addActionListener(listener);
            buttons[i].setFill( (i%3 == 0) ? true : false );
            battlefield.add(buttons[i]);
        }
        
        return battlefield;
    }*/
    
    private void createChat() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        createChatLabel(gbc);     
        gbc.weighty = 0.3;
        createChatOutput(gbc);
        gbc.weighty = 0.05;
        createChatInput(gbc);
    }
    
    private void createChatLabel(GridBagConstraints gbc) {
        JLabel chatLabel = new JLabel("Chat:");
        layout.setConstraints(chatLabel, gbc);
        add(chatLabel);
    }
    
    private void createChatOutput(GridBagConstraints gbc) {
        chatOutput = new JTextArea("coming soon");
        chatOutput.setLineWrap(true);
        chatOutput.setEditable(false);
        JScrollPane chatPanel = new JScrollPane(chatOutput);
        layout.setConstraints(chatPanel, gbc);
        add(chatPanel);
    }
    
    private void createChatInput(GridBagConstraints gbc) {
        final JTextField chatInput = new JTextField();
        chatInput.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { 
                String msg = chatInput.getText();
                if (msg.length()==0) {
                    return;
                }
                chatOutput.append("\n" + msg);
                chatOutput.setCaretPosition(chatOutput.getText().length()-1);
                chatInput.setText("");
            }
        });
        
        layout.setConstraints(chatInput, gbc);
        add(chatInput);
    }
}