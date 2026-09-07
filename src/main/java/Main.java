import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Displays a simple Hello World JavaFX application.
 */
public class Main extends Application {
    /**
     * Displays the Hello World label in the primary application window.
     *
     * @param stage the primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!");
        Scene scene = new Scene(helloWorld);
        stage.setScene(scene);
        stage.show();
    }
}
