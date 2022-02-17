/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.scene.control.Label;
import java.util.logging.Logger;

/**
 *
 * @author mgali
 */
public class TimerThread extends Thread{
    private Label label;

    public TimerThread(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        while(true) {
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            Platform.runLater(() ->{
                label.setText(dateTimeFormatter.format(currentTime));
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimerThread.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
}
