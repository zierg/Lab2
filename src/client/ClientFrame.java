package client;

import client.battleship.*;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class ClientFrame extends JFrame {
    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            battleshipFrame = null;
        }
    }

    private JPanel connectedPanel;
    private JPanel nonConnectedPanel;
    
    private final CardLayout mainCardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(mainCardLayout);
    
    private final JList<String> playersList;        // Заменить на JList<User>
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
        playersList = new JList<>(new String[] {"asd", "asd", "asdasd qw"});
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
                playersList.setListData(new String[] {"hahahah"});
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
        nonConnectedPanel.setLayout(new GridLayout());
        
        final JTextField serverIPTextField = new JTextField("127.0.0.1");
        nonConnectedPanel.add(serverIPTextField);
        
        final JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    InetAddress ipAddress = InetAddress.getByName(serverIPTextField.getText());
                    Socket socket = new Socket(ipAddress, Server.PORT);
                } catch (Exception ex) {
                    System.out.println("FAIL (nothing epic..)");
                }
                System.out.println("Successfully conected =)");
                playersList.setEnabled(true);
                mainCardLayout.next(cardPanel);
            }
        });
        nonConnectedPanel.add(connectButton);
        cardPanel.add(nonConnectedPanel);
    }
}
