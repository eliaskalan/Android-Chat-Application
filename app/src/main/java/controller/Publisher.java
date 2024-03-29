package controller;


import static utils.socketMethods.closeEverything;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

import model.MultimediaFile;
import model.ProfileName;
import utils.Config;

public class Publisher implements Serializable {

    private BufferedWriter bufferedWriter;
    private Socket socket;
    ProfileName profileName;
    public static String FILE_TO_SEND = MultimediaFile.FOLDER_SAVE + "new.jpeg";
    Publisher(Socket socket, ProfileName profileName) throws IOException {
        this.socket = socket;
        this.profileName = profileName;
        try{
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ioException) {
            System.out.println("There was a problem in the connection of the client");
            closeEverything(socket, bufferedWriter);
        }
    }


    public void sendMessage(String context) throws IOException {
        if (socket.isConnected()) {
            if (context.equals(Config.EXIT_FROM_TOPIC)) {
                throw new IOException("Go to zookeeper");
            }
            Config.sendAMessage(bufferedWriter, this.profileName.getUserId() + ": " + context);
        }
    }

    public void sendOneTimeMessage(String messageToSend) throws IOException {
        Config.sendAMessage(bufferedWriter, messageToSend);
    }



    public void sendImage() throws IOException {
        DataInputStream dataInputStream=null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;

        if(socket.isConnected()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("File name");
            String file_name = scanner.nextLine();
            FILE_TO_SEND=FILE_TO_SEND+file_name+".jpeg";
            Config.sendAMessage(bufferedWriter,Config.IMAGE_TYPE);


            File myFile = new File(FILE_TO_SEND);
            byte[] mybytearray = new byte[(int) myFile.length()];
            fis = new FileInputStream(myFile);

            bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            os = socket.getOutputStream();

            System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            System.out.println("Done.");
        }

    }

    public void sendVideo(String video_name, String folder_for_chunks) throws Exception{
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        //        while(socket.isConnected()) {

        MultimediaFile mf = new MultimediaFile();
        mf.SplitFile(video_name, folder_for_chunks);

        File splitFiles = new File(folder_for_chunks);
        File[] files = splitFiles.getAbsoluteFile().listFiles();

        try {
            dataOutputStream.write(files.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            try {
                sendChunk(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendChunk(String path) throws Exception{
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        dataOutputStream.writeLong(file.length());

        byte[] buffer = new byte[(int)file.length()];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }

        //        fileInputStream.close();
    }
}