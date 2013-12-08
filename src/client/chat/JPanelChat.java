package client.chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JPanelChat extends JPanel {
    private class ChatInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { 
            String message = chatInput.getText();
            if (message.length()==0) {
                return;
            }
            addMessage(message);
            listenAction(new ChatActionEvent(this, message));
        }
    }
    
    protected List<ChatActionListener> listeners = new LinkedList<>();
    
    private JTextArea chatOutput;
    private JTextField chatInput;
    
    private ChatInputListener chatInputListener = new ChatInputListener();
    
    GridBagLayout layout = new GridBagLayout();

    public JPanelChat() {
        super();
        setLayout(layout);
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
        JLabel chatLabel = new JLabel("Chat: ");
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
        chatInput = new JTextField();
        chatInput.addActionListener(chatInputListener);
        layout.setConstraints(chatInput, gbc);
        add(chatInput);
    }
    
    public void addChatActionListener(ChatActionListener listener) {
        listeners.add(listener);
    }
    
    public void addMessage(String message) {
        chatOutput.append("\n" + message);
        chatOutput.setCaretPosition(chatOutput.getText().length()-1);
        chatInput.setText("");
    }
    
    private void listenAction(ChatActionEvent e) {
        ListIterator<ChatActionListener> iterator = listeners.listIterator();
        while ( iterator.hasNext() ) {
            iterator.next().actionPerformed(e);
        }
    }
}