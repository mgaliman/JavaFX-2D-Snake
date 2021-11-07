/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import com.sun.javafx.scene.traversal.Direction;
import hr.algebra.model.Corner;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    static int speed;
    static int foodColor;
    static int width;
    static int height;
    static int foodX;
    static int foodY;
    static int cornersize;
    static List<Corner> snake = new ArrayList<>();
    static Direction direction = Direction.LEFT;
    static boolean gameOver = false;
    static Random rand = new Random();

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
        init();
    }

    @FXML
    public void btnMainMenuClick() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/view/MainMenuView.fxml"));

        Scene scene = btnMainMenu.getScene();

        spGame.getChildren().add(root);

        spGame.getChildren().remove(apGameWindow);
    }

    private void init() {
        speed = 5;
        foodColor = 0;
        width = 20;
        height = 20;
        foodX = 0;
        foodY = 0;
        cornersize = 25;
        snake = new ArrayList<>();
        direction = Direction.LEFT;
        gameOver = false;

        //Food for snake 
        newFood();

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
            snake.add(new Corner(width / 2, height / 2));
        }
    }

    // tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            lbGameResult.setText("\tGAME OVER\n To continue press start!");
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

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

        // eat food
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y
                    == snake.get(i).y) {
                gameOver = true;
            }
        }

        // fill
        // background
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        // score        
        lbScore.setText(String.valueOf(speed - 6));

        // random foodcolor
        Color cc = Color.WHITE;

        switch (foodColor) {
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
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.BLACK;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize,
                cornersize);

        // snake
        for (Corner c : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1,
                    cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2,
                    cornersize - 2);
        }
    }

    // food
    public void newFood() {
        while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            foodColor = rand.nextInt(5);
            speed++;
            break;
        }
    }
}
