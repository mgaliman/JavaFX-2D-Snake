package hr.algebra.threads;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.algebra.controller.GameViewController;
import hr.algebra.model.GameObjects;
import hr.algebra.utilities.SerializationUtils;

public class SavingThread extends Thread {

    SerializationUtils serializationUtils = new SerializationUtils();
    private final String FILE_NAME = "serialization.ser";

    @Override
    public void run() {
        try {
            serializationUtils.write(GameObjects.getInstance(), FILE_NAME);
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(currentTime);

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
