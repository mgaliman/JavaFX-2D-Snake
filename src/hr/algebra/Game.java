
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Game extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource(
                "hr/algebra/view/GameView.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("SNAKE GAME");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Scene changer
    public void switchScene(String fxmlFile) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource(fxmlFile));
        Parent root;
        {
            root = (Parent) loader.load();
            if (fxmlFile.equals("calculator.fxml")) {
                //BasicCalculatorView controller = (BasicCalculatorView) loader.getController();
                //controller.setModel(new BasicCalculatorModelTest(controller));
                //controller.setLogic(this);
            }
            this.stage.setScene(new Scene(root));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
