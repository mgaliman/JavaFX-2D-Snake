package hr.algebra.model.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.algebra.controller.GameViewController;
import hr.algebra.model.GameObjects;
import hr.algebra.model.JNDIInfo;
import hr.algebra.model.Position;
import hr.algebra.utilities.JndiUtils;
import javafx.application.Platform;

import static hr.algebra.controller.GameViewController.clientSnake;
import static hr.algebra.controller.GameViewController.clientSnakeSize;
import static hr.algebra.controller.GameViewController.clientStartingPosition;

public class Server extends Thread {

    private static final String PLAYER_JOINED = "Player joined!";
    private final GameViewController controller;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private ObjectOutputStream objectWriter;
    private BufferedReader in;
    private int numOfPlayers = 0;

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
                String deleteLine = in.readLine();
                while ((greeting = in.readLine()) != null) {
                    //Ispisi na drugi monitor...
                    Platform.runLater(() -> {

                        clientSnakeSize.setSnakeLength(GameObjects.getInstance().getSnakeSize().getSnakeLength());
                        clientStartingPosition.setX(GameObjects.getInstance().getPosition().getX());
                        clientStartingPosition.setY(GameObjects.getInstance().getPosition().getY());

                        for (int i = 0; i < clientSnakeSize.getSnakeLength(); i++) {
                            clientSnake.add(new Position(clientStartingPosition.getX(), clientStartingPosition.getY()));
                        }
                    });
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
