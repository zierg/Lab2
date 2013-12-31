package client;

import client.battleship.*;
import client.battleship.events.*;
import client.chat.ChatActionEvent;
import client.events.*;
import network.*;
import server.Server;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JOptionPane;

public class ClientFrame extends JFrame implements NetworkClientMessengerListener {

    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            setPlaying(false);
            battleshipFrame = null;
            clientMessenger.setUserFree(user);
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
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void gameOver(BSFrameGameOverEvent e) {
            //battleshipFrame.showWinMessage();
            clientMessenger.sendGameOverMessage(opponent, user);
        }

        @Override
        public void chatActionPerformed(ChatActionEvent e) {
            clientMessenger.sendTextMessage(opponent, e.getMessage());
        }      
    }
    
    private boolean playing = false;
    
    private User user;
    private User opponent;
    private NetworkClientMessenger clientMessenger;
      
    private JPanel connectedPanel;
    private JPanel nonConnectedPanel;
    private JTextField playerNameTextField;
    private JTextField serverIPTextField;
    
    private final CardLayout mainCardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final JList<User> playersList;
    private BattleshipFrame battleshipFrame;
    private final BattleshipWindowListener battleshipWindowListener = new BattleshipWindowListener();
    private final NetworkBattleshipFrameListener battleshipFrameListener = new NetworkBattleshipFrameListener();
    
    public ClientFrame() {
        super();
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
    public void usersListRefreshed(UsersListRefreshedEvent e) {
        if (isPlaying()) {
            return;
        }
        setUsersList(e.getUsersList());
    }
    
    @Override
    public void invitedToPlay(InvitedToPlayEvent e) {
        if (isPlaying()) {
            return;
        }
        User invitor = e.getInvitor();
        Object[] options = { "Yes", "No" };
        int answer = JOptionPane.showOptionDialog(this, invitor + " invited you to play. Accept?",
            "Accept invitation", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        answerToInvitation(invitor, answer == 0);
    }
    
    @Override
    public void answerToInvitationRecieved(AnswerToInvitationRecievedEvent e) {
        if (isPlaying()) {
            return;
        }
        User invitor = e.getInvitor();
        boolean accept = e.getAccept();
        System.out.println("User " + invitor + (accept ? "wants" : "does not want") + " play with you.");
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
        }
        battleshipFrame.setEnemyFieldFill(fieldNum, hit);
        battleshipFrame.attackEnemy(fieldNum);
    }
    
    @Override
    public void playerIsReady(NetworkPlayerIsReadyEvent e) {
        if (!isPlaying()) {
            return;
        }
        clientMessenger.sendReadyMessage(opponent);
    }

    @Override
    public void gameOver(NetworkGameOverEvent e) {
        if (!isPlaying()) {
            return;
        }
        battleshipFrame.showWinMessage();
    }

    @Override
    public void textMessageRecieved(ChatActionEvent e) {
        battleshipFrame.sendChatMessage(opponent + ": " + e.getMessage());
    }
    
    private boolean configureMessengers(String serverName) {
        try {
            NetworkMessenger messenger = new NetworkMessenger(serverName, Server.PORT); // Поменять на чтение из поля
            clientMessenger = new NetworkClientMessenger(messenger);
            clientMessenger.addNetworkClientMessengerListener(this);
        } catch (IOException ex) {
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
        
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameWidth, frameHeihgt);
        setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeihgt/2);
        setResizable(false);
    }
    
    private void createConnectedPanel() {
        connectedPanel = new JPanel();
        final JButton startGameButton = new JButton("Start game");
        startGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (playersList.isSelectionEmpty()) {
                    return;
                }
                User selectedUser = playersList.getSelectedValue();
                System.out.println("I wanna play with " + selectedUser);
                letsPlay(selectedUser);
            }
        });
        connectedPanel.add(startGameButton);
        cardPanel.add(connectedPanel);
    }
    
    private void createNonConnectedPanel() {
        nonConnectedPanel = new JPanel();
        nonConnectedPanel.setLayout(new GridLayout(3, 2));
        
        nonConnectedPanel.add(new JLabel("Player name:"));
        playerNameTextField = new JTextField("петя");
        nonConnectedPanel.add(playerNameTextField);
        
        nonConnectedPanel.add(new JLabel("Server:"));
        serverIPTextField = new JTextField("127.0.0.1");
        nonConnectedPanel.add(serverIPTextField);
        
        final JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (!configureMessengers(serverIPTextField.getText())) {
                    return;
                }
                if ( !connect() ) {
                    return;
                }
                mainCardLayout.next(cardPanel);
            }
        });
        nonConnectedPanel.add(connectButton);
        cardPanel.add(nonConnectedPanel);
    }
    
    private boolean connect(){
        String userName = playerNameTextField.getText();
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
        battleshipFrame = new BattleshipFrame(user.getName());
        battleshipFrame.addWindowListener(battleshipWindowListener);
        battleshipFrame.addBattleshipFrameListener(battleshipFrameListener);
        setPlaying(true);
        setVisible(false);
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
}
