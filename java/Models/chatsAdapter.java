package Models;

public class chatsAdapter {

    public String messageText, user, timeStamp;

    public chatsAdapter() {}

    public chatsAdapter(String messageText, String user, String timeStamp) {
        this.messageText = messageText;
        this.user = user;
        this.timeStamp = timeStamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimeStamp() {return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

}
