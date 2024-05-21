/*
package esercitazione_hashed_download;


 //@author Samuele Esposito
 

public class UploaderRunnable {
    
}
*/
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download;

import esercitazione_hashed_download.grafica.InterfacciaUploader;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.nio.file.*;
import java.util.*;


/**
 *
 * @author acer
 */
public class UploaderRunnable implements Runnable {


    private Socket uploaderSocket;
    
    private InterfacciaUploader uploaderInterf;
    
    private String ipServer;
    private int portServer;
    private JButton uploaderSendButton;
    private JTextArea uploaderInOutArea;

    private final File selectedFile;
    
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private String usedAlgorithm;
    
    public UploaderRunnable(String ip, int port, InterfacciaUploader uploaderInterf, File file, String algorithm) {
        ipServer = ip;
        portServer = port;
        selectedFile = file;
        this.uploaderInterf = uploaderInterf;
        uploaderSendButton = uploaderInterf.sendButton;
        usedAlgorithm = algorithm;
        
        uploaderSendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    if (!uploaderInterf.corruptedButton.isSelected()) {
                        try {
                            DataInputStream dataInputStream = new DataInputStream(uploaderSocket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(uploaderSocket.getOutputStream());
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                            String fileBytes = selectedFile.toPath().toString();
                            System.out.println("SENDING FILE: " + fileBytes);
                            objectOutputStream.writeObject(fileBytes); // INVIA IL FILE NELLA SOCKET
                            objectOutputStream.flush();

                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
                            StringBuilder content = new StringBuilder();
                            String line;

                            while ((line = reader.readLine()) != null) {
                                content.append(line);
                                content.append(System.lineSeparator());
                            }
                            String contentFileBytes = content.toString();
                            objectOutputStream.writeObject(contentFileBytes); // INVIA IL CONTENUTO DEL FILE NELLA SOCKET
                            objectOutputStream.flush();

                            System.out.println("SENDING ALGORITHM: " + usedAlgorithm);
                            objectOutputStream.writeObject(usedAlgorithm); // INVIA L'ALGORITMO NELLA SOCKET
                            objectOutputStream.flush();

                            String fileHashed =  UploaderRunnable.calculateHash(selectedFile, usedAlgorithm);
                            System.out.println("SENDING PERSONAL HASH: " + fileHashed);
                            objectOutputStream.writeObject(fileHashed);
                            objectOutputStream.flush();

                        
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Errore!\n" + ex.getLocalizedMessage());
                        }
                    } else {
                        
                        try {
                            DataInputStream dataInputStream = new DataInputStream(uploaderSocket.getInputStream());
                            DataOutputStream dataOutputStream = new DataOutputStream(uploaderSocket.getOutputStream());
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                            Path fileBytes = selectedFile.toPath();
                            File file = fileBytes.toFile();
                            File corruptedFile = generateCorruptedFile(file);
                            
                            String corruptedFileBytes = corruptedFile.toPath().toString();
                            System.out.println("SENDING FILE: " + corruptedFileBytes);
                            objectOutputStream.writeObject(corruptedFileBytes); // INVIA IL FILE NELLA SOCKET
                            objectOutputStream.flush();

                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()));
                            StringBuilder content = new StringBuilder();
                            String line;

                            while ((line = reader.readLine()) != null) {
                                content.append(line);
                                content.append(System.lineSeparator());
                            }
                            String contentFileBytes = content.toString();
                            objectOutputStream.writeObject(contentFileBytes); // INVIA IL CONTENUTO DEL FILE NELLA SOCKET
                            objectOutputStream.flush();

                            System.out.println("SENDING ALGORITHM: " + usedAlgorithm);
                            objectOutputStream.writeObject(usedAlgorithm); // INVIA L'ALGORITMO NELLA SOCKET
                            objectOutputStream.flush();

                            String fileHashed =  UploaderRunnable.calculateHash(selectedFile, usedAlgorithm);
                            System.out.println("SENDING PERSONAL HASH: " + fileHashed);
                            objectOutputStream.writeObject(fileHashed);
                            objectOutputStream.flush();

                        
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Errore!\n" + ex.getLocalizedMessage());
                        }
                        
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Errore!\nNessun file trovato!\nControlla di aver selezionato il file prima di effettuare la connessione!");
                }
            }
        });
        
    }
    
    @Override
    public void run() {
        try {
            
            
            uploaderSocket = new Socket(ipServer, portServer);
            
            inputStream = uploaderSocket.getInputStream();
            outputStream = uploaderSocket.getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(uploaderSocket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(uploaderSocket.getOutputStream());
            
            JOptionPane.showMessageDialog(null, "Connessione stabilita!\n");
            
        } catch (Exception ex) {
            if (ex instanceof NullPointerException) {
                try {
                    uploaderSocket.close();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Connessione interrotta:\n" + ex + "\nRIGA 142 UploaderRunnable");
            }
        }
    }
    
    public static String calculateHash(File selectedFile, String algorithmUsed) {
        
        try {
            MessageDigest algorithmDig = MessageDigest.getInstance(algorithmUsed);
            
            byte[] fileBytes = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
            byte[] hashedBytes = algorithmDig.digest(fileBytes);
            String hashedString = Base64.getEncoder().encodeToString(hashedBytes);
            return hashedString;
            
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getLocalizedMessage());
        } catch (IOException ex) {
           ex.printStackTrace();
           
        }
        return "null";
    }
    
    private File generateCorruptedFile(File file) {
        
        File newFile = new File(file.getParentFile().getAbsolutePath() + "/corrupted_" + file.getName());
        
        try {
            
            int length = 1000;
            String randomChars = randomCharsGenerated(length);
            
            List<String> lines = Files.readAllLines(newFile.toPath());
            StringBuilder content = new StringBuilder();
            for (String line : lines) {
                content.append(line).append("\n");
            }
            
            int insertPosition = new Random().nextInt(content.length());
            content.insert(insertPosition, randomChars);
            
            Files.write(newFile.toPath(), content.toString().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            
            return newFile;
            
        } catch (Exception ex) {
            
        }
        
        return newFile;
    }
    
    private String randomCharsGenerated(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    
    }
    
}