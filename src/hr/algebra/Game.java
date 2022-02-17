package hr.algebra;

import hr.algebra.threads.RandomColorThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Game extends Application {

    private static Stage mainStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        mainStage = stage;
        
        Parent root = FXMLLoader.load(getClass().getResource(
                "view/MainMenuView.fxml"));
        
        RandomColorThread thread = new RandomColorThread(root);
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        new Thread(thread).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }
    
    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
