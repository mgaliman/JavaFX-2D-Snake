/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author mgali
 */
public class MainMenuViewController implements Initializable {

    @FXML
    private StackPane spMainMenu;
    @FXML
    private AnchorPane apMainMenuWindow;
    @FXML
    private Label lbStartGame;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void StartGameClick(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/view/GameView.fxml"));
        
        Scene scene = lbStartGame.getScene();
        
        spMainMenu.getChildren().add(root);
        
        spMainMenu.getChildren().remove(apMainMenuWindow);
    }
    
}
