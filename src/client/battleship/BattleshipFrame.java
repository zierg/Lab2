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
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import logger.LoggerManager;
import settings.SettingsHandler;

public class BattleshipFrame extends JFrame {
    private static final Insets LEFT_INSETS = new Insets(0, 0, 0, 2);
    private static final Insets RIGHT_INSETS = new Insets(0, 2, 0, 0);
    private static final int BATTLEFIELD_SIDE = JButtonBattlefield.SIDE_FIELDS_COUNT;
    
    
    private final SettingsHandler translation;
    // ---- Translation -------
    private String frameTitle;
    private String opponentHitMessage;
    private String opponentMissMessage;
    private String winMessage;
    private String loseMessage;
    private String playerShipsLabel;
    private String enemyShipsLabel;
    private String chatTitle;
    // ------------------------
    
    private final CardLayout mainCardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final GridBagLayout playLayout = new GridBagLayout();
    private GridBagConstraints battlefieldConstraint;
        
    private JPanelBattlefield playerBF;
    private JPanelBattlefield enemyBF;
    private BrowseShipPanel browseShipPanel;
    
    private JPanelChat chat;
    private final String userName;
    
    protected List<BattleshipFrameListener> listeners = new LinkedList<>();
    
    public BattleshipFrame(String userName, SettingsHandler translation) {
        super();
        this.translation = translation;
        this.userName = userName;
        loadTranslation();
        configureFrame();
        initConstraints();
        createTitleLabels();
        createBattlefiedls();
        createChat(chatTitle);
        setVisible(true);
    }
    
    public BattleshipFrame(String userName, SettingsHandler translation, Image icon) {
        this(userName, translation);
        setIconImage(icon);
    }
    
    public void addBattleshipFrameListener(BattleshipFrameListener listener) {
        listeners.add(listener);
    }
    
    public void attackPlayer(int fieldNum) {
        boolean hit = playerBF.attack(fieldNum);
        if (hit) {
            sendChatMessage(opponentHitMessage);
        } else {
            sendChatMessage(opponentMissMessage);
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
        JOptionPane.showMessageDialog(this, winMessage);
    }
    
    public void sendChatMessage(String message) {
        chat.addMessage(message);
    }
    
    private void loadTranslation() {
        frameTitle = loadSetting(translation, "battleship_frame_title", "Battleship");
        opponentHitMessage = loadSetting(translation, "opponent_hit_message", "Your opponent hits your ship!");
        opponentMissMessage = loadSetting(translation, "opponent_miss_message", "Your opponent missed.");
        winMessage = loadSetting(translation, "win_message", "You won!");
        loseMessage = loadSetting(translation, "lose_message", "You lose!");
        
        playerShipsLabel = loadSetting(translation, "player_ships_label", "Your ships");
        enemyShipsLabel = loadSetting(translation, "enemy_ships_label", "Enemy ships");
        chatTitle = loadSetting(translation, "chat_title", "Chat");
    }
    
    private String loadSetting(SettingsHandler handler, String setting, String defaultValue) {
        String value = handler.readValue(setting);
        return (value==null ? defaultValue : value);
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
        
        setTitle(frameTitle);
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
        JLabel playerLabel = new JLabel(playerShipsLabel);
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        playLayout.setConstraints(playerLabel, gbc);
        add(playerLabel);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = RIGHT_INSETS;
        JLabel enemyLabel = new JLabel(enemyShipsLabel);
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
        browseShipPanel = new JButtonBrowseShipPanel(translation, 4, 3, 2, 1);
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
    
    private void createChat(String chatTitle) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        chat = new JPanelChat(userName, translation);
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
        JOptionPane.showMessageDialog(this, loseMessage);
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