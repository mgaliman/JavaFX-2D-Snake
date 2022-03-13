/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.model.*;

/**
 *
 * @author mgali
 */
public class ParserUtils {

    private static final String DELIMITER = ",";

    public ParserUtils() {
    }

    public static void parseMessage(String message) {
        String[] details = message.split(DELIMITER);
        int foodColor = Integer.parseInt(details[0].trim());
        int foodX = Integer.parseInt(details[1].trim());
        int foodY = Integer.parseInt(details[2].trim());
        int score = Integer.parseInt(details[3].trim());
        int positionX = Integer.parseInt(details[4].trim());
        int positionY = Integer.parseInt(details[5].trim());
        int snakeLength = Integer.parseInt(details[6].trim());
        SnakeDirection direction = SnakeDirection.valueOf(details[7].trim());

        Food food = new Food();
        food.setFoodColor(foodColor);
        food.setFoodX(foodX);
        food.setFoodY(foodY);
        food.setScore(score);

        Position position = new Position();
        position.setX(positionX);
        position.setY(positionY);

        SnakeSize snakeSize = new SnakeSize();
        snakeSize.setSnakeLength(snakeLength);
        snakeSize.setDirection(direction);

        GameObjects.getInstance().setFood(food);
        GameObjects.getInstance().setPosition(position);
        GameObjects.getInstance().setSnakeSize(snakeSize);


    }
}
