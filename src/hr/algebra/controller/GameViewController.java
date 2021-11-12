/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import com.sun.javafx.scene.traversal.Direction;
import hr.algebra.model.Position;
import hr.algebra.model.Food;
import hr.algebra.model.SnakeSize;
import hr.algebra.utilities.SerializationUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    Food food = new Food();
    static int cornersize;
    static List<Position> snake = new ArrayList<>();
    static Direction direction = Direction.LEFT;
    static boolean gameOver = false;
    static Random rand = new Random();
    SnakeSize snakeSize = new SnakeSize();

    //@FXML is used for getting variables from fxml
    @FXML
    private Canvas cnGamePlatform;
    @FXML
    private AnchorPane apGameWindow;
    @FXML
    private Label lbScore;
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
    private Button btnLoad;
    @FXML
    private Button btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbGameResult.setText("\tPress Start!");
    }

    @FXML
    public void btnStartClick() {
        lbGameResult.setText("\tGame is running!");
        init(true);
    }

    @FXML
    private void btnSaveClick(MouseEvent event) {
        dataSeriazlization();
    }

    @FXML
    private void btnLoadClick(MouseEvent event) {
        try {            
            food.foodX = 2;
            food.foodY = 2;
            food.foodColor = 2;
            SerializationUtils.read(fileName);
            init(false);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void btnMainMenuClick() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/view/MainMenuView.fxml"));

        spGame.getChildren().add(root);

        spGame.getChildren().remove(apGameWindow);
    }

    private void init(boolean button) {
        speed = 5;
        width = 20;
        height = 20;
        cornersize = 25;
        snake = new ArrayList<>();
        direction = Direction.LEFT;
        gameOver = false;

        if (button) {
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
                direction = Direction.UP;
            }
            if (key.getCode() == KeyCode.A) {
                direction = Direction.LEFT;
            }
            if (key.getCode() == KeyCode.S) {
                direction = Direction.DOWN;
            }
            if (key.getCode() == KeyCode.D) {
                direction = Direction.RIGHT;
            }
        });

        //Adding start snake parts
        for (int i = 0; i < 3; i++) {
            snake.add(new Position(width / 2, height / 2));
        }
    }

    // tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            lbGameResult.setText("\tGAME OVER\n To continue press start!");
            return;
        }

        //Get snake size
        snakeSize.snakeSize(snake);

        //Change direction and check if it hits wall
        switch (direction) {
            case UP:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y >= height) {
                    gameOver = true;
                }
                break;
            case LEFT:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case RIGHT:
                snake.get(0).x++;
                if (snake.get(0).x >= width) {
                    gameOver = true;
                }
                break;
        }

        //Eat food
        if (food.foodX == snake.get(0).x && food.foodY == snake.get(0).y) {
            snake.add(new Position(-1, -1));
            newFood();
            food.score++;
            System.out.println("Food eaten");
        }

        //Self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y
                    == snake.get(i).y) {
                gameOver = true;
            }
        }

        //Fill background
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        //Score     
        lbScore.setText(String.valueOf(food.score));

        //Random foodColor
        Color cc = Color.WHITE;

        switch (food.foodColor) {
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
        gc.fillOval(food.foodX * cornersize, food.foodY * cornersize, cornersize,
                cornersize);

        //Snake color
        for (Position p : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(p.x * cornersize, p.y * cornersize, cornersize - 1,
                    cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(p.x * cornersize, p.y * cornersize, cornersize - 2,
                    cornersize - 2);
        }
    }

    //Food
    public void newFood() {
        while (true) {
            food.foodX = rand.nextInt(width);
            food.foodY = rand.nextInt(height);

            food.foodColor = rand.nextInt(5);
            speed++;            
            break;
        }
    }

    private void dataSeriazlization() {
        try {
            SerializationUtils.write(food, fileName);
        } catch (IOException ex) {
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
