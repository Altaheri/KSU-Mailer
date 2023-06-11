/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.start;
//import com.ksu.mailnoti.ui.util.ToolBar;
/**
 *
 * @author Hamdi
 */
public class StartMainPanel extends javax.swing.JPanel{

    public StartPanel startPanel;
    public ToolBar toolBar;
/*****************************************************************/
	
/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public StartMainPanel(){
	setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        startPanel = new StartPanel();	
        toolBar = new ToolBar(startPanel);

        add(toolBar);
	add(startPanel);
    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/   
}
