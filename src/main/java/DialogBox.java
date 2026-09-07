import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Displays one user or Loki message with its avatar. */
public class DialogBox extends HBox {
    private final Label text;
    private final ImageView displayPicture;

    /**
     * Creates a dialog box containing text and an avatar.
     *
     * @param message the message to display.
     * @param image the avatar image to display.
     */
    public DialogBox(String message, Image image) {
        text = new Label(message);
        displayPicture = new ImageView(image);
        text.setWrapText(true);
        text.setMaxWidth(280.0);
        displayPicture.setFitWidth(40.0);
        displayPicture.setFitHeight(40.0);
        setAlignment(Pos.TOP_RIGHT);
        setSpacing(8.0);
        getChildren().addAll(text, displayPicture);
    }

    /**
     * Creates a dialog box aligned as a user message.
     *
     * @param message the user's message.
     * @param image the user's avatar image.
     * @return the user dialog box.
     */
    public static DialogBox getUserDialog(String message, Image image) {
        return new DialogBox(message, image);
    }

    /**
     * Creates a dialog box aligned as a Loki response.
     *
     * @param message Loki's response.
     * @param image Loki's avatar image.
     * @return the Loki dialog box.
     */
    public static DialogBox getLokiDialog(String message, Image image) {
        DialogBox dialogBox = new DialogBox(message, image);
        dialogBox.flip();
        return dialogBox;
    }

    /** Flips this dialog box so its avatar appears on the left. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(children);
        getChildren().setAll(children);
    }
}
