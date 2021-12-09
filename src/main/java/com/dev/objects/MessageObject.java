package com.dev.objects;

import java.util.Date;

public class MessageObject {

    private int senderUsername;
    private int receiverUsername;
    private String title;
    private String content;
    private boolean isRead;
    private int MessageId;
    private Date sendDate;


    public Date getSendDate() {return sendDate;}
    public void setSendDate(Date sendDate) {this.sendDate = sendDate;}

    public int getMessageId() {return MessageId;}
    public void setMessageId(int messageId) {MessageId = messageId;}

    public boolean getIsRead() {return isRead;}
    public void setIsRead(boolean read) {isRead = read;}

    public int getSenderUsername() {return senderUsername;}
    public void setSenderUsername(int senderUsername) {this.senderUsername = senderUsername;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getReceiverUsername() {return receiverUsername;}
    public void setReceiverUsername(int receiverUsername) {this.receiverUsername = receiverUsername;}
}
