/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package esercitazione_hashed_download.grafica;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Tommaso Melani
 */
public class InterfacciaUploader extends JFrame implements ActionListener {
    
    private Container container = this.getContentPane();
    
    private JPanel centralPanel = new JPanel();
    
    private JPanel topPanel = new JPanel();
    private JLabel titleTopLabel = new JLabel("Uploader");
    
    public InterfacciaUploader(String title) {
        
        this.setTitle(title);
        this.setBounds(300,100,800,600);
        
        topPanel.setBackground(Color.LIGHT_GRAY);
        titleTopLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        
        topPanel.add(titleTopLabel);
        
        container.add(topPanel, BorderLayout.NORTH);
        container.add(centralPanel, BorderLayout.CENTER);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
    }
    
}
