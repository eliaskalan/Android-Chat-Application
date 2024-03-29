package controller;

import model.ProfileName;
import utils.Config;

import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;


public class Client {
    public Consumer consumer;
    public Publisher publisher;
    ProfileName profileName;
    private Socket socket;
    private Address address;
    public boolean hasTopic = false;

    public Client(Address address, String name) throws IOException {
        try{
            this.address = address;
            this.socket = new Socket(address.getIp(), address.getPort());
            this.consumer = new Consumer(socket);
            this.profileName = new ProfileName(name);
            this.publisher = new Publisher(socket, profileName);
        }catch (IOException e){
            System.out.println("There was a problem in the connection of the client");
        }
    }

    public  Address getAddress(){
        return address;
    }
    public void closeSocket() throws IOException {
        socket.close();
    }
    public Socket getSocket(){
        return socket;
    }
    public void initialBroker(String topicName) throws IOException {
        this.publisher.sendOneTimeMessage(this.profileName.getProfileName());
        this.publisher.sendOneTimeMessage(this.profileName.getUserId());
        this.publisher.sendOneTimeMessage(topicName);
    }

    public Address getBrokerAddress() throws IOException {
        String ip = this.consumer.listenForMessageOneTime();
        String port = this.consumer.listenForMessageOneTime();
        return new Address(ip, Integer.parseInt(port));
    }

    public void initialConnectWithZookeeper(String username) throws IOException {
        this.publisher.sendOneTimeMessage(username);
        System.out.println("Welcome " + username);

    }
    public String getTopic() throws IOException {
        String topic = this.consumer.printListenForMessageOneTime();
        return topic;

    }
    public void selectTopic(String id) throws IOException {
        //String id = Config.readFromUser("Select the topic you want");
        System.out.println(id);
        //Selects the Topic
        this.publisher.sendOneTimeMessage(id);
        //Returns the TopicName
        String topicName = this.consumer.listenForMessageOneTime();
        System.out.println("If you want to exit from topic write " + Config.EXIT_FROM_TOPIC);
        //return topicName;
    }
}
