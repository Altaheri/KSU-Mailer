 /*/
  *      Republic of Yemen
  *		2011
  *	E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import com.ksu.mailnoti.ui.util.CloMaxMin;

public class MainFrame extends javax.swing.JFrame 
    implements ActionListener,ComponentListener,WindowListener,MouseMotionListener{

    protected GraphicsDevice screen = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    protected CloMaxMin cloMaxMin ;
/*****************************************************************/
    protected boolean isFullScreen = true;
//*->to determine the (current and last)size and location of the main JFrame
    protected Dimension currentSize;
    protected Dimension lastSize;
    protected Point currentLocation;
    protected Point lastLocation;
//*->to determine the Width and height of the main container without all borders
    protected int pureWid ;
    protected int pureHei ;
/*****************************************************************/

/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public MainFrame(String title){
	super(title);
/*****************************************************************/	
	cloMaxMin = new CloMaxMin(this);
/*****************************************************************/	
	setLayout(new java.awt.GridLayout(0, 1,0,0));
	setPreferredSize(new Dimension(1056, 594));
	setMinimumSize(new Dimension(480,270));
/*****************************/	
	addComponentListener(this);
    	addWindowListener(this);
/*****************************/
	java.awt.GraphicsConfiguration gc = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	java.awt.Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(gc);
	System.out.println("insets.left = "+insets.left+"  insets.right = "+insets.right+"  insets.top = "+insets.top+"  insets.bottom = "+insets.bottom);
    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/


/*******************************************************************************/
/*------------>( Method To : Set Location At Center Of OS Screen)<-------------*/
    public void setLocationAtCenter() {
//	int width = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//	int height = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	int width = screen.getDisplayMode().getWidth();
	int height = screen.getDisplayMode().getHeight();
		
	if(getWidth()>width && getHeight()>height){ setSize(width, height);/*setAlwaysOnTop(true);*/}
	else if(getWidth()>width && !(getHeight()>height)){ setSize(width, getHeight());}
	else if(getHeight()>height && !(getWidth()>width)){ setSize(getWidth(), height);}
	else setAlwaysOnTop(false);

	setLocation((width-getWidth())/2 ,(height-getHeight())/2 );
    }
/*-------------------->( End setLocationAtCenter Method )<---------------------*/
/*******************************************************************************/


/*******************************************************************************/
/*-------->( Function To : Maximize Or Restore the ReceiveRDTJFrame)<----------*/
/*******************************************************************************/
    public void maximizeRestore(int type){
//*---->if(type = 0) Toggle
//*---->if(type = 1) Maximize
//*---->if(type = 2) Restore
	if(isFullScreen){
            if(type==0 || type==2){
		isFullScreen=false;
                    setLocation(lastLocation);
                    setSize(lastSize);
            }
	}else
            if(type==0 || type==1){
		isFullScreen=true;
		setLocation(0,0);
		setSize(screen.getDisplayMode().getWidth(),screen.getDisplayMode().getHeight());
            }
    }
/*--------------------->( End maximizeRestore Function )<----------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*----------------->( Functions To : Handle ComponentEvents)<------------------*/
    public void componentResized(ComponentEvent comEv){
//	System.out.println("MFComponentResized");
	if(screen.getDisplayMode().getWidth()==getWidth() && screen.getDisplayMode().getHeight()==getHeight()){
            isFullScreen = true;
            //setAlwaysOnTop(true);
            pureWid=getWidth()-10 ; pureHei=getHeight()-10;
	}else{
            isFullScreen = false;
            pureWid=getWidth()-10 ; pureHei=getHeight()-(24)-10;
            cloMaxMin.setVisible(false);
	}
	currentSize = getSize();
	if(!isFullScreen)
            lastSize = currentSize;
    }
/*****************************************************************/	
    public void componentMoved(ComponentEvent comEv){
	//System.out.println("componentMoved");
	lastLocation = currentLocation;
	currentLocation = getLocation();
    }
/*****************************************************************/	
    public void componentHidden(ComponentEvent comEv){	System.out.println("componentHidden");	}
    public void componentShown(ComponentEvent comEv){	System.out.println("componentShown");	}
/*------------------>( End ComponentEventsHandle Functions )<------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*------------------->( Functions To : Handle WindowEvents)<-------------------*/
    public void windowClosed(WindowEvent winEv)	{
	System.out.println("windowClosed");
	cloMaxMin.dispose();
        System.exit(0);
    }
    public void windowClosing(WindowEvent winEv){
    	System.out.println("windowClosing");
	dispose();
    }
    public void windowIconified(WindowEvent winEv){   System.out.println("windowIconified");    }
    public void windowDeiconified(WindowEvent winEv){ System.out.println("windowDeiconified");    }
    public void windowActivated(WindowEvent winEv){     }
    public void windowDeactivated(WindowEvent winEv){    }
/*****************************************************************/	
    public void windowOpened(WindowEvent winEv){        System.out.println("windowOpened");    }
    public void windowGainedFocus(WindowEvent winEv){  	System.out.println("windowGainedFocus");	}
    public void windowLostFocus(WindowEvent winEv){	System.out.println("windowLostFocus");	}
    public void windowStateChanged(WindowEvent winEv){ 	System.out.println("windowStateChanged");	}
/*------------------->( End WindowEventsHandle Functions )<--------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*------------------->( Functions To : Handle MouseEvents)<--------------------*/
    public void mouseMoved(MouseEvent mouEv){
    	if(isFullScreen)
            if(mouEv.getYOnScreen()<20){
                System.out.println("mouseMoved");
    		cloMaxMin.setVisible(true);
            }else
    		cloMaxMin.setVisible(false);
    }
/*****************************************************************/	
    public void mouseDragged(MouseEvent mouEv){	}
/*------------------->( End MouseEventsHandle Functions )<---------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*------------------->( Function To : Handle ActionEvents)<--------------------*/
    public void actionPerformed( ActionEvent aEvent ){
	if(aEvent.getSource().equals(cloMaxMin.closeJButt)){
            dispose();
	}else if(aEvent.getSource().equals(cloMaxMin.maximizeJButt) ){
            maximizeRestore(0);
            cloMaxMin.setVisible(false);
	}else if( aEvent.getSource().equals(cloMaxMin.minimizeJButt) ){
            //cloMaxMin.setVisible(false);
	}else 
            System.out.println(aEvent.getActionCommand());
    }
/*------------------->( End ActionEventsHandle Function )<---------------------*/
/*******************************************************************************/
}