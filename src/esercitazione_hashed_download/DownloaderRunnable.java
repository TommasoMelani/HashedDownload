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
            
            JOptionPane.showMessageDialog(null, "Connessione stabilita!\n");
            
            
            
            uploaderSocket = downloaderSocket.accept();
            System.out.println("CONNESSIONE EFFETTUATA!");
            
            inputStream = uploaderSocket.getInputStream();
            outputStream = uploaderSocket.getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(uploaderSocket.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            String fileBytes = (String) objectInputStream.readObject();
            if (fileBytes == null) {
                JOptionPane.showMessageDialog(null, "Nessun file ricevuto!", "ERRORE", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("FILE RICEVUTO: " + fileBytes);
            Path receivedFilePath = Paths.get(fileBytes);
            File receivedFile = receivedFilePath.toFile();
            System.out.println(receivedFile);
            
            String content = (String) objectInputStream.readObject(); // RICEVE IL CONTENUTO DEL FILE DALLA SOCKET
            System.out.println("CONTENUTO:\n" + content); 
            
            String algorithm = (String) objectInputStream.readObject(); // RICEVE L'ALGORITMO DALLA SOCKET
            System.out.println("ALGORITMO: " + algorithm);
            
            String calculatedHash = UploaderRunnable.calculateHash(receivedFile, algorithm);
            System.out.println("HASH CALCOLATO:" + calculatedHash);
            
            String hashReceived = (String) objectInputStream.readObject();
            System.out.println("HASH RICEVUTO:" + hashReceived);
            
            if (hashReceived.equalsIgnoreCase(calculatedHash)) {
                System.out.println("FILE INTEGRO!");
            } else {
                System.out.println("FILE CORROTTO!");
            }
            
            
            
            
        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                try {
                    downloaderSocket.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Connessione interrotta:\n" + ex);
            }
            
        }
        
        
    }
    
}