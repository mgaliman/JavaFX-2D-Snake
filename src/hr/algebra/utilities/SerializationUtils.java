/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationUtils {

    private static boolean workInProgress = false;

    public SerializationUtils() {
    }

    public synchronized void write(Object object, String fileName) throws IOException, InterruptedException {
        System.out.println("Thread has been activated");
        while (workInProgress) {
            System.out.println("WAIT! Other thread is in process");
            wait();
        }
        workInProgress = true;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(object);
        }
        System.out.println("SAVED");
        workInProgress = false;
        notifyAll();
    }

    public synchronized Object read(String fileName) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Thread has been activated");
        while (workInProgress) {
            System.out.println("WAIT! Other thread is in process");
            wait();
        }
        workInProgress = true;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            System.out.println("LOADED");
            workInProgress = false;
            notifyAll();
            return ois.readObject();
        }
    }
}
