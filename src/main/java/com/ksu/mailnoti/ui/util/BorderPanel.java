 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( Signature Recognition )-
 /*/

package com.ksu.mailnoti.ui.util;
import java.util.ArrayList;
import javax.swing.ImageIcon;


public class BorderPanel extends javax.swing.JPanel{
    public java.awt.Color fillColor = new java.awt.Color(255, 255, 255, 120);
    public int inPanelColorTran = 255;
	protected InnerPanel innerPanel;
//**********************************
//	edge = 0; // no point corner.
//	edge = 1; // zero point corner.
//	edge = 2; // one point corner.
//	edge = 3; // two point corner.
	public int edge = 3;
//**********************************
	public static final int TOP = 201;
	public static final int BOTTOM = 202;
	public static final int LEFT = 203;
	public static final int RIGHT = 204;
	public static final int CENTER_LEFT = 205;
        public static final int CENTER_RIGHT = 206;
        
	private ArrayList topPath;
	private ArrayList leftPath;
	private ArrayList bottomPath;
	private ArrayList rightPath;
//**********************************

/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
    public BorderPanel(){
	innerPanel = new InnerPanel();
	setLayout(new java.awt.GridLayout(0,1));
	setBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(8, 8, 8, 8), new java.awt.Color(255,255,255,0)));
	add(innerPanel);
        //setOpaque(false);
    }
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/

/*******************************************************************************/
    private BorderInfo createBInfo(int gapType, int startPoint, int gapLength){
        int p1 = startPoint-1;
        int p2 = startPoint+gapLength;
	if(gapType==TOP||gapType==BOTTOM)
	    return new BorderInfo(gapType, p1, p2, 0, 0);
	else if(gapType==LEFT||gapType==RIGHT)
	    return new BorderInfo(gapType, 0, 0, p1, p2);
	else if(gapType==CENTER_LEFT||gapType==CENTER_RIGHT)
	    return new BorderInfo(gapType, 0, 0, p1, gapLength);   
        
	else return null;
    }
/*******************************************************************************/
/*	public void setPaths(int t, int l, int b, int r){
		setPaths(createBInfo(gapType, p1, p2));
	}*/
	public void setPaths(int gapType, int p1, int p2){
		setPaths(createBInfo(gapType, p1, p2));
	}
	public void setPaths(BorderInfo borderInfo){
                if(borderInfo==null)
                    return;
                
                if(borderInfo.gapType==TOP){
			if(topPath==null) topPath = new ArrayList(1);
			topPath.add(borderInfo);
		}else if(borderInfo.gapType==LEFT){
			if(leftPath==null) leftPath = new ArrayList(1);
			leftPath.add(borderInfo);
		}else if(borderInfo.gapType==BOTTOM){
			if(bottomPath==null) bottomPath = new ArrayList(1);
			bottomPath.add(borderInfo);
		}else if(borderInfo.gapType==RIGHT){
			if(rightPath==null) rightPath = new ArrayList(1);
			rightPath.add(borderInfo);
		}else if(borderInfo.gapType==CENTER_LEFT){
			leftPath = new ArrayList(1);
			leftPath.add(borderInfo);
		}else if(borderInfo.gapType==CENTER_RIGHT){
			rightPath = new ArrayList(1);
			rightPath.add(borderInfo);
                }
	}
/*******************************************************************************/
	public void setImage(ImageIcon image){
		innerPanel.setImage(image);
	}      
/*******************************************************************************/

