package client.battleship;

import client.battleship.events.*;
import java.awt.*;
import client.chat.*;
import javax.swing.*;

public class BattleshipFrame extends JFrame {
    private static final Insets LEFT_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets RIGHT_INSETS = new Insets(0, 2, 0, 0);
    private static final int BATTLEFIELD_SIDE = JButtonBattlefield.SIDE_FIELDS_COUNT;
    
    private final GridBagLayout layout;
    private GridBagConstraints battlefieldConstraint;
    
    
    
    private JPanelBattlefield playerBF;     // delete
    
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
        playerBF = new JButtonBattlefield(false);
        layout.setConstraints(playerBF, constraint);
        add(playerBF);
    }
    
    private void createEnemyBattlefield(GridBagConstraints constraint) {
        constraint.insets = RIGHT_INSETS;
        JPanelBattlefield battlefield = new JButtonBattlefield(true);
        battlefield.addBattlefieldActionListener(new BattlefieldActionListener() {

            @Override
            public void actionPerformed(BattlefieldActionEvent e) {
                setTitle(Boolean.toString(playerBF.attack(Integer.parseInt(e.getMessage()))));
            }
        });
        constraint.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(battlefield, constraint);
        add(battlefield);
    }
    
    private void createChat() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JPanelChat chat = new JPanelChat();
        chat.addChatActionListener(new ChatActionListener() {

            @Override
            public void actionPerformed(ChatActionEvent e) {
                setTitle(e.getMessage());
            }
        });
        layout.setConstraints(chat, gbc);
        add(chat);
    }
}