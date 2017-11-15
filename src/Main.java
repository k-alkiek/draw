import controllers.commands.commandsClasses.LoadExtension;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LoadExtension cl = new LoadExtension("/Users/khaledabdelfattah/draw/out/artifacts/draw_jar/draw.jar");
        System.out.println(cl.addExtension().get(0).getName());
        launch(args);
    }
}