/*******************************************************************************/
 	public void paintComponent(java.awt.Graphics g){
            //super.paintComponent(g);
		int w = getWidth()-1;
		int h = getHeight()-1;
		//System.out.println("Width1111 = "+w+"    Height1111 = "+h);
		if(w>0 && h>0){
			g.setColor(fillColor);
			g.fillRoundRect(1, 1, w-1, h-1, 10, 10);

			paintBorders(g, 0, 0, w, h);
                        
		}
	}
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
	public void paintBorders(java.awt.Graphics g, int x1, int y1, int x2, int y2){
		g.setColor(new java.awt.Color(26,57,111));
		if(topPath!=null)
			paintTBBorders(g, topPath, x1+edge, x2-edge, y1);
		else g.drawLine(x1+edge, y1, x2-edge, y1);//top
		if(bottomPath!=null)
			paintTBBorders(g, bottomPath, x1+edge, x2-edge, y2);
		else {g.drawLine(x1+edge, y2, x2-edge, y2);/*System.out.println("zWidth1111 = "+getWidth()+"    zHeight1111 = "+getHeight());*/}//bottom
		if(leftPath!=null)
			paintLRBorders(g, leftPath, y1+edge, y2-edge, x1);
		else g.drawLine(x1, y1+edge, x1, y2-edge);//left
		if(rightPath!=null)
			paintLRBorders(g, rightPath, y1+edge, y2-edge, x2);
		else g.drawLine(x2, y1+edge, x2, y2-edge);//right

		g.drawLine(x1, y1+edge, x1+edge, y1);//top left corner
		g.drawLine(x2-edge, y1, x2, y1+edge);//top right corner
		g.drawLine(x1, y2-edge, x1+edge, y2);//bottom left corner
		g.drawLine(x2-edge, y2, x2, y2-edge);//bottom right corner
	}
	public void paintTBBorders(java.awt.Graphics g, ArrayList blist, int x1, int x2, int y){//top + bottom
		BorderInfo bi;
		for(int i=0; i<blist.size(); i++){
			bi = (BorderInfo)blist.get(i);
			g.drawLine(x1, y, bi.x1, y);
			x1 = bi.x2;
		}
		g.drawLine(x1, y, x2, y);
	}
	public void paintLRBorders(java.awt.Graphics g, ArrayList blist, int y1, int y2, int x){//left + right
		BorderInfo bi;
                    
		for(int i=0; i<blist.size(); i++){
                    bi = (BorderInfo)blist.get(i);
                    if(bi.gapType==CENTER_LEFT||bi.gapType==CENTER_RIGHT){
                        g.drawLine(x, y1, x, bi.y1);
                        y1 = y2-bi.y2;
                        break;
                    }
                    g.drawLine(x, y1, x, bi.y1);
                    y1 = bi.y2;
		}
		g.drawLine(x, y1, x, y2);
	}




/*-----------------------------------------------------------------------------*/
	class BorderInfo{
	    public int gapType;
	    public int x1;
	    public int y1;
	    public int x2;
	    public int y2;

		public BorderInfo(int gapType, int x1, int x2, int y1, int y2){
			this.gapType = gapType;
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}
	}
/*-----------------------------------------------------------------------------*/



/*-----------------------------------------------------------------------------*/
	class InnerPanel extends javax.swing.JPanel{
	    private ImageIcon imageI = null;
	    private double imgAspectRatio;
/*******************************************************************************/
		public InnerPanel(){
		}
/*******************************************************************************/

/*******************************************************************************/
		public void paintComponent(java.awt.Graphics g){
			int w = getWidth();
			int h = getHeight();
			if(w>0 && h>0){
				if(imageI!=null){
					if((h*imgAspectRatio)>w)	h = (int)(w*(1f/imgAspectRatio));
					else	w = (int)(h*imgAspectRatio);
 					g.drawImage(imageI.getImage(), getWidth()/2-w/2, getHeight()/2-h/2, w, h, null);
				}else if(getComponentCount()!=0){
					g.setColor(new java.awt.Color(133, 141, 154, 0));
					g.fillRoundRect(0, 0, w, h, 4, 4);
				}else{
					g.setColor(new java.awt.Color(255, 255, 255, inPanelColorTran));
					g.fillRoundRect(0, 0, w, h, 4, 4);
				}
			}
		}
/*******************************************************************************/
		
/*******************************************************************************/
		public void setImage(ImageIcon image){
			int iw = image.getIconWidth();
			int ih = image.getIconHeight();
                        double imgARatio = (double)iw/ih;
                        setImage(image, imgARatio);
		}
		public void setImage(ImageIcon image, double imgARatio){
			imageI = image;
			imgAspectRatio = imgARatio;
			setPreferredSize(new java.awt.Dimension(288, 162));
			//setMinimumSize(new java.awt.Dimension(288, 162));
			//setMaximumSize(new java.awt.Dimension(10000, ih));
		}                
/*******************************************************************************/
	}
/*-----------------------------------------------------------------------------*/

}
