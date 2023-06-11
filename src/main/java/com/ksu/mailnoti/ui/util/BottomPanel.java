 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of : International Language Map -( ILM )-
 /*/

package com.ksu.mailnoti.ui.util;
public class BottomPanel extends javax.swing.JPanel{
    protected javax.swing.ImageIcon bgImageI = new javax.swing.ImageIcon("icons/Bottom-bg.png");
	
	public BottomPanel(){
		setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
		//setBackground(new java.awt.Color(68,68,68));
		setMaximumSize(new java.awt.Dimension(5000, 25));
		setPreferredSize(new java.awt.Dimension(600, 25));
		setMinimumSize(new java.awt.Dimension(200, 25));
		setBorder(new javax.swing.border.CompoundBorder(
        			new javax.swing.border.MatteBorder(
        				new java.awt.Insets(0, 0, 0, 0), 
        					//new java.awt.Color(255, 153, 0)), 
        					new java.awt.Color(197, 210, 223)), 
								new javax.swing.border.CompoundBorder(
									new javax.swing.border.LineBorder(
										new java.awt.Color(0, 0, 0), 0, true), 
											null)));
	}
 	public void  paintComponent(java.awt.Graphics g){
 		g.drawImage(bgImageI.getImage(), 0, 0, getWidth(), getHeight(), 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
	}
}
