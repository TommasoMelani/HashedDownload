/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download.grafica;

import esercitazione_hashed_download.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 *
 * @author Tommaso Melani
 */
public class InterfacciaUploader extends JFrame implements ActionListener {
    
    private File selectedFile;
    
    private Container container = this.getContentPane();
    
    private JPanel ipPanel = new JPanel();
    private JLabel ipLabel = new JLabel("IP:");
    private JTextField ipTextField = new JTextField();
    private JLabel portLabel = new JLabel("Porta:");
    private JTextField portTextField = new JTextField();
    private JButton connectButton = new JButton("Connettiti");
    
    private JPanel centralPanel = new JPanel();
    private JButton fileChooserButton = new JButton("Scegli File");
    private JComboBox hashComboBox = new JComboBox();
    public JButton sendButton = new JButton("Invia");
    
    private JPanel topPanel = new JPanel();
    private JLabel titleTopLabel = new JLabel("Uploader");
    
    private JPanel flowPanel = new JPanel();
    
    private String hashedString;
    
    private String usedAlgorithm;
    
    public InterfacciaUploader(String title) {
        
        this.setTitle(title);
        this.setBounds(300,100,800,600);
        
        flowPanel.setLayout(new FlowLayout());
        
        centralPanel.setPreferredSize(new Dimension(800, 100));
        ipPanel.setPreferredSize(new Dimension(800, 100));
        
        topPanel.setBackground(Color.LIGHT_GRAY);
        titleTopLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        
        sendButton.addActionListener(this);
        fileChooserButton.addActionListener(this);
        connectButton.addActionListener(this);
        
        ipTextField.setPreferredSize(new Dimension(100, 25));
        portTextField.setPreferredSize(new Dimension(100, 25));
        
        hashComboBox.addItem("MD5");
        hashComboBox.addItem("SHA-1");
        
        topPanel.add(titleTopLabel);
        
        ipPanel.add(ipLabel);
        ipPanel.add(ipTextField);
        ipPanel.add(Box.createRigidArea(new Dimension(100,0)));
        ipPanel.add(portLabel);
        ipPanel.add(portTextField);
        ipPanel.add(Box.createRigidArea(new Dimension(20,0)));
        ipPanel.add(connectButton);
        
        centralPanel.add(fileChooserButton);
        centralPanel.add(hashComboBox);
        centralPanel.add(Box.createRigidArea(new Dimension(100,0)));
        centralPanel.add(sendButton);
        
        flowPanel.add(ipPanel);
        flowPanel.add(centralPanel);
        
        container.add(topPanel, BorderLayout.NORTH);
        container.add(flowPanel);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == fileChooserButton) {
            
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                
                if (selectedFile == null) {
                    JOptionPane.showMessageDialog(this, "Nessun file selezionato!", "ERRORE: File non trovato", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                usedAlgorithm = (String) hashComboBox.getSelectedItem();

                hashedString = UploaderRunnable.calculateHash(selectedFile, usedAlgorithm);
                System.out.println(selectedFile + " / HASH: " + hashedString);
                
            } else {
                JOptionPane.showMessageDialog(this, "Nessun file selezionato!", "ERRORE: File non trovato", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (e.getSource() == connectButton) {
            try {
                String ip = ipTextField.getText();
                int port = Integer.parseInt(portTextField.getText());
                Thread uploaderThread = new Thread(new UploaderRunnable(ip, port, this, selectedFile, usedAlgorithm));
                uploaderThread.start();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Controlla i campi per la connessione!\n" + ex.getLocalizedMessage());
            }
        }
        
        
    }
    
            
    
}
