package client.battleship;

import client.battleship.events.*;
import client.battleship.events.bscomponents.*;
import client.chat.*;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BattleshipFrame extends JFrame {
    private static final Insets LEFT_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets RIGHT_INSETS = new Insets(0, 2, 0, 0);
    private static final int BATTLEFIELD_SIDE = JButtonBattlefield.SIDE_FIELDS_COUNT;
    
    private final CardLayout mainCardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final GridBagLayout playLayout = new GridBagLayout();
    private GridBagConstraints battlefieldConstraint;
        
    private JPanelBattlefield playerBF;
    private JPanelBattlefield enemyBF;
    private JButtonBrowseShipPanel browseShipPanel;
    
    private JPanelChat chat;
    private final String userName;
    
    protected List<BattleshipFrameListener> listeners = new LinkedList<>();
    
    public BattleshipFrame(String userName) {
        super();
        this.userName = userName;
        configureFrame();
        initConstraints();
        createTitleLabels();
        createBattlefiedls();
        createChat();
        setVisible(true);
    }
    
    public void addBattleshipFrameListener(BattleshipFrameListener listener) {
        listeners.add(listener);
    }
    
    public void attackPlayer(int fieldNum) {
        boolean hit = playerBF.attack(fieldNum);
        if (hit) {
            sendChatMessage("Your opponent hits your ship!");
        } else {
            sendChatMessage("Your opponent missed!");
        }
        listenTurnResult(new TurnResultEvent(this, fieldNum, hit));
    }
    
    public void attackEnemy(int fieldNum) {
        enemyBF.attack(fieldNum);
    }
    
    public void setEnemyBattlefieldEnabled(boolean enabled) {
        enemyBF.setEnabled(enabled);
    }
    
    public void setEnemyFieldFill(int fieldNum, boolean filled) {
        enemyBF.setFill(fieldNum, filled);
    }
    
    public void showWinMessage() {
        enemyBF.setEnabled(false);
        JOptionPane.showMessageDialog(this, "You won!");
    }
    
    public void sendChatMessage(String message) {
        chat.addMessage(message);
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
        frameWidth = frameHeihgt * 4/3;
        
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeihgt);
        setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeihgt/2);
        setResizable(false);
        setLayout(playLayout);
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
        playLayout.setConstraints(playerLabel, gbc);
        add(playerLabel);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = RIGHT_INSETS;
        JLabel enemyLabel = new JLabel("Enemy ships will be here");
        enemyLabel.setHorizontalAlignment(JLabel.CENTER);
        playLayout.setConstraints(enemyLabel, gbc);
        add(enemyLabel);
    }
    
    private void createBattlefiedls() {
        JPanel battlefieldsPanel = new JPanel(new GridLayout(1, 2));
        createPlayerBattlefield();
        battlefieldsPanel.add(playerBF);

        createBroweShipPanel();
        cardPanel.add(browseShipPanel);

        createEnemyBattlefield();
        cardPanel.add(enemyBF);
        battlefieldsPanel.add(cardPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        playLayout.setConstraints(battlefieldsPanel, gbc);
        add(battlefieldsPanel);
    }
    
    private void createBroweShipPanel() {
        browseShipPanel = new JButtonBrowseShipPanel(0,0,2);// тестить проще(4, 3, 2, 1);
        browseShipPanel.addBrowseShipPanelEmptyListener(new BrowseShipPanelEmptyListener() {

            @Override
            public void panelIsEmpty(BrowseShipPanelEmptyEvent e) {
                playerBF.setAvailable(true);
                showEnemyBF();
                listenPlayerIsReady(new PlayerIsReadyEvent(this));
            }
        });
        browseShipPanel.addBrowseShipPanelActionListener(new BrowseShipPanelActionListener() {

            @Override
            public void actionPerformed(BrowseShipPanelActionEvent e) {
                playerBF.setEnabled(true);
            }
        });
    }
    
    private void createPlayerBattlefield() { 
        playerBF = new JButtonBattlefield(false);
        playerBF.addBattlefieldActionListener(new BattlefieldActionListener() {

            @Override
            public void actionPerformed(BattlefieldActionEvent e) {
                try {
                    playerBF.addShip(Integer.parseInt(e.getMessage()), browseShipPanel.getSelectedShip());
                } catch (UncorrectFieldException ex) {
                    return;
                }
                browseShipPanel.deleteSelectedShip();
                playerBF.setEnabled(false);
            }
        });
        playerBF.addBattlefieldGameOverListener(new GameOverListener() {

            @Override
            public void gameOver(GameOverEvent e) {
                enemyBF.setEnabled(false);
                listenGameOver(new BSFrameGameOverEvent(this));
                showLoseMessage();
            }
        });
    }
    
    private void createEnemyBattlefield() {       
        enemyBF = new JButtonBattlefield(true);
        enemyBF.addBattlefieldActionListener(new BattlefieldActionListener() {

            @Override
            public void actionPerformed(BattlefieldActionEvent e) {
                listenTurnMade(new TurnMadeEvent(this, Integer.parseInt(e.getMessage())));
            }
        });
    }
    
    private void createChat() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        chat = new JPanelChat(userName);
        chat.addChatActionListener(new ChatActionListener() {

            @Override
            public void actionPerformed(ChatActionEvent e) {
                listenChatAction(e);
            }
        });
        playLayout.setConstraints(chat, gbc);
        add(chat);
    }
    
    private void showEnemyBF() {
        mainCardLayout.last(cardPanel);
    }
      
    private void showLoseMessage() {
        enemyBF.setEnabled(false);
        JOptionPane.showMessageDialog(this, "You lose!");
    }
    
    private void listenTurnMade(TurnMadeEvent e) {
        for (BattleshipFrameListener listener : listeners) {
            listener.turnMade(e);
        }
    }
    
    private void listenTurnResult(TurnResultEvent e) {
        for (BattleshipFrameListener listener : listeners) {
            listener.turnResult(e);
        }
    }
    
    private void listenPlayerIsReady(PlayerIsReadyEvent e) {
        for (BattleshipFrameListener listener : listeners) {
            listener.playerIsReady(e);
        }
    }
    
    private void listenGameOver(BSFrameGameOverEvent e) {
        for (BattleshipFrameListener listener : listeners) {
            listener.gameOver(e);
        }
    }
    
    private void listenChatAction(ChatActionEvent e) {
        for (BattleshipFrameListener listener : listeners) {
            listener.chatActionPerformed(e);
        }
    }
}