 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui;

import java.io.File;
import javax.swing.UnsupportedLookAndFeelException;

public class KSUMailerMainFrame extends MainFrame{
    public static MyPainter emptyPainter = new MyPainter(null, MyPainter.TAB);
    public static MyPainter epanelPainter = new MyPainter(null, MyPainter.EPANEL);
    public static MyPainter selectedAndMouseOverPainter = new MyPainter("icons/SelectedAndMouseOverTab.png", MyPainter.TAB);
    public static MyPainter selectedPainter = new MyPainter("icons/SelectedTab.png", MyPainter.TAB_SEL);
    public static MyPainter rbSelectedPainter = new MyPainter("icons/TapBorder1.png", MyPainter.TOOL_BAR_ICON);
    public static MyPainter rbEnabledPainter = new MyPainter(null, MyPainter.TOOL_BAR_ICON);
    public static MyPainter rbEnabledPainter0 = new MyPainter(null, MyPainter.TOOL_BAR_ICON0);
    public static MyPainter toolbarMouseOverPainter = new MyPainter("icons/toolbarIcons1.png", MyPainter.TOOL_BAR_ICON);

    public MainPanel mainPanel;
	
/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public KSUMailerMainFrame(){
        super("KSUMailer");
    }
/*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*//*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/

    public static void main(String[] args) {
    	setLookAndFeel();
    	KSUMailerMainFrame enMainFrame = new KSUMailerMainFrame();
    	enMainFrame.initJframe();
        enMainFrame.maximizeRestore(1);
//    	javax.swing.SwingUtilities.updateComponentTreeUI(enMainFrame);
    }

/*******************************************************************************/
/*------------------------>( Method To : initJframe )<-------------------------*/
    private void initJframe(){
        mainPanel = new MainPanel(null);
        add(mainPanel);
		
        //setDefaultLookAndFeelDecorated(true);
        pack();
        setLocationAtCenter();
        setVisible(true);
		
        pureWid = getWidth()-10 ; pureHei = getHeight()-24-10;
        lastLocation = currentLocation = getLocation();
        lastSize = currentSize = getSize();
    }
/*------------------------->( End initJframe Method )<-------------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
    public static void setLookAndFeel(){
 	String keys[]={"Enabled", "Enabled+Pressed", "Enabled+MouseOver", "Selected", "MouseOver+Selected", "Pressed+Selected"};
	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        	if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    System.out.println(javax.swing.UIManager.getLookAndFeel());

                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Enabled].backgroundPainter", emptyPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Enabled+Pressed].backgroundPainter", emptyPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Enabled+MouseOver].backgroundPainter", emptyPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Pressed+Selected].backgroundPainter", selectedPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Selected].backgroundPainter", selectedPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[MouseOver+Selected].backgroundPainter", selectedAndMouseOverPainter);

                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Pressed+Selected].textForeground", new java.awt.Color(100, 20, 170));
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Selected].textForeground", new java.awt.Color(21, 66, 139));
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[MouseOver+Selected].textForeground", new java.awt.Color(21, 66, 139));
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab[Enabled].textForeground", new java.awt.Color(80, 100, 150));
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane.font", new java.awt.Font("Segoe UI", 0, 11));

                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("TabbedPane:TabbedPaneTab.contentMargins", new java.awt.Insets(3,11,0,11));

                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("RadioButton[Selected].backgroundPainter", toolbarMouseOverPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("RadioButton[MouseOver].backgroundPainter", toolbarMouseOverPainter);
//                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("RadioButton[Enabled].backgroundPainter", rbEnabledPainter);
		
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("EditorPane[Enabled].backgroundPainter",epanelPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("EditorPane[Selected].backgroundPainter",epanelPainter);
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("EditorPane[Disabled].backgroundPainter",epanelPainter);
                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ToolBar:Button[MouseOver].backgroundPainter", toolbarMouseOverPainter);                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ToolBar:Button[Pressed].backgroundPainter", rbEnabledPainter);                          
                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].textForeground", new java.awt.Color(0, 0, 0));                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", new java.awt.Color(153, 204, 255));                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox:\"ComboBox.listRenderer\"[Selected].background", new java.awt.Color(153, 204, 255));                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox[Focused+MouseOver].backgroundPainter", toolbarMouseOverPainter);                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox[Focused+Pressed].backgroundPainter", toolbarMouseOverPainter);                    
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox[Focused].backgroundPainter", rbEnabledPainter0);                                            
                    javax.swing.UIManager.getLookAndFeel().getDefaults().put("ComboBox[Enabled].backgroundPainter", rbEnabledPainter0);                                            
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println ("If Nimbus is not available");
            // If Nimbus is not available, you can set the GUI to another look and feel.
	}
    }
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/

/*******************************************************************************/
    @Override
    public void componentResized(java.awt.event.ComponentEvent comEv){
	super.componentResized(comEv);
    }
    @Override
    public void componentMoved(java.awt.event.ComponentEvent comEv){
	super.componentMoved(comEv);
    }
/*******************************************************************************/
    @Override
    public void windowClosed(java.awt.event.WindowEvent winEv)	{
	super.windowClosed(winEv);
    }
    @Override
    public void windowClosing(java.awt.event.WindowEvent winEv){
	super.windowClosing(winEv);
    }
    @Override
    public void windowIconified(java.awt.event.WindowEvent winEv){
	super.windowIconified(winEv);
    }
    @Override
    public void windowDeiconified(java.awt.event.WindowEvent winEv) {
	super.windowDeiconified(winEv);
    }
/*******************************************************************************/
}


