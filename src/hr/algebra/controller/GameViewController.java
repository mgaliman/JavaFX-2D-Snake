/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import com.sun.jmx.mbeanserver.Repository;
import hr.algebra.model.ChatMessage;
import hr.algebra.model.Position;
import hr.algebra.model.Food;
import hr.algebra.model.GameObjects;
import hr.algebra.model.JNDIInfo;
import hr.algebra.model.SnakeDirection;
import hr.algebra.model.SnakeSize;
import hr.algebra.model.networking.MessengerService;
import hr.algebra.model.networking.MessengerServiceImpl;
import hr.algebra.model.networking.Server;
import hr.algebra.threads.TimerThread;
import hr.algebra.utilities.JndiUtils;
import hr.algebra.utilities.ParserUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author mgali
 */
public class GameViewController implements Initializable {

    // variable
    static String fileName = "Serialization.ser";
    static int speed;
    static int width;
    static int height;

    static Food food = new Food();

    static int cornerSize;
    static List<Position> snake = new ArrayList<>(); // Current Snake Position

    static Position startingPosition = new Position();

    static boolean gameOver = false;
    static Random rand = new Random();

    SnakeSize snakeSize = new SnakeSize();

    //GameObjects gameObjects = new GameObjects();
    //Networking
    private Socket clientSocket;
    private Server serverThread;
    private boolean isHost;
    private MessengerService stub;
    private MessengerService messengerService;
    private JNDIInfo configurationInfo;
    private OutputStream os;
    private PrintStream printStream;
    private BufferedReader in;
    private PrintWriter out;

    //Server Snake
    static SnakeSize serverSnakeSize = new SnakeSize();
    static List<Position> serverSnake = new ArrayList<>();
    static Position serverStartingPosition = new Position();

