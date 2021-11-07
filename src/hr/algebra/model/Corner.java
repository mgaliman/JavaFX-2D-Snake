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
public class Corner implements Serializable{
    
    public int x;
    public int y;

    public Corner(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
