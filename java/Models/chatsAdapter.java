package Models;

public class chatsAdapter {

    public String messageText, receiver, sender, timeStamp;

    public chatsAdapter() {}

    public chatsAdapter(String messageText, String receiver, String sender, String timeStamp) {
        this.messageText = messageText;
        this.receiver = receiver;
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

    public String getReceiver(){
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimeStamp() {return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

}
