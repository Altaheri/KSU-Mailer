 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui.util;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class ToolBar extends TopPanel{
    public static final int BUTTON = 1;
    public static final int RADIO_BUTTON = 2;
    
    public java.util.ArrayList toolBarSectionsList = new java.util.ArrayList();
//	public java.util.ArrayList toolBarButGroups = new java.util.ArrayList();

/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
	public ToolBar(){
		super(null, null);
	}
	public ToolBar(java.awt.event.ActionListener al, java.awt.event.MouseMotionListener mml){
		super(al, mml);
	}
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
	public void initSections(int num){
		for(int i=0; i<num; i++){
			add(new DescriptionPanel());
			toolBarSectionsList.add(getComponent(i));
		}
	}
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
        public void createButtons(int buttonType, int section, String icons[], String mouseOverIcons[], String ToolTipText[]){
            if(buttonType == RADIO_BUTTON){
                ButtonGroup group = new ButtonGroup();
                for(int i=0; i<icons.length; i++){
                    JRadioButton button = createRadioButton("", icons[i], mouseOverIcons[i],ToolTipText[i] );
                    group.add(button);
                    ((javax.swing.JPanel)toolBarSectionsList.get(section)).add(button);
                }
            }
        }
        public void createButtons(int buttonType, int section, String icons, String mouseOverIcons, String ToolTipText){
            if(buttonType == RADIO_BUTTON){
                JRadioButton button = createRadioButton("", icons, mouseOverIcons,ToolTipText );
                ((javax.swing.JPanel)toolBarSectionsList.get(section)).add(button);
            }
        }
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
	private JRadioButton createRadioButton(String name, String icon, String mouseOverIcon, String ToolTipText){
		JRadioButton button = new JRadioButton("Play");
                //JRadioButton button = new JRadioButton();
		if(icon!=null)  button.setIcon(new javax.swing.ImageIcon(icon));
//		button.setPressedIcon(new javax.swing.ImageIcon(mouseOverIcon));
//		button.setRolloverIcon(new javax.swing.ImageIcon(mouseOverIcon));
//		button.setRolloverSelectedIcon(new javax.swing.ImageIcon(mouseOverIcon));
//		button.setSelectedIcon(new javax.swing.ImageIcon(mouseOverIcon));
		button.setToolTipText(ToolTipText);
		//button.setBorder(
		//	new javax.swing.border.CompoundBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(4, 4, 4, 4), new java.awt.Color(174, 212, 249)),null));
		button.setBorder(null);
		//button.setBounds(7,8,19,17);
//		button.addActionListener(actionListener);
		return button;
	}
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/
}
