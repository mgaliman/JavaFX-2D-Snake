/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model.networking;

import hr.algebra.controller.GameViewController;
import hr.algebra.model.ChatMessage;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class MessengerServiceImpl implements MessengerService {

    private  List<ChatMessage> messageList = new ArrayList<>();
    
    private final GameViewController controller;

    public MessengerServiceImpl(GameViewController controller) {
        this.controller = controller;
    }
    
    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        messageList.add(message);
        StringBuilder sb = new StringBuilder();
        messageList.forEach((msg) -> {
            sb.append(msg)
                    .append(System.getProperty("line.separator"))
                    .append(System.getProperty("line.separator"));
        });

        controller.taChat.setText(sb.toString());
        controller.taInput.clear();
    }

    @Override
    public List<ChatMessage> getAllMessages() throws RemoteException {
        return messageList;
    }

    @Override
    public ChatMessage getlastChatMessage() throws RemoteException {
        return messageList.get(messageList.size() - 1);
    }
    
}
