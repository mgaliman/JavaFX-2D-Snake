/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model.networking;

import hr.algebra.controller.GameViewController;
import hr.algebra.model.GameObjects;
import hr.algebra.model.JNDIInfo;
import hr.algebra.utilities.JndiUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgali
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private ObjectOutputStream objectWriter;
    private BufferedReader in;
    private static final String PLAYER_JOINED = "Player joined!";

    private int numOfPlayers = 0;

    private final GameViewController controller;

    public Server(GameViewController controller) {
        this.controller = controller;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        JNDIInfo configurationInfo = JndiUtils.getConfigurationInfo();
        try {
            serverSocket = new ServerSocket(Integer.parseInt(configurationInfo.getPort()));
            System.out.println("The server socket was successfully created!");
            while (numOfPlayers < 1) {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket);
                controller.taChat.setText(PLAYER_JOINED);
                numOfPlayers++;
                System.out.println("Connection accepted!");
            }
            System.out.println("2 players are in the game. Player entry is no longer allowed!");

            while (true) {
                objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("The connection with the client is successfully established!");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("I connected to the client!");
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                GameObjects gameObjects = GameObjects.getInstance();
                out.println(gameObjects);

                String greeting = "";
                while ((greeting = in.readLine()) != null) {
                    System.out.println("I read the message: " + greeting);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
