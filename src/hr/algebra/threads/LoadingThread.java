package hr.algebra.threads;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.algebra.controller.GameViewController;
import hr.algebra.utilities.SerializationUtils;

public class LoadingThread extends Thread {

    private SerializationUtils serializationUtils = new SerializationUtils();
    private Random random = new Random();
    private final String FILE_NAME = "serialization.ser";

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(5000));
            serializationUtils.read(FILE_NAME);
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(currentTime);

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GameViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoadingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
