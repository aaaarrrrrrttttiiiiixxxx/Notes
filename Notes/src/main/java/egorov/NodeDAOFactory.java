package egorov;

public class NodeDAOFactory {
    private static final NoteDAO noteDAO = new NoteDAO();

    public static NoteDAO getNoteDAO(){
        return noteDAO;
    }
}
