package egorov;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainController {
    private final NoteDAO noteDAO = NodeDAOFactory.getNoteDAO();

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane notesPane;

    @FXML
    private Button newNoteButton;

    @FXML
    protected void addNewNote(ActionEvent event) throws IOException {
        noteDAO.setChosenNoteInd(-1);
        changeScene(event);
    }

    private void changeScene(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("editor.fxml")));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    private void buttonSettings(Button button, String text){
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setText(text);
    }

    private void addRowToNotesPane(Note note){
        int rowInd = notesPane.getRowConstraints().size();
        notesPane.getRowConstraints().add(new RowConstraints(100));
        notesPane.getRowConstraints().get(rowInd).setMaxHeight(100);

        Button topicButton = new Button();
        buttonSettings(topicButton, note.getTopic());
        topicButton.setOnAction(event -> {
            noteDAO.setChosenNoteInd(rowInd);
            try {
                changeScene(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button bodyButton = new Button();
        buttonSettings(bodyButton, note.getBodyPreview());
        bodyButton.setOnAction(event -> {
            noteDAO.setChosenNoteInd(rowInd);
            try {
                changeScene(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteButton = new Button();
        buttonSettings(deleteButton, "delete");
        deleteButton.setOnMouseClicked(mouseEvent -> {
            noteDAO.delete(rowInd);
            notesPane.getChildren().remove(rowInd, 0);
            notesPane.getChildren().remove(rowInd, 1);
            notesPane.getChildren().remove(rowInd, 2);
            notesPane.getRowConstraints().remove(rowInd);
            //есть баг
        });

        notesPane.add(topicButton, 0, rowInd);
        notesPane.add(bodyButton, 1, rowInd);
        notesPane.add(deleteButton, 2, rowInd);
    }

    @FXML
    private void initialize(){
        List<Note> notes = noteDAO.getNotes();

        newNoteButton.setMaxWidth(Double.MAX_VALUE);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);

        notesPane.getColumnConstraints().get(0).setPercentWidth(30);
        notesPane.getColumnConstraints().get(1).setPercentWidth(50);
        notesPane.getColumnConstraints().get(2).setPercentWidth(20);

        for (Note note : notes) {
            addRowToNotesPane(note);
        }
    }
}