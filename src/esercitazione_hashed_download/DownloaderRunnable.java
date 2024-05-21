/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download;

import esercitazione_hashed_download.grafica.InterfacciaDownloader;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.nio.file.*;
import java.nio.charset.*;
/**
 *
 * @author acer
 */
public class DownloaderRunnable implements Runnable{
    private ServerSocket downloaderSocket;
    private Socket uploaderSocket;

    private InterfacciaDownloader downloaderIntrf;
    
    private int portDownloader;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private String usedAlgorithm;
    
    public DownloaderRunnable(int port, InterfacciaDownloader downloaderIntrf) {
        
        portDownloader = port;
        this.downloaderIntrf = downloaderIntrf;
      
    }
    
    @Override
    public void run() {
        
        try {
            
            downloaderSocket = new ServerSocket(portDownloader);  

            JOptionPane.showMessageDialog(null, "Connessione aperta!\n");

            uploaderSocket = downloaderSocket.accept();
            JOptionPane.showMessageDialog(null, "Connessione stabilita!\nIP: " + uploaderSocket.getInetAddress().getHostAddress() + "\nPorta: " + uploaderSocket.getPort());
            
            inputStream = uploaderSocket.getInputStream();
            outputStream = uploaderSocket.getOutputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            while (true) {
                try {
                    String fileBytes = (String) objectInputStream.readObject();
                    if (fileBytes == null) {
                        JOptionPane.showMessageDialog(null, "Nessun file ricevuto!", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    } else {
                        receiveMessages(fileBytes, objectInputStream);
                    }
                    downloaderSocket.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            
        } catch (Exception ex) {
            try {
                downloaderSocket.close();
            } catch (IOException ioex) {
                
            }
        }
        
    }
    
    private void receiveMessages(String fileBytes, ObjectInputStream objectInputStream) throws Exception {
        
        Path receivedFilePath = Paths.get(fileBytes);
        File receivedFile = receivedFilePath.toFile();

        String content = (String) objectInputStream.readObject(); // RICEVE IL CONTENUTO DEL FILE DALLA SOCKET
        
        String algorithm = (String) objectInputStream.readObject(); // RICEVE L'ALGORITMO DALLA SOCKET

        String calculatedHash = UploaderRunnable.calculateHash(receivedFile, algorithm);
        
        String hashReceived = (String) objectInputStream.readObject();

        String status = "";
        if (hashReceived.equalsIgnoreCase(calculatedHash)) {
            status = "INTEGRO";
        } else {
            status = "CORROTTO";
        }

        String[] values = {uploaderSocket.getInetAddress().getHostAddress() + ":" + uploaderSocket.getPort() ,receivedFile.getName(), calculatedHash, hashReceived, status};

        downloaderIntrf.modelTable.addRow(values);
    }
    
}