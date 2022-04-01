/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.utilities.ReflectionUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class SettingsViewController implements Initializable {

    @FXML
    private StackPane spSettings;
    @FXML
    private AnchorPane apSettingsWindow;
    @FXML
    private Label lbBack;
    @FXML
    private Label lbSaveDocumentation;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void lbBackClick(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/view/MainMenuView.fxml"));

        Scene scene = lbBack.getScene();

        spSettings.getChildren().add(root);

        spSettings.getChildren().remove(apSettingsWindow);
    }

    @FXML
    private void lbSaveDocumentationClick(MouseEvent event) {
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>\n");
        builder.append("<html>\n");
        builder.append("<head>\n");
        builder.append("<title>Documentation</title>\n");
        builder.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css/\" rel=\"stylesheet\" integrity=\"sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3\" crossorigin=\"anonymous\">\n");
        builder.append("</head>\n");
        builder.append("<body style=\"margin:50px\">\n");
        builder.append("<h1 style=\"text-align:center; font-size:3em\">Basic information about packages</h1>\n");

        String packageLocation = ".\\src";
        packageWriter(packageLocation, "", builder);

        builder.append("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js/\" integrity=\"sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p\" crossorigin=\"anonymous\"></script>\n");
        builder.append("</body>\n");
        builder.append("</html>\n");

        builder.append("</body>\n");
        builder.append("</html>\n");

        try (FileWriter writer = new FileWriter("documentation.html")) {
            writer.write(builder.toString());

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successful documentation generation");
            alert.setHeaderText("Documentation for your code has been successfully "
                    + "generated!");
            alert.setContentText("File \"documentation.html\""
                    + "generated successfully!");

            alert.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(SettingsViewController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to generate documentation");
            alert.setHeaderText("Dokumentacija za Vaš kod nije "
                    + "generated!");
            alert.setContentText("File \"documentation.html\""
                    + "was not created due to a system error!");

            alert.showAndWait();
        }
    }

    private void packageWriter(String packageLocation, String packageLocation2, StringBuilder builder) {        
        String packageArray[] = new File(packageLocation).list();

        for (String packageName : packageArray) {

            String childPackageArray[] = new File(packageLocation + "\\" + packageName).list();
            if (!packageName.contains(".")) {
                builder.append("<h1>Package name: ")
                        .append(packageName)
                        .append(" (")
                        .append("".equals(packageLocation2) ? packageName : packageLocation2 + "." + packageName)
                        .append(")")
                        .append("</h1>\n");
            }

            String classArray[] = new File(packageLocation + "\\"
                    + packageName
            ).list();
            if (classArray != null) {

                for (String className : classArray) {

                    if (className.endsWith(".java") == false) {
                        continue;
                    }
                    try {
                        Class c = Class.forName(
                                packageLocation2 + "." + packageName + "."
                                + className.substring(0, className.indexOf(".")));
                        builder.append("<h2>&emsp;Class name: ")
                                .append(className)
                                .append("</h2>\n");
                        ReflectionUtils.readClassAndMembersInfo(c, builder);

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SettingsViewController.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
            }
            if (childPackageArray != null) {
                packageWriter(packageLocation + "\\" + packageName, "".equals(packageLocation2) ? packageName : packageLocation2 + "." + packageName, builder);

            }
        }
    }
}
