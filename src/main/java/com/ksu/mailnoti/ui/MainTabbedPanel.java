 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui;
import com.ksu.mailnoti.about.AboutMainPanel;
import com.ksu.mailnoti.start.StartMainPanel;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import java.awt.Color;

public class MainTabbedPanel extends javax.swing.JPanel {
    public static final Color OUTER_BORDER_COLOR = new Color(142, 179, 227);
    public static final Color INNER_BORDER_COLOR = new Color(100, 251, 255);
    private final JTabbedPane jTabbedPane ;
    private final ImageIcon bgImageI = new ImageIcon("icons/menulbarBg2.png");
    
    public StartMainPanel startMainPanel = new StartMainPanel();
    public AboutMainPanel aboutMainPanel = new AboutMainPanel();    
    
    public MainTabbedPanel(){
    	setLayout(new java.awt.GridLayout(0,1));
	setPreferredSize(new java.awt.Dimension(300 ,400));
	setMinimumSize(new java.awt.Dimension(250 ,300));
               
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);                       
    	jTabbedPane.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        jTabbedPane.setFont(new java.awt.Font("Arial", 0, 15));		
	jTabbedPane.setAutoscrolls(true);
        jTabbedPane.setFocusable(false);

        jTabbedPane.addTab(" Start ", startMainPanel);
        jTabbedPane.addTab(" About ", aboutMainPanel);
        
        add(jTabbedPane);
    }
    
    @Override
    public void  paintComponent(java.awt.Graphics g){
        super.paintComponent(g);
 	int hei = getHeight()-jTabbedPane.getComponentAt(0).getHeight();
 	g.drawImage(bgImageI.getImage(), 0, 0, getWidth(), hei, 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
	g.setColor( OUTER_BORDER_COLOR );
	g.drawLine(3, hei-2, getWidth()-4, hei-2);
	g.drawLine(2, hei-1, getWidth()-3, hei-1);
	g.setColor( INNER_BORDER_COLOR );
	g.drawLine(3, hei-1, getWidth()-4, hei-1);
    }
}
