
package com.example.ds_2;

public class MessageItem {

    private String message;
    private String name;

    public MessageItem() {
    }

    public MessageItem(String name, String message) {
        this.message = message;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void  setMessage(){
        this.message = message;
    }
    public void setName(){
        this.name = name;
    }

}
