package Models;

public class messagesAdapter {

    public String messageText, sender, timeStamp;

    public messagesAdapter() {}

    public messagesAdapter(String messageText, String sender, String timeStamp) {
        this.messageText = messageText;
        this.sender = sender;
        this.timeStamp = timeStamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeStamp() {return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

}
