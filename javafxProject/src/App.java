import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
  
    Parent root;
    try {
        root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.web("#81c483"));
        primaryStage.setTitle("Wordle");
        primaryStage.setScene(scene);

        primaryStage.show(); //stage.show() always at the end of the function
    } catch (IOException e) {
    }
}

 public static void main(String[] args) {
        launch(args);
    }
}