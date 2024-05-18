/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download.grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import esercitazione_hashed_download.DownloaderRunnable;

/**
 *
 * @author Tommaso Melani
 */
public class InterfacciaDownloader extends JFrame implements ActionListener {

    private Container container = this.getContentPane();
    
    private JPanel ipPanel = new JPanel();
    private JButton ipGetButton = new JButton("Vedi IP");
    private JLabel ipLabel = new JLabel("IP:");
    private JTextField ipTextField = new JTextField();
    private JLabel portLabel = new JLabel("Porta:");
    private JTextField portTextField = new JTextField();
    private JButton openConnectionButton = new JButton("Apri connessione");
    
    private JPanel centralPanel = new JPanel();
    
    private JPanel topPanel = new JPanel();
    private JLabel titleTopLabel = new JLabel("Downloader");
    
    private JPanel flowPanel = new JPanel();
    
    public String ipAddress;
    
    public InterfacciaDownloader(String title) {
        
        this.setTitle(title);
        this.setBounds(100,100,800,600);
        
        flowPanel.setLayout(new FlowLayout());
        
        ipTextField.setEditable(false);
        ipTextField.setPreferredSize(new Dimension(100,25));
        portTextField.setPreferredSize(new Dimension(100,25));
        
        ipGetButton.addActionListener(this);
        openConnectionButton.addActionListener(this);
        
        topPanel.setBackground(Color.LIGHT_GRAY);
        titleTopLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        
        ipPanel.add(ipGetButton);
        ipPanel.add(Box.createRigidArea(new Dimension(20,0)));
        ipPanel.add(ipLabel);
        ipPanel.add(ipTextField);
        ipPanel.add(Box.createRigidArea(new Dimension(100,0)));
        ipPanel.add(portLabel);
        ipPanel.add(portTextField);
        ipPanel.add(Box.createRigidArea(new Dimension(20,0)));
        ipPanel.add(openConnectionButton);
        
        topPanel.add(titleTopLabel);
        
        flowPanel.add(ipPanel);
        flowPanel.add(centralPanel);
        
        container.add(topPanel, BorderLayout.NORTH);
        container.add(flowPanel);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == ipGetButton) {
             
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            ipTextField.setText(ipAddress);
        }
        
        if (e.getSource() == openConnectionButton) {
            
            try {
                int port = Integer.parseInt(portTextField.getText());
                Thread serverThread = new Thread(new DownloaderRunnable(port,this));
                serverThread.start();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Controlla i campi per la connessione!");
            }   
        }
        
    }
    
}
