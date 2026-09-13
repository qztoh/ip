import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Displays one user or Loki message with its avatar.
 */
public class DialogBox extends HBox {
    @FXML
    private Label text;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box using the reusable FXML message layout.
     *
     * @param message the message to display.
     * @param image the avatar image to display.
     */
    public DialogBox(String message, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load a dialog box", exception);
        }

        text.setText(message);
        displayPicture.setImage(image);
        getStyleClass().add("dialog-box");
    }

    /**
     * Creates a dialog box aligned as a user message.
     *
     * @param message the user's message.
     * @param image the user's avatar image.
     * @return the user dialog box.
     */
    public static DialogBox getUserDialog(String message, Image image) {
        DialogBox dialogBox = new DialogBox(message, image);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /**
     * Creates a dialog box aligned as a Loki response.
     *
     * @param message Loki's response.
     * @param image Loki's avatar image.
     * @return the Loki dialog box.
     */
    public static DialogBox getLokiDialog(String message, Image image) {
        return getLokiDialog(message, image, false);
    }

    /**
     * Creates a Loki response dialog and optionally marks it as an error.
     *
     * @param message Loki's response.
     * @param image Loki's avatar image.
     * @param isError whether the response represents an invalid command.
     * @return the Loki dialog box.
     */
    public static DialogBox getLokiDialog(String message, Image image, boolean isError) {
        DialogBox dialogBox = new DialogBox(message, image);
        dialogBox.flip();
        dialogBox.getStyleClass().add("loki-dialog");
        if (isError) {
            dialogBox.getStyleClass().add("error-dialog");
        }
        return dialogBox;
    }

    /**
     * Flips this dialog box so the avatar appears on the left.
     */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
    }
}
