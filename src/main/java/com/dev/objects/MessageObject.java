package com.dev.objects;

import java.util.Date;

public class MessageObject {

    private int senderId;
    private String receiverUsername;
    private String title;
    private String content;
    private boolean isRead;
    private int messageId;
    private Date sendDate;


    public Date getSendDate() {return sendDate;}
    public void setSendDate(Date sendDate) {this.sendDate = sendDate;}

    public int getMessageId() {return messageId;}
    public void setMessageId(int messageId) {messageId = messageId;}

    public boolean getIsRead() {return isRead;}
    public void setIsRead(boolean read) {isRead = read;}

    public int getSenderId() {return senderId;}
    public void setSenderId(int senderId) {this.senderId = senderId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiverUsername() {return receiverUsername;}
    public void setReceiverUsername(String receiverUsername) {this.receiverUsername = receiverUsername;}
}
