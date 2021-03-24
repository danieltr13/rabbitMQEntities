/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.enviocolas;

import com.mycompany.dominio.Persona;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author MSI GF63
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
             //System.out.println(" [x] Sent '" + message + "'");
            for (int i = 0; i < 10; i++) {
                Persona p= new Persona("Juan", i+"");
                System.out.println(p);
                //channel.basicPublish("", QUEUE_NAME, null, message.getBytes()); 
                byte[] n= convertToBytes(p);
                channel.basicPublish("", QUEUE_NAME, null, n);  
                Thread.sleep(1000);
            }
        }
    }
    
    private static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        } 
    }
    
    private static Persona convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Persona) in.readObject();
        }
    }
}
