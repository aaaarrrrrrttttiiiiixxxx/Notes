package egorov;

public class Note {
    private String topic;
    private final String body;

    public Note(String topic, String body) {
        this.topic = topic;
        this.body = body;
    }

    public Note() {
        this.topic = "";
        this.body = "";
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public String getBodyPreview() {
        if(body.length() > 100)
            return body.substring(0, 100) + " ...";
        return body;
    }
}
