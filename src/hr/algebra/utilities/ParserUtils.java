/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.model.GameObjects;
import hr.algebra.model.Position;
import hr.algebra.model.SnakeDirection;

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
        int foodColor = Integer.valueOf(details[0].trim());
        int foodX = Integer.valueOf(details[1].trim());
        int foodY = Integer.valueOf(details[2].trim());
        int score = Integer.valueOf(details[3].trim());
        int positionX = Integer.valueOf(details[4].trim());
        int positionY = Integer.valueOf(details[5].trim());
        int snakeLength = Integer.valueOf(details[6].trim());
        SnakeDirection direction = SnakeDirection.valueOf(details[7].trim());

        /*Food food = new Food();
        food.setFoodColor(foodColor);
        food.setFoodX(foodX);
        food.setFoodY(foodY);
        food.setScore(score);*/
        Position position = new Position();
        position.setX(positionX);
        position.setY(positionY);

        GameObjects.getInstance().setPosition(position);
    }
}