    //@FXML is used for getting variables from fxml
    @FXML
    private Canvas cnGamePlatform;
    @FXML
    private AnchorPane apGameWindow;
    @FXML
    private Label lbGameResult;
    @FXML
    private StackPane spGame;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnMainMenu;
    @FXML
    private Label lbScore1;
    @FXML
    private Label lbScore2;
    @FXML
    public TextArea taChat;
    @FXML
    private Label lbTimer;
    @FXML
    public TextArea taInput;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnHost;
    @FXML
    private Button btnConnect;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new TimerThread(lbTimer).start();
        lbGameResult.setText("\tPress Start!");
        configurationInfo = JndiUtils.getConfigurationInfo();
        taInput.setDisable(true);
        btnSend.setDisable(true);
    }

    @FXML
    public void btnStartClick() {
        lbGameResult.setText("\tGame is running!");
        btnStart.setDisable(true);
        init(true);
    }
    /*
    @FXML
    private void btnSaveClick(MouseEvent event) {
        dataSerialization();
    }

    @FXML
    private void btnLoadClick(MouseEvent event) {
        lbGameResult.setText("\tGame is running!");
        //gameObjects = (GameObjects) SerializationUtils.read(fileName);
        GameObjects.getInstance().setFood(food);
        GameObjects.getInstance().setPosition(startingPosition);
        GameObjects.getInstance().setSnakeSize(snakeSize);
        //food = gameObjects.getFood();
        //startingPosition = gameObjects.getPosition();
        //snakeSize = gameObjects.getSnakeSize();

        init(false);
    }*/

    @FXML
    public void btnMainMenuClick() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/view/MainMenuView.fxml"));

        spGame.getChildren().add(root);

        spGame.getChildren().remove(apGameWindow);
    }

    @FXML
    private void btnSendClick(MouseEvent event) {
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            ChatMessage chatMessage = new ChatMessage("Player", taInput.getText().trim(), dateTimeFormatter.format(currentTime));
            stub.sendMessage(chatMessage);
            if (!isHost) {
                List<ChatMessage> messageList = stub.getAllMessages();
                ;
                StringBuilder sb = new StringBuilder();
                messageList.forEach((msg) -> {
                    sb.append(msg)
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));
                });

                taChat.setText(sb.toString());
                taInput.clear();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnHostClick(MouseEvent event) {
        try {
            messengerService = new MessengerServiceImpl(this);
            stub = (MessengerService) UnicastRemoteObject
                    .exportObject((MessengerService) messengerService, 0);
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(configurationInfo.getRegistry()));
            registry.rebind("MessengerService", stub);

            isHost = true;
            btnHost.setDisable(true);
            btnConnect.setDisable(true);
            taInput.setDisable(false);
            btnSend.setDisable(false);

            serverThread = new Server(this);
            serverThread.setDaemon(true);
            serverThread.start();
        } catch (RemoteException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Port already used!");
            alert.showAndWait();
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnConnectClick(MouseEvent event) throws IOException {
        try {
            messengerService = new MessengerServiceImpl(this);
            Registry registry = LocateRegistry.getRegistry();
            stub = (MessengerService) registry
                    .lookup("MessengerService");

            isHost = false;
            clientSocket = new Socket(configurationInfo.getName(), Integer.parseInt(configurationInfo.getPort()));
            System.out.println(clientSocket);

            Thread clientThread = new Thread(() -> {
                try {
                    os = clientSocket.getOutputStream();
                    printStream = new PrintStream(os);

                    btnConnect.setDisable(true);
                    btnHost.setDisable(true);
                    taInput.setDisable(false);
                    btnSend.setDisable(false);

                    System.out.println("The message was sent to the server!");

                    System.out.println("I'm receiving a message from the server!");
                    String greeting = "";
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String deleteLine = in.readLine();
                    while ((greeting = in.readLine()) != null) {
                        System.out.println("I read the message: " + greeting);
                        ParserUtils.parseMessage(greeting);

                        //Ispisi na drugi monitor...
                        Platform.runLater(() -> {

                            serverSnakeSize.setSnakeLength(GameObjects.getInstance().getSnakeSize().getSnakeLength());
                            serverStartingPosition.setX(GameObjects.getInstance().getPosition().getX());
                            serverStartingPosition.setY(GameObjects.getInstance().getPosition().getY());

                            for (int i = 0; i < serverSnakeSize.getSnakeLength(); i++) {
                                //Clear fali negdje
                                serverSnake.add(new Position(serverStartingPosition.getX(), serverStartingPosition.getY()));
                            }

                            System.out.println("Snake" + serverSnake);

                            System.out.println("Position" + serverStartingPosition);


                            food.setFoodX(GameObjects.getInstance().getFood().getFoodX());
                            food.setFoodY(GameObjects.getInstance().getFood().getFoodY());
                            food.setFoodColor(GameObjects.getInstance().getFood().getFoodColor());
                            System.out.println("Food" + food);
                            lbScore1.setText(String.valueOf(GameObjects.getInstance().getFood().getScore()));
                        });
                    }

                } catch (IOException ex) {
                    Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            clientThread.setDaemon(true);
            clientThread.start();
        } catch (RemoteException | NotBoundException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("There is no host to connect to!");
            alert.showAndWait();
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void init(boolean button) {

        speed = 1;
        width = 20;
        height = 20;
        cornerSize = 25;
        snake = new ArrayList<>(); //Current Snake Position

        gameOver = false;

        if (button) {
            if (isHost) {
                startingPosition.setX(0);
                startingPosition.setY(19);
            } else {
                startingPosition.setX(19);
                startingPosition.setY(19);
            }
            snakeSize.setSnakeLength(3);
            snakeSize.setDirection(SnakeDirection.UP);

            //Food for snake 
            newFood();
        }

        //Drawing on canvas
        GraphicsContext gc = cnGamePlatform.getGraphicsContext2D();

        //Keeps the game going
        new AnimationTimer() {

            @Override
            public void stop() {
                if (gameOver) {
                    super.stop();
                }
            }

            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) { //Problem line
                    lastTick = now;
                    tick(gc);
                }
            }
        }.start();

        //Snake control
        apGameWindow.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.W) {
                snakeSize.setDirection(SnakeDirection.UP);
            }
            if (key.getCode() == KeyCode.A) {
                snakeSize.setDirection(SnakeDirection.LEFT);
            }
            if (key.getCode() == KeyCode.S) {
                snakeSize.setDirection(SnakeDirection.DOWN);
            }
            if (key.getCode() == KeyCode.D) {
                snakeSize.setDirection(SnakeDirection.RIGHT);
            }
        });

        //Adding start snake parts
        for (int i = 0; i < snakeSize.getSnakeLength(); i++) {
            snake.add(new Position(startingPosition.getX(), startingPosition.getY()));
        }
    }

    // tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            lbGameResult.setText("\tGAME OVER\n Player ? WINS!");
            return;
        }

        //Get snake size
        snakeSize.snakeSize(snake);
        startingPosition.setX(snake.get(0).getX());
        startingPosition.setY(snake.get(0).getY());

        //Change direction and check if it hits wall
        switch (snakeSize.getDirection()) {
            case UP:
                snake.get(0).setY(snake.get(0).getY() - 1);
                if (snake.get(0).getY() < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).setY(snake.get(0).getY() + 1);
                if (snake.get(0).getY() >= height) {
                    gameOver = true;
                }
                break;
            case LEFT:
                snake.get(0).setX(snake.get(0).getX() - 1);
                if (snake.get(0).getX() < 0) {
                    gameOver = true;
                }
                break;
            case RIGHT:
                snake.get(0).setX(snake.get(0).getX() + 1);
                if (snake.get(0).getX() >= width) {
                    gameOver = true;
                }
                break;
        }

        //Eat food
        if (food.getFoodX() == snake.get(0).getX() && food.getFoodY() == snake.get(0).getY()) {
            snake.add(new Position(-1, -1));
            snakeSize.setSnakeLength(snakeSize.getSnakeLength() + 1);
            newFood();
            food.setScore(food.getScore() + 1);
        }

        //Self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).getX() == snake.get(i).getX() && snake.get(0).getY()
                    == snake.get(i).getY()) {
                gameOver = true;
            }
        }

        //Fill background
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, width * cornerSize, height * cornerSize);

        //Score
        if (isHost) {
            lbScore1.setText(String.valueOf(food.getScore()));
        } else {
            lbScore2.setText(String.valueOf(food.getScore()));
        }

        //Random foodColor
        Color cc = Color.WHITE;

        switch (food.getFoodColor()) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.ORANGE;
                break;
            case 2:
                cc = Color.RED;
                break;
            case 3:
                cc = Color.BLUE;
                break;
            case 4:
                cc = Color.KHAKI;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(food.getFoodX() * cornerSize, food.getFoodY() * cornerSize, cornerSize,
                cornerSize);

        //Snake color
        for (Position p : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(p.getX() * cornerSize, p.getY() * cornerSize, cornerSize - 1,
                    cornerSize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(p.getX() * cornerSize, p.getY() * cornerSize, cornerSize - 2,
                    cornerSize - 2);
        }

        if (!isHost){
            //Server snake color
            for (Position p : serverSnake) {
                gc.setFill(Color.LIGHTBLUE);
                gc.fillRect(p.getX() * cornerSize, p.getY() * cornerSize, cornerSize - 1,
                        cornerSize - 1);
                gc.setFill(Color.BLUE);
                gc.fillRect(p.getX() * cornerSize, p.getY() * cornerSize, cornerSize - 2,
                        cornerSize - 2);
            }
        }

        sendObjects();
    }

    //Food
    public void newFood() {
        while (true) {
            food.setFoodX(rand.nextInt(width));
            food.setFoodY(rand.nextInt(height));
            food.setFoodColor(rand.nextInt(5));
            speed++;
            break;
        }
    }

    private void sendObjects() {
        GameObjects.getInstance().getFood();
        GameObjects.getInstance().getPosition();
        GameObjects.getInstance().getSnakeSize();

        GameObjects.getInstance().setFood(food);
        GameObjects.getInstance().setPosition(startingPosition);
        GameObjects.getInstance().setSnakeSize(snakeSize);

        if (isHost) {
            new Thread(() -> {
                try {
                    clientSocket = serverThread.getClientSocket();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    out.println(GameObjects.getInstance());
                    System.out.println("I sent the message: " + GameObjects.getInstance() + " - " + clientSocket);
                } catch (IOException ex) {
                    Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }).start();
        }
    }
    /*
    private void dataSerialization() {
        try {
            //gameObjects = new GameObjects(food, startingPosition, snakeSize);
            GameObjects.getInstance().getFood();
            GameObjects.getInstance().getPosition();
            GameObjects.getInstance().getSnakeSize();

            SerializationUtils.write(GameObjects.getInstance().getFood(), fileName);

        } catch (IOException ex) {
            Logger.getLogger(GameViewController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
