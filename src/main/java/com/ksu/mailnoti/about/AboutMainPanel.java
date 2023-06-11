/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.about;
import com.ksu.mailnoti.ui.util.ToolBar;
/**
 *
 * @author Hamdi
 */
public class AboutMainPanel extends javax.swing.JPanel{

    public AboutPanel settingPanel;
    public ToolBar toolBar;
/*****************************************************************/
	
/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public AboutMainPanel(){
	setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
	settingPanel = new AboutPanel();
	toolBar = new ToolBar();
	//initToolBar();
	//add(toolBar);
	add(settingPanel);
    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/
    
/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
    public void initToolBar(){
	toolBar.initSections(3);
	String icons[] = {"icons/TBarIcons/RDt/Search.png", "icons/TBarIcons/RDt/ShowAll.jpg", "icons/TBarIcons/RDt/RoundShow.jpg"};
	String mOverIcons[] = {"icons/closeMaxMin/minimizeA2.jpg", "icons/closeMaxMin/maximizeA2.jpg", "icons/closeMaxMin/closeA2.jpg"};
	String ToolTipText[] = {"click", "click", "click"};
	toolBar.createButtons(toolBar.RADIO_BUTTON, 0, icons[0], mOverIcons[0], ToolTipText[0]);
	toolBar.createButtons(toolBar.RADIO_BUTTON, 1, icons[1], mOverIcons[1], ToolTipText[1]);
	toolBar.createButtons(toolBar.RADIO_BUTTON, 2, icons[2], mOverIcons[2], ToolTipText[2]);
    }
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/    
    
}
