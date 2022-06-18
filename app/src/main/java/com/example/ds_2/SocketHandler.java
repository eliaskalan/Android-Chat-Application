package com.example.ds_2;

import java.io.IOException;
import java.net.Socket;

public class SocketHandler {
    private static Socket socket;

    public  synchronized Socket getSocket(){
        return SocketHandler.socket;
    }

    public  synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }

}
