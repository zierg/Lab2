package client;

import client.battleship.*;
import client.battleship.events.*;
import client.chat.ChatActionEvent;
import client.events.*;
import network.*;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import logger.LoggerManager;
import settings.SettingsHandler;

public class ClientFrame extends JFrame implements NetworkClientMessengerListener {

    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            setPlaying(false);
            battleshipFrame = null;
            clientMessenger.setUserFree(opponent, user);
        }
    }
    
    private class NetworkBattleshipFrameListener implements BattleshipFrameListener {

        @Override
        public void turnMade(TurnMadeEvent e) {
            attackEnemy(e.getFieldNumber());
        }

        @Override
        public void turnResult(TurnResultEvent e) {
            turnResultRecieved(e.getFieldNumber(), e.getHit());
        }

        @Override
        public void playerIsReady(PlayerIsReadyEvent e) {
            battleshipFrame.setEnemyBattlefieldEnabled(false);
            if (!isOpponentReady) {
                battleshipFrame.sendChatMessage(playerFirstMessage);
            } else {
                battleshipFrame.sendChatMessage(playerSecondMessage);
            }
            isUserReady = true;
            clientMessenger.sendReadyMessage(opponent);
        }

        @Override
        public void gameOver(BSFrameGameOverEvent e) {
            isGameOver = true;
            clientMessenger.sendGameOverMessage(opponent, user);
        }

        @Override
        public void chatActionPerformed(ChatActionEvent e) {
            clientMessenger.sendTextMessage(opponent, e.getMessage());
        }      
    }
    
    private final SettingsHandler settings;
    private final SettingsHandler translation;
    
    // ---- Translation -------
    private String frameTitle;
    private String playerNameLabel;
    private String serverLabel;
    private String portLabel;
    private String connectButtonText;
    private String startButton;
    private String invitationTitle;
    private String invitationText;
    private String invitationYes;
    private String invitationNo;
    private String playerFirstMessage;
    private String playerSecondMessage;
    private String gameStartedMessage;
    private String playerHitMessage;
    private String playerMissMessage;
    private String opponentLeftMessage;
    private String connectionError;
    private String disconnectButtonText;
    // ------------------------
    
    private boolean playing = false;
    
    private User user;
    private boolean isUserReady = false;
    private User opponent;
    private boolean isOpponentReady = false;
    private NetworkClientMessenger clientMessenger;
    private boolean isGameOver = true;
      
    private JPanel connectedPanel;
    private JPanel nonConnectedPanel;
    private JTextField playerNameTextField;
    private JTextField serverIPTextField;
    private JTextField serverPortTextField;
    
    private final CardLayout mainCardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final JList<User> playersList;
    private BattleshipFrame battleshipFrame;
    private final BattleshipWindowListener battleshipWindowListener = new BattleshipWindowListener();
    private final NetworkBattleshipFrameListener battleshipFrameListener = new NetworkBattleshipFrameListener();
    
    public ClientFrame(SettingsHandler settings, SettingsHandler translation) {
        super();
        this.settings = settings;
        this.translation = translation;
        loadTranslation();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        configureFrame();
        
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        
        // Вынести в отдельный метод ----------
        playersList = new JList<>();
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersList.setEnabled(false);
        JScrollPane playersListScrollPane = new JScrollPane(playersList);
        layout.setConstraints(playersListScrollPane, constraints);
        add(playersListScrollPane);
        // ------------------------------------
        
        createNonConnectedPanel();
        createConnectedPanel();
        constraints.weighty = 0;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        layout.setConstraints(cardPanel, constraints);
        add(cardPanel);
        setVisible(true);
    }
    
    @Override
    public void usersListRefreshed(UsersListRefreshEvent e) {
        if (isPlaying()) {
            return;
        }
        setUsersList(e.getUsersList());
    }

    @Override
    public void invitedToPlay(InviteToPlayEvent e) {
        if (isPlaying()) {
            return;
        }
        User invitor = e.getInvitor();
        Object[] options = { invitationYes, invitationNo };
        int answer = JOptionPane.showOptionDialog(this, invitor + invitationText,
            invitationTitle, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        answerToInvitation(invitor, answer == 0);
    }
    
    @Override
    public void answerToInvitationRecieved(AnswerToInvitationEvent e) {
        if (isPlaying()) {
            return;
        }
        User invitor = e.getInvitor();
        boolean accept = e.getAccept();
        if (accept) {
            startGame(invitor);
        }
    }
    
    @Override
    public void turnMade(NetworkTurnMadeEvent e) {
        if (!isPlaying()) {
            return;
        }
        battleshipFrame.attackPlayer(e.getFieldNumber());
    }
    
    @Override
    public void turnResult(NetworkTurnResultEvent e) {
        if (!isPlaying()) {
            return;
        }
        int fieldNum = e.getFieldNumber();
        boolean hit = e.getHit();
        if (!hit) {
            battleshipFrame.setEnemyBattlefieldEnabled(false);
            battleshipFrame.sendChatMessage(playerMissMessage);
        } else {
            battleshipFrame.sendChatMessage(playerHitMessage);
        }
        battleshipFrame.setEnemyFieldFill(fieldNum, hit);
        battleshipFrame.attackEnemy(fieldNum);
    }
    
    @Override
    public void playerIsReady(NetworkPlayerIsReadyEvent e) {
        if (!isPlaying()) {
            return;
        }
        if (isUserReady) {
            battleshipFrame.setEnemyBattlefieldEnabled(true);
            battleshipFrame.sendChatMessage(gameStartedMessage);
        }
        isOpponentReady = true;
    }

    @Override
    public void gameOver(NetworkGameOverEvent e) {
        if (!isPlaying()) {
            return;
        }
        battleshipFrame.showWinMessage();
        isGameOver = true;
    }

    @Override
    public void textMessageRecieved(ChatActionEvent e) {
        if (!isPlaying()) {
            return;
        }
        battleshipFrame.sendChatMessage(e.getMessage());
    }
    
    @Override
    public void errorRecieved(ErrorEvent e) {
        if (isPlaying()) {
            battleshipFrame.sendChatMessage(e.getMessage());
        } else {
            showError(e.getMessage());
        }
    }
    
    @Override
    public void userLeftGame(UserLeftGameEvent e) {
        if (!isPlaying()) {
            return;
        }
        if (!isGameOver) {
            isGameOver = true;
            battleshipFrame.sendChatMessage(opponentLeftMessage);
            battleshipFrame.showWinMessage();
        }
    }
    
    private void loadTranslation() {
        frameTitle = loadSetting(translation, "client_frame_title", "Battleship");
        playerNameLabel = loadSetting(translation, "player_name_label", "Player name:");
        serverLabel = loadSetting(translation, "server_label", "Server:");
        portLabel = loadSetting(translation, "port_label", "Port:");
        connectButtonText = loadSetting(translation, "connect_button", "Connect");
        startButton = loadSetting(translation, "start_button", "Start game");
        invitationTitle = loadSetting(translation, "invitation_title", "Accept invitation");
        invitationText = loadSetting(translation, "invitation_text", " invited you to play. Accept?");
        invitationYes = loadSetting(translation, "invitation_yes", "Yes");
        invitationNo = loadSetting(translation, "invitation_no", "No");
        playerFirstMessage = loadSetting(translation, "player_first_message", "Your opponent is not ready. You will turn first. Wait please.");
        playerSecondMessage = loadSetting(translation, "player_second_message", "Game started! Your opponent turns first.");
        gameStartedMessage = loadSetting(translation, "game_started_message", "Game started! You turn first.");
        playerHitMessage = loadSetting(translation, "player_hit_message", "You hit the ship!");
        playerMissMessage = loadSetting(translation, "player_miss_message", "You missed.");
        opponentLeftMessage = loadSetting(translation, "opponent_left_message", "Your opponent has left the game.");
        connectionError = loadSetting(translation, "connection_error", "Connection error!");
        disconnectButtonText = loadSetting(translation, "disconncect_button", "Disconnect");
    }
    
    private String loadSetting(SettingsHandler handler, String setting, String defaultValue) {
        String value = handler.readValue(setting);
        return (value==null ? defaultValue : value);
    }
    
    private boolean configureMessengers(String serverName, int port) {
        try {
            NetworkMessenger messenger = new NetworkMessenger(serverName, port); // Поменять на чтение из поля
            clientMessenger = new NetworkClientMessenger(messenger);
            clientMessenger.addNetworkClientMessengerListener(this);
        } catch (IOException ex) {
            LoggerManager.ERROR_FILE_LOGGER.error("Messengers creating error. " + ClientFrame.class.toString() + ex);
            return false;
        }
        return true;
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
        
        setTitle(frameTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameWidth, frameHeihgt);
        setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeihgt/2);
        setResizable(false);
        try {
            Image icon =ImageIO.read(new File("data" + File.separator + "icon.png"));
            setIconImage(icon);
        }
        catch (Exception ex) {
            LoggerManager.ERROR_FILE_LOGGER.error(ex);
        }
    }
    
    private void createConnectedPanel() {
        connectedPanel = new JPanel();
        final JButton startGameButton = new JButton(startButton);
        startGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (playersList.isSelectionEmpty()) {
                    return;
                }
                User selectedUser = playersList.getSelectedValue();
                letsPlay(selectedUser);
            }
        });
        connectedPanel.add(startGameButton);
        
        final JButton disconnectButton = new JButton(disconnectButtonText);
        disconnectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clientMessenger.shutdown();
                clientMessenger = null;
                mainCardLayout.first(cardPanel);
            }
        });
        connectedPanel.add(disconnectButton);
        
        cardPanel.add(connectedPanel);
    }
    
    private void createNonConnectedPanel() {
        nonConnectedPanel = new JPanel();
        nonConnectedPanel.setLayout(new GridLayout(4, 2));
        
        nonConnectedPanel.add(new JLabel(playerNameLabel));
        playerNameTextField = new JTextField(loadSetting(settings, "player_name", "петя"));
        nonConnectedPanel.add(playerNameTextField);
        
        nonConnectedPanel.add(new JLabel(serverLabel));
        serverIPTextField = new JTextField(loadSetting(settings, "server", "127.0.0.1"));
        nonConnectedPanel.add(serverIPTextField);
        
        nonConnectedPanel.add(new JLabel(portLabel));
        serverPortTextField = new JTextField(loadSetting(settings, "port", "12345"));
        nonConnectedPanel.add(serverPortTextField);
        
        final JButton connectButton = new JButton(connectButtonText);
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                String playerName = playerNameTextField.getText().trim();
                String serverIP = serverIPTextField.getText().trim();
                String serverPort = serverPortTextField.getText().trim();
                if ( playerName.isEmpty() || serverIP.isEmpty() || serverPort.isEmpty() ) {
                    return;
                }
                if (!configureMessengers(serverIP, Integer.parseInt(serverPort))) {
                    showError(connectionError);
                    return;
                }
                if ( !connect(playerName) ) {
                    return;
                }
                settings.setValue("server", serverIP);
                settings.setValue("port", serverPort);
                settings.setValue("player_name", playerName);
                mainCardLayout.next(cardPanel);
            }
        });
        nonConnectedPanel.add(connectButton);
        cardPanel.add(nonConnectedPanel);
    }
    
    private boolean connect(String userName){
        boolean connected = clientMessenger.login(userName);
        if (connected) {
            user = new User(userName);
        }
        return connected;
    }
    
    private void refreshUsersList() {
        clientMessenger.getUsersList();
    }
    
    private void setUsersList(Vector<User> usersList) {
        trimUsersList(usersList);
        playersList.setListData(usersList);
        playersList.setEnabled(true);
    }
    
    private void trimUsersList(Vector<User> usersList) {
        ListIterator<User> iterator = usersList.listIterator();
        while(iterator.hasNext()) {
            User currentUser = iterator.next();
            if (!currentUser.isFree() || currentUser.equals(user)) {
                iterator.remove();
            }
            
        }
    }
    
    private void letsPlay(User withWhomWantsToPlay) {
        clientMessenger.letsPlay(user, withWhomWantsToPlay);
    }
    
    private void answerToInvitation(User invitor, boolean accept) {
        clientMessenger.answerToInvitation(invitor, user, accept);
        if (accept) {
            startGame(invitor);
        }
    }
    
    private void setPlaying(boolean playing) {
        this.playing = playing;
    }
    
    private boolean isPlaying() {
        return playing;
    }
    
    private void startGame(User opponent) {
        this.opponent = opponent;
        battleshipFrame = new BattleshipFrame(user.getName(), translation, this.getIconImage());
        battleshipFrame.addWindowListener(battleshipWindowListener);
        battleshipFrame.addBattleshipFrameListener(battleshipFrameListener);
        setPlaying(true);
        setVisible(false);
        isGameOver = false;
    }
    
    private void attackEnemy(int fieldNum) {
        clientMessenger.attack(opponent, fieldNum);
    }
    
    private void turnResultRecieved (int fieldNum, boolean hit) {
        if (!hit) {
            battleshipFrame.setEnemyBattlefieldEnabled(true);
        }
        clientMessenger.sendTurnResult(opponent, fieldNum, hit);
    }
    
    private void showError(String errorText) {
        JOptionPane.showMessageDialog(this, errorText, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
