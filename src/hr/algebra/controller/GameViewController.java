/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import com.sun.javafx.scene.traversal.Direction;
import hr.algebra.model.Corner;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author mgali
 */
public class GameViewController implements Initializable {

    // variable
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Direction direction = Direction.LEFT;
    static boolean gameOver = false;
    static Random rand = new Random();

    @FXML
    private Canvas cnGamePlatform;

    @FXML
    private AnchorPane apGameWindow;

    @FXML
    private Label lbScore;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        newFood();
        GraphicsContext gc = cnGamePlatform.getGraphicsContext2D();

        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }
        }.start();

        // control
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

        // add start snake parts
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
    }

    // tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) {
            case UP:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case DOWN:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
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
                if (snake.get(0).x > width) {
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

        switch (foodcolor) {
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
        start:
        while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;

        }
    }
}
