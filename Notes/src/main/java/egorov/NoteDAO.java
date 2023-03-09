package egorov;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteDAO {
    private int chosenNoteInd = -1;
    private final Path PATH = Paths.get("src\\main\\resources\\Notes");
    private List<String> notesPaths;
    private final List<Note> notes = new LinkedList<>();

    public NoteDAO() {
        notesPaths = new LinkedList<>();
        try {
            notesPaths = Files.find(PATH, 1, (p, a) -> a.isRegularFile() && p.getFileName().toString().endsWith(".txt")).map(Path::toString).collect(Collectors.toList());
            for (String path: notesPaths) {
                String body = readFile(Path.of(path));
                String topic = path.replaceAll(".txt", "").substring(25);
                notes.add(new Note(topic, body));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(Path path) {
        StringBuilder res = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path.toString()));

            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res.toString();
    }

    public int getChosenNoteInd() {
        return chosenNoteInd;
    }

    public void setChosenNoteInd(int chosenNoteInd) {
        this.chosenNoteInd = chosenNoteInd;
    }

    public Note getChosenNote(){
        return notes.get(chosenNoteInd);
    }

    public List<Note> getNotes(){
        return notes;
    }

    public int add(Note note) {
        note.setTopic(createAndFillNewFile(note));
        notes.add(note);
        notesPaths.add(PATH + "\\" + note.getTopic() + ".txt");
        return notes.size() - 1;
    }

    private String createAndFillNewFile(Note note) {
        String topic = note.getTopic();
        try {
            File newFile = new File(PATH + "\\" + topic + ".txt");
            int counter = 1;
            while (newFile.exists()) {
                newFile = new File(PATH + "\\"+ topic + counter + ".txt");
                counter++;
            }
            newFile.createNewFile();
            if(counter > 1)
                topic += counter - 1;

            FileWriter writer = new FileWriter(PATH + "\\" + topic + ".txt");
            writer.write(note.getBody());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return topic;
    }

    public void delete(int ind) {
        try {
            Files.delete(Path.of(notesPaths.get(ind)));
            notes.remove(ind);
            notesPaths.remove(ind);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int ind, Note note) {
        try {
            notes.set(ind, note);
            Files.delete(Path.of(notesPaths.get(ind)));
            File newFile = new File(notesPaths.get(ind));
            newFile.createNewFile();
            FileWriter writer = new FileWriter(notesPaths.get(ind));
            writer.write(note.getBody());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}