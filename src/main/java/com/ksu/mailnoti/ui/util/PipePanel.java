 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altaheri
  *
  * Project of -( Balance Game )-
 /*/

package com.ksu.mailnoti.ui.util;




public class PipePanel extends javax.swing.JPanel{
    public java.awt.Color fillColor = new java.awt.Color(255, 255, 255, 80);
	public javax.swing.JButton nextJButton;
//**********************************
//	edge = 0; // no point corner.
//	edge = 1; // zero point corner.
//	edge = 2; // one point corner.
//	edge = 3; // two point corner.
	public int edge = 2;
//**********************************
	public static final int VERTICAL_PATH = 101;
	public static final int HORIZONTAL_PATH = 102;
        public static final int EXTENDED_HORIZONTAL_PATH = 103;

	public int pathType = HORIZONTAL_PATH;
	public int pathBreadth = 60;
	public int pathLength = 40;
	public int width = 40;
	public int height = 150;
//**********************************
//	public int topFreeArea = 15;
//	public int botFreeArea = 15;
//**********************************

/*******************************************************************************/
/*------------------------------>( Constructor )<------------------------------*/
	public PipePanel(int pathBreadth, int pathType){
		this(pathBreadth, pathType, 2);
	}
	public PipePanel(int pathBreadth, int pathType, int edge){
            this(pathBreadth, 40, pathType, edge);
	}
	public PipePanel(int pathBreadth, int pathLength, int pathType, int edge){
		this.pathBreadth = pathBreadth;
		this.pathLength = pathLength;
		this.pathType = pathType;
                this.edge = edge;
		//setOpaque(false);
		setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);

		if(pathType==HORIZONTAL_PATH){
			setMaximumSize(new java.awt.Dimension(100000, pathBreadth));
			setPreferredSize(new java.awt.Dimension(pathLength, pathBreadth));
			setMinimumSize(new java.awt.Dimension(pathLength, pathBreadth));
			//putPanelInCenter(this, start);
		}else if(pathType == VERTICAL_PATH){
			//edge = 0;
			setMaximumSize(new java.awt.Dimension(pathBreadth, 100000));
			setPreferredSize(new java.awt.Dimension(pathBreadth, 200));
			setMinimumSize(new java.awt.Dimension(pathBreadth, pathLength));
		//add(start);
		}
	}
/*--------------------------->( End Constructor )<-----------------------------*/
/*******************************************************************************/

/*******************************************************************************/
/**/ 	public void  paintComponent(java.awt.Graphics g){
		int w = getWidth();
		int h = getHeight();
		//System.out.println("Width = "+w+"    Height = "+h);
		if(w>0 && h>0){

			if(pathType == HORIZONTAL_PATH){
				int x1 = 0;
				int y1 = (h/2)-pathBreadth/2+edge;
				int x2 = w-1;
				int y2 = (h/2)+pathBreadth/2-edge-1;
				paintHorPath(g, x1, y1, x2, y2);
			}else if(pathType == EXTENDED_HORIZONTAL_PATH){
				int x1 = 0;
				int y1 = (h/2)-(h-pathBreadth)/2+edge;
				int x2 = w-1;
				int y2 = (h/2)+(h-pathBreadth)/2-edge-1;
				paintHorPath(g, x1, y1, x2, y2);
                        }else if(pathType == VERTICAL_PATH){
				int x1 = (w/2)-pathBreadth/2+edge;
				int y1 = 0;
				int x2 = (w/2)+pathBreadth/2-edge-1;
				int y2 = h-1;
				paintVerPath(g, x1, y1, x2, y2);
			}

                        
		}
	}
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
	public void paintHorPath(java.awt.Graphics g, int x1, int y1, int x2, int y2){
		g.setColor(fillColor);
		g.fillRect(x1, y1, x2+1, y2-y1+1);
		g.setColor(new java.awt.Color(255, 255, 255, 50));
		g.fillRect(x1+edge, y1, x2-edge-1, y2-y1+1);
//*********************************
		g.setColor( new java.awt.Color(26,57,111) );
		g.drawLine(x1+edge, y1, x2-edge, y1);//top
		g.drawLine(x1+edge, y2, x2-edge, y2);//bottom
			
		g.drawLine(x1, y1-edge, x1+edge, y1);//top left corner
		g.drawLine(x2-edge, y1, x2, y1-edge);//top right corner
		g.drawLine(x1, y2+edge, x1+edge, y2);//bottom left corner
		g.drawLine(x2-edge, y2, x2, y2+edge);//bottom right corner
	}
/*******************************************************************************/

/*******************************************************************************/
	public void paintVerPath(java.awt.Graphics g, int x1, int y1, int x2, int y2){
		g.setColor(fillColor);
		g.fillRect(x1, y1, x2-x1+1, y2+1);
		g.setColor(new java.awt.Color(255, 255, 255, 50));
//		g.fillRect(x1, y1+edge, x2-x1+1, y2-edge-1);
//*********************************
		g.setColor( new java.awt.Color(26,57,111) );
		g.drawLine(x1, y1+edge, x1, y2-edge);//left
		g.drawLine(x2, y1+edge, x2, y2-edge);//right
			
		g.drawLine(x1-edge, y1, x1, y1+edge);//top left corner
		g.drawLine(x2, y1+edge, x2+edge, y1);//top right corner
		g.drawLine(x1-edge, y2, x1, y2-edge);//bottom left corner
		g.drawLine(x2, y2-edge, x2+edge, y2);//bottom right corner
	}
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/

/*******************************************************************************/
/*-------------------->( Method To : ------------------- )<--------------------*/
		public static void putPanelInCenter(javax.swing.JPanel parentPanel, javax.swing.JPanel childPanel){
		parentPanel.setLayout(new javax.swing.BoxLayout(parentPanel, javax.swing.BoxLayout.Y_AXIS));
		
		javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
		javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
		
		jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,0,0));
		jPanel2.setLayout(new java.awt.GridLayout(1,0,0,0));
		jPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
		jPanel2.setMinimumSize(new java.awt.Dimension(0, 0));
		
		
		childPanel.setBackground(parentPanel.getBackground());
		jPanel1.setBackground(parentPanel.getBackground());
		jPanel2.setBackground(parentPanel.getBackground());
		jPanel1.setBackground(java.awt.Color.red);
		jPanel2.setBackground(java.awt.Color.blue);
/**/		childPanel.setOpaque(parentPanel.isOpaque());		
		jPanel1.setOpaque(parentPanel.isOpaque());		
		jPanel2.setOpaque(parentPanel.isOpaque());		

		parentPanel.add(jPanel1);
		parentPanel.add(childPanel);
		parentPanel.add(jPanel2);
	}
/*-------------------->( End ------------------- Method )<---------------------*/
/*******************************************************************************/
}