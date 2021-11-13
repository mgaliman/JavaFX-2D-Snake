/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;

/**
 *
 * @author mgali
 */
public class GameObjects implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Food food;
    private Position position;
    private SnakeSize snakeSize;

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public SnakeSize getSnakeSize() {
        return snakeSize;
    }

    public void setSnakeSize(SnakeSize snakeSize) {
        this.snakeSize = snakeSize;
    }

    public GameObjects() {
    }    
    
    public GameObjects(Food food, Position position, SnakeSize snakeSize) {
        this.food = food;
        this.position = position;
        this.snakeSize = snakeSize;
    }

     
}
