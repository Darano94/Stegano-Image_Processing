package Main;

import Mvc.Controller;
import Mvc.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        final Model model = new Model();
        FXMLLoader loader = new FXMLLoader();
        Controller controller = new Controller(model);

        loader.setLocation(getClass().getResource("/Mvc/View.fxml"));
        loader.setControllerFactory(param -> controller);

        Parent root = (BorderPane) loader.load();
        Scene scene = new Scene(root, 1240, 720);

        controller.setScene(scene);
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Steganography and Imageprocessing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
