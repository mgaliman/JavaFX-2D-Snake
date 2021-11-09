/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mgali
 */
public class Food implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public int foodColor;
    public int foodX;
    public int foodY;
    public int score;

    public Food() {   
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(foodColor);
        oos.writeInt(foodX);
        oos.writeInt(foodY);
        oos.writeInt(score);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        foodColor = ois.readInt();
        foodX = ois.readInt();
        foodY = ois.readInt();
        score = ois.readInt();
    }
}
