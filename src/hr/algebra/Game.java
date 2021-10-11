
import com.sun.javafx.scene.traversal.Direction;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Game extends Application {

    // variable    
    static Direction direction = Direction.LEFT;

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource(
                "hr/algebra/view/GameView.fxml"));

        Scene scene = new Scene(root);        

        primaryStage.setTitle("SNAKE GAME");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
