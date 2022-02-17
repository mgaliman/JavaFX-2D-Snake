/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import static hr.algebra.Game.toRGBCode;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

/**
 *
 * @author mgali
 */
public class RandomColorThread implements Runnable{
    private final Parent parent;

    public RandomColorThread(Parent parent) {
        this.parent = parent;
    }

    @Override
    public void run() {

        Random rand = new Random();

        while(true) {
            
            double r = rand.nextDouble();
            double g = rand.nextDouble();
            double b = rand.nextDouble();
            double o = rand.nextDouble();
            Color randomColor = new Color(r, g, b, o);

            Platform.runLater(() ->
                    parent.setStyle("-fx-background-color: "
                            + toRGBCode(randomColor) + ";"));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomColorThread.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
