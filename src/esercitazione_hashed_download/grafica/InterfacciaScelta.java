/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download.grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Tommaso Melani
 */
public class InterfacciaScelta extends JFrame implements ActionListener {
    
    private Container container = this.getContentPane();
    
    private JPanel centralPanel = new JPanel();
    private JButton uploaderButton = new JButton("Uploader");
    private JButton downloaderButton = new JButton("Downloader");
    
    private JPanel topPanel = new JPanel();
    private JLabel titleTopLabel = new JLabel("Hashed Download");
    
    public InterfacciaScelta(String title) {
        
        this.setTitle(title);
        this.setBounds(200,200,400,200);

        uploaderButton.addActionListener(this);
        downloaderButton.addActionListener(this);
        
        topPanel.setBackground(Color.LIGHT_GRAY);
        titleTopLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        
        centralPanel.add(uploaderButton);
        centralPanel.add(downloaderButton);
        topPanel.add(titleTopLabel);
        
        container.add(topPanel, BorderLayout.NORTH);
        container.add(centralPanel, BorderLayout.CENTER);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == uploaderButton) {
            InterfacciaUploader iup = new InterfacciaUploader("Uploader");
            setVisible(false);
        }
        
        if (e.getSource() == downloaderButton) {
            InterfacciaDownloader idwn = new InterfacciaDownloader("Downloader");
            setVisible(false);
        }
        
    }
    
    
}
