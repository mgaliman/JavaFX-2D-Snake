package hr.algebra.threads;

import java.io.IOException;
import java.time.LocalDateTime;

import hr.algebra.utilities.SerializationUtils;

public class LoadingThread extends Thread {

    private SerializationUtils serializationUtils = new SerializationUtils();
    private final String FILE_NAME = "serialization.ser";

    @Override
    public void run() {
        try {
            serializationUtils.read(FILE_NAME);
            LocalDateTime currentTime = LocalDateTime.now();
            System.out.println(currentTime);
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
