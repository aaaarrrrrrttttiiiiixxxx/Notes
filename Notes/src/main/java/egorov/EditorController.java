package egorov;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EditorController {
    private final NoteDAO noteDAO = NodeDAOFactory.getNoteDAO();

    @FXML
    private TextField topic;

    @FXML
    private TextArea body;

    @FXML
    protected void save(ActionEvent event) throws IOException {
        if(noteDAO.getChosenNoteInd() == -1)
            noteDAO.add(new Note(topic.getText(), body.getText()));
        else
            noteDAO.update(noteDAO.getChosenNoteInd(), new Note(topic.getText(), body.getText()));

        Parent tableViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    @FXML
    private void initialize(){
        if(noteDAO.getChosenNoteInd() == -1)
            return;
        Note note = noteDAO.getChosenNote();
        topic.setText(note.getTopic());
        body.setText(note.getBody());
    }
}