class MyPainter implements javax.swing.Painter{
    private javax.swing.ImageIcon bgImageI ;
    public static final int TOOL_BAR_ICON0 = 0;
    public static final int TOOL_BAR_ICON = 1;
    public static final int TAB = 2;
    public static final int TAB_SEL = 3;
    public static final int EPANEL = 4;
    public int type;
/*******************************************************************************/
    public MyPainter(String image, int type){
	this.type = type;
//        System.out.println( getClass().getResource(""));
//        System.out.println( getClass().getClassLoader().getResource(""));
//        System.out.println( new File(".").getAbsolutePath());        
        
 	if(image!=null){
            bgImageI = new javax.swing.ImageIcon(image);
        }
    }
/*******************************************************************************/
    public void paint(java.awt.Graphics2D g, Object object, int width, int height){
/*    	System.out.println(object);*/ 			
 	if(bgImageI!=null)
            g.drawImage(bgImageI.getImage(), 1, 2, width, height+4, 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
 	if(type==TAB_SEL)
            paintTabBorder(g, width, height);
 	else if(type==TOOL_BAR_ICON)
            paintTBIconBorder(g, width, height);
 	else if(type==EPANEL)
            paintEPBorder(g, width, height);        
 	else if(type==TOOL_BAR_ICON0)
            paintTBIconBorder0(g, width, height);
        //else paintTabBorder(g, width, height);
/* 	if(object.getClass().isInstance(new javax.swing.JTabbedPane())){
//          ((javax.swing.JTabbedPane)object).getSelectedComponent();
            g.drawLine(-100, height, ((javax.swing.JTabbedPane)object).getWidth(), height);
 	}*/
    }
/*******************************************************************************/
    public void paintTabBorder(java.awt.Graphics2D g, int width, int height){
	g.setColor( MainTabbedPanel.OUTER_BORDER_COLOR );
//	g.setColor( new java.awt.Color(255, 0, 0) );
	g.drawLine(1, 2, 1, height);//left
	g.drawLine(width-1, 2, width-1, height);//right
	g.drawLine(3, 0, width-3, 0);//top
	g.drawLine(2, 1, 2, 1);//top left corner
	g.drawLine(width-2, 1, width-2, 1);//top right corner
	g.drawLine(1-2, 2+height, 3-2, 0+height);//bottom left corner
	g.drawLine(width-1, height, width+1, 2+height);//bottom right corner

	g.setColor( MainTabbedPanel.INNER_BORDER_COLOR );
//	g.setColor( new java.awt.Color(0, 0, 255) );
	g.drawLine(1+1, 2, 1+1, height);//left
	g.drawLine(width-1-1, 2, width-1-1, height);//right
	g.drawLine(3, 0+1, width-3, 0+1);//top
	g.drawLine(1-2+1, 2+height, 3-2+1, 0+height);//bottom left corner
	g.drawLine(width-1-1, height, width+1-1, 2+height);//bottom right corner
    }
/*******************************************************************************/
    public void paintTBIconBorder(java.awt.Graphics2D g, int width, int height){
        g.setColor( new java.awt.Color(174, 212, 249) );
	g.drawLine(0, 1, 0, height-2);
	g.drawLine(width-1, 1, width-1, height-2);
	g.drawLine(1, 0, width-2, 0);
	g.drawLine(1, height-1, width-2, height-1);

    }
/*******************************************************************************/
    public void paintEPBorder(java.awt.Graphics2D g, int width, int height){
    	//System.out.println("ggggggggggggggggg");
/*	g.setColor( new java.awt.Color(255, 255, 255, 0));
	g.fillRect(0, 0, width, height);*/
    }
    
    public void paintTBIconBorder0(java.awt.Graphics2D g, int width, int height){
        g.setColor( new java.awt.Color(255, 255, 255) );
        g.fillRect(0, 0, width, height);
        g.setColor( new java.awt.Color(174, 212, 249) );
	g.drawLine(0, 1, 0, height-2);
	g.drawLine(width-1, 1, width-1, height-2);
	g.drawLine(1, 0, width-2, 0);
	g.drawLine(1, height-1, width-2, height-1);
    }   
    
}
