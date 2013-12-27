package client;

import client.battleship.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ClientFrame extends JFrame {
    private class BattleshipWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(true);
            battleshipFrame = null;
        }
    }

    private final JPanel buttonsPanel;
    
    
    private final JList<String> playersList;
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
        
        playersList = new JList<>(new String[] {"asd", "asd", "asdasd qw"});
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane playersListScrollPane = new JScrollPane(playersList);
        layout.setConstraints(playersListScrollPane, constraints);
        add(playersListScrollPane);
        
        buttonsPanel = new JPanel();
        createButtonsPanel();
        constraints.weighty = 0;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        layout.setConstraints(buttonsPanel, constraints);
        add(buttonsPanel);
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
    
    private void createButtonsPanel() {
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
                /*battleshipFrame = new BattleshipFrame();
                battleshipFrame.addWindowListener(battleshipWindowListener);
                setVisible(false);*/
            }
        });
        
        buttonsPanel.add(startGameButton);
    }
}
