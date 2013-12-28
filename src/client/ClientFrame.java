package client;

import client.battleship.*;
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
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ClientFrame extends JFrame {
    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            battleshipFrame = null;
        }
    }

    private User user = null;
    private Socket clientSocket = null;
    private InputStream sin;
    private OutputStream sout;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    private JPanel connectedPanel;
    private JPanel nonConnectedPanel;
    private JTextField playerNameTextField;
    private JTextField serverIPTextField;
    
    private final CardLayout mainCardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final JList<User> playersList;        // Заменить на JList<User>
    private BattleshipFrame battleshipFrame;
    private final BattleshipWindowListener battleshipWindowListener = new BattleshipWindowListener();
    
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
        playersList = new JList<>(new User[] {new User("Pit", new Socket())});
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
                System.out.println(playersList.getSelectedValue());
                //playersList.setListData(new String[] {"hahahah"});
                //playersList.add("as");
                //System.out.println(/*playersList.gets*/);
//                battleshipFrame = new BattleshipFrame();
//                battleshipFrame.addWindowListener(battleshipWindowListener);
//                setVisible(false);
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
                
                if ( !connect() ) {
                    return;
                }
                playersList.setEnabled(true);
                mainCardLayout.next(cardPanel);
            }
        });
        nonConnectedPanel.add(connectButton);
        cardPanel.add(nonConnectedPanel);
    }
    
    private boolean connect(){
        try {
            InetAddress ipAddress = InetAddress.getByName(serverIPTextField.getText());
            clientSocket = new Socket(ipAddress, Server.PORT);
            sin = clientSocket.getInputStream();
            sout = clientSocket.getOutputStream();
            input = new ObjectInputStream(sin);
            System.out.println("asdasd");
            output = new ObjectOutputStream(sout);
            
            Message authMessage = new Message(Message.AUTHORIZATION, playerNameTextField.getText());
            output.writeObject(authMessage);
        } catch (Exception ex) {
            System.out.println("FAIL (nothing epic..)");
            return false;
        }
        System.out.println("Successfully conected =)");
        return true;
    }
}
