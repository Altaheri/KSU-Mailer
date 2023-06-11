 /*/
  *       Republic of Yemen
  *        Taiz University
  *			  	2011
  *
  * Engineer : Hamdi Taher Altahery
  *
  * Project of : International Language Map -( ILM )-
  *
 /*/
package com.ksu.mailnoti.ui.util;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;


public class CloMaxMin extends JFrame 
{
	private JLabel closeMaxMinJLabel ;
	public JButton closeJButt;
	public JButton maximizeJButt;
	public JButton minimizeJButt;
/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
/*******************************************************************************/
	public CloMaxMin(java.awt.event.ActionListener actionListener)
	{
/*******************************/
		setDefaultLookAndFeelDecorated(true);
    	setUndecorated(true);
		getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.NONE);
		setSize(78+4,31+4);
		setLocation(5,5);
		setLayout(null);
		setAlwaysOnTop(true);
/*******************************/
		getRootPane().setBorder(new CompoundBorder(
        				new LineBorder(
        					new Color(255, 153, 0), 1, true), 
								new CompoundBorder(
									new LineBorder(
										new Color(0, 0, 0), 1, true), 
											null)));
/*******************************/
		closeMaxMinJLabel = new JLabel(new ImageIcon("icons/closeMaxMin/CloMaxMin.jpg"));
		closeMaxMinJLabel.setBounds(0,0,78,31);
		add(closeMaxMinJLabel);
/*******************************/
		minimizeJButt=new JButton(new ImageIcon("icons/closeMaxMin/minimize.jpg"));
		maximizeJButt=new JButton(new ImageIcon("icons/closeMaxMin/maximize.jpg"));
		closeJButt=new JButton(new ImageIcon("icons/closeMaxMin/close.jpg"));
/*******************************/
		minimizeJButt.setRolloverIcon(new ImageIcon("icons/closeMaxMin/minimizeA2.jpg"));
		maximizeJButt.setRolloverIcon(new ImageIcon("icons/closeMaxMin/maximizeA2.jpg"));
		closeJButt.setRolloverIcon(new ImageIcon("icons/closeMaxMin/closeA2.jpg"));
/*******************************/
		minimizeJButt.setToolTipText("�����");
		maximizeJButt.setToolTipText("�������");
		closeJButt.setToolTipText("����� ����");
/*******************************/
		minimizeJButt.setBorder(null);
		maximizeJButt.setBorder(null);
		closeJButt.setBorder(null);
/*******************************/
		minimizeJButt.setBounds(7,8,19,17);
		maximizeJButt.setBounds(30,8,19,17);
		closeJButt.setBounds(53,8,19,17);
/*******************************/
		minimizeJButt.addActionListener(actionListener);
		maximizeJButt.addActionListener(actionListener);
		closeJButt.addActionListener(actionListener);
/*******************************/
		getContentPane().add(minimizeJButt);
		getContentPane().add(maximizeJButt);
		getContentPane().add(closeJButt);
    }
/*******************************************************************************/
/*--------------------------->( End  Constructor )<----------------------------*/
/*******************************************************************************/

   public static void main(String ar[])
    {
    	new CloMaxMin(null).setVisible(true);
    }
}
