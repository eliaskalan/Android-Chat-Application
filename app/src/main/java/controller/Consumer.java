package controller;

import static utils.socketMethods.closeEverything;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ds_2.Chat;
import com.example.ds_2.MessageAdapter;
import com.example.ds_2.MessageItem;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;

import model.MultimediaFile;

public class Consumer implements Serializable {
    private BufferedReader bufferedReader;
    private Socket socket;
    private InputStream inputStream;
    Consumer(Socket socket) throws IOException {
        this.socket = socket;
        try{
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedReader);
        }
    }
    public boolean socketIsConnected(){
        return  this.socket.isConnected();
    }
    public Socket getSocket(){
        return socket;
    }
    public BufferedReader getBufferedReader(){
        return bufferedReader;
    }

    public String readMessage() throws IOException {
        return bufferedReader.readLine();
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        String[] message =msgFromGroupChat.split(":");
                        String name = " ";
                        String context = " ";
                       for(int i=0; i < message.length; i++){
                           if(i == 0){
                               name = message[i];
                           }else{
                               context = message[i];
                           }
                       }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader);
                    }
                }
            }
        }).start();
    }


    public String listenForMessageOneTime() throws IOException {
        String msg;
        msg = bufferedReader.readLine();
        return (String) msg;
    }

    public String printListenForMessageOneTime() throws IOException {
        String topic = listenForMessageOneTime();
        return topic;
    }
}

