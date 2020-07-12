package com.example.garagesale.model;

public class LastChatModel
{
    String DisplayName,Sender_id,Text,Seen,type,Date,key,otheruserId,otherName;

    public LastChatModel(String displayName, String sender_id, String text, String seen, String type, String date, String key,String otheruserId,String otherName)
    {
        DisplayName = displayName;
        Sender_id = sender_id;
        Text = text;
        Seen = seen;
        this.type = type;
        Date = date;
        this.key = key;
        this.otheruserId = otheruserId;
        this.otherName = otherName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtheruserId() {
        return otheruserId;
    }

    public void setOtheruserId(String otheruserId) {
        this.otheruserId = otheruserId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getSender_id() {
        return Sender_id;
    }

    public void setSender_id(String sender_id) {
        Sender_id = sender_id;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getSeen() {
        return Seen;
    }

    public void setSeen(String seen) {
        Seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
