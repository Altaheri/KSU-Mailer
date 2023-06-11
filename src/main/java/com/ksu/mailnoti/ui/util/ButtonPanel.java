/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.ui.util;

/**
 *
 * @author Hamdi
 */
    public class ButtonPanel extends javax.swing.JPanel
    {
        public javax.swing.JButton camButton = new javax.swing.JButton("Activate Camera");
        public javax.swing.JButton penButton = new javax.swing.JButton("Activate OpticalPen");

        public ButtonPanel(java.awt.event.ActionListener actionListener){
            setMaximumSize(new java.awt.Dimension(170 , 24+6));
            setPreferredSize(new java.awt.Dimension(160 , 24+6));
            setLayout(new java.awt.GridLayout(1, 2,5,0));
            setBackground(new java.awt.Color(255, 251, 230,100));
            setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.MatteBorder(
                            new java.awt.Insets(1, 0, 0, 0), 
                                    new java.awt.Color(0, 0, 0)), 
                                                    new javax.swing.border.CompoundBorder(
                                                            new javax.swing.border.LineBorder(
                                                                    new java.awt.Color(0, 0, 0), 1, true), 
                                                                            null)));

            camButton.setIcon(new javax.swing.ImageIcon("icons/camera/camera0.png"));
            penButton.setIcon(new javax.swing.ImageIcon("icons/camera/IRISPen0.png"));
            camButton.setFont(new java.awt.Font("Serif",java.awt.Font.BOLD,15));
            penButton.setFont(new java.awt.Font("Serif",java.awt.Font.BOLD,15));
            
            camButton.setToolTipText("Click to activate Camera");
            penButton.setToolTipText("Click to Activate Optical Pen");

            camButton.setBackground(new java.awt.Color(150, 200, 255));
            penButton.setBackground(new java.awt.Color(150, 200, 255));
            camButton.addActionListener(actionListener);
            penButton.addActionListener(actionListener);
            //pause.setEnabled(false);

            add(camButton);
            add(penButton);
        }
    }
