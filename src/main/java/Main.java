import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import loki.exception.LokiExceptions;
import loki.logic.Logic;

/**
 * Displays a graphical interface for the Loki chatbot.
 */
public class Main extends Application {
    private static final double WINDOW_HEIGHT = 600.0;
    private static final double WINDOW_WIDTH = 400.0;

    private final Image lokiImage = createAvatar(Color.DARKRED);
    private final Image userImage = createAvatar(Color.DARKBLUE);

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Logic logic;

    /**
     * Displays the chatbot layout in the primary application window.
     *
     * @param stage the primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        logic = new Logic();

        scrollPane = new ScrollPane();
        dialogContainer = new VBox(8.0);
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        userInput.setPromptText("Enter a command...");
        sendButton = new Button("Send");
        sendButton.setOnAction(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);
        stage.setTitle("Loki");
        stage.setResizable(false);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);

        mainLayout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        scrollPane.setPrefSize(385.0, 535.0);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setFitToWidth(true);
        userInput.setPrefWidth(325.0);
        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) ->
                scrollPane.setVvalue(1.0));
        dialogContainer.getChildren().add(
                DialogBox.getLokiDialog("Greetings, mortal. Loki at your service.", lokiImage));
        stage.setOnCloseRequest(event -> saveData());
        stage.show();
    }

    /** Handles a command submitted through the input field or Send button. */
    private void handleUserInput() {
        String userText = userInput.getText().trim();
        if (userText.isEmpty()) {
            return;
        }

        String response = logic.processCommand(userText);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getLokiDialog(response, lokiImage));
        userInput.clear();
    }

    /** Saves the current task state before the JavaFX window closes. */
    private void saveData() {
        try {
            logic.save();
        } catch (LokiExceptions exception) {
            dialogContainer.getChildren().add(DialogBox.getLokiDialog(exception.getMessage(), lokiImage));
        }
    }

    /**
     * Creates a small circular avatar image for a dialog box.
     *
     * @param color the avatar color.
     * @return the generated avatar image.
     */
    private static Image createAvatar(Color color) {
        int imageSize = 40;
        int radius = imageSize / 2;
        WritableImage image = new WritableImage(imageSize, imageSize);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int x = 0; x < imageSize; x++) {
            for (int y = 0; y < imageSize; y++) {
                int distanceX = x - radius;
                int distanceY = y - radius;
                boolean isInsideCircle = distanceX * distanceX + distanceY * distanceY <= radius * radius;
                pixelWriter.setColor(x, y, isInsideCircle ? color : Color.TRANSPARENT);
            }
        }
        return image;
    }
}
