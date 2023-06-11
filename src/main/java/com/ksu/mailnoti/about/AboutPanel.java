/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.about;

import com.ksu.mailnoti.ui.util.BorderPanel;


/**
 *
 * @author Hamdi
 */
public class AboutPanel extends BorderPanel{
    protected javax.swing.ImageIcon bgImageI = new javax.swing.ImageIcon("icons/Default.jpg");
    protected javax.swing.ImageIcon aboutLogo = new javax.swing.ImageIcon("icons/CS2R.png");

/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public AboutPanel(){
	setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
	setBorder(
            new javax.swing.border.CompoundBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(5, 5, 5, 5), new java.awt.Color(255,255,255,0)),
            new javax.swing.border.MatteBorder(new java.awt.Insets(10, 10, 10, 10), new java.awt.Color(255,255,255,0))));
        setImage(aboutLogo);
    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/
    
    @Override
    public void  paintComponent(java.awt.Graphics g){
        super.paintComponent(g);
 	g.drawImage(bgImageI.getImage(), 0, 0, getWidth(), getHeight(), 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
	g.setColor(new java.awt.Color(255, 255, 255, 160));
	g.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 12, 12);
    }
}




