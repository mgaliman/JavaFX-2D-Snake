package hr.algebra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Game extends Application {

    private static Stage mainStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        mainStage = stage;
        
        Parent root = FXMLLoader.load(getClass().getResource(
                "view/MainMenuView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }
}
