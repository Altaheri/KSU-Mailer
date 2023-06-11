 /*/
  *      	 Republic of Yemen
  *				  2011
  *		E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui.util;

import com.ksu.mailnoti.ui.MainTabbedPanel;

class TopPanel extends javax.swing.JPanel{
    protected javax.swing.ImageIcon bgImageI = new javax.swing.ImageIcon("icons/toolbar.png");
	private java.awt.event.ActionListener actionListener;
	
	public TopPanel(java.awt.event.ActionListener al, java.awt.event.MouseMotionListener mml){
		this.actionListener = al;
	    setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
	
		setMaximumSize(new java.awt.Dimension(5000, 76));
		setPreferredSize(new java.awt.Dimension(600, 76));
		setMinimumSize(new java.awt.Dimension(300, 76));

		setBackground(new java.awt.Color(255,255,255));
		//setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 2));
		addMouseMotionListener( mml);
	}
 	public void  paintComponent(java.awt.Graphics g){
		int width = getWidth();
		int height = getHeight();
 		g.drawImage(bgImageI.getImage(), 0, 0, width, height, 0,0, bgImageI.getIconWidth(), bgImageI.getIconHeight(), null);
		
		g.setColor( MainTabbedPanel.OUTER_BORDER_COLOR );
		g.drawLine(1, 0, 1, height-4);//left
		g.drawLine(width-2, 0, width-2, height-4);//right
		g.drawLine(3, height-2, width-4, height-2);//bottom
		g.drawLine(2, height-3, 2, height-3);//bottom left corner
		g.drawLine(width-3, height-3, width-3, height-3);//bottom right corner

		g.setColor( MainTabbedPanel.INNER_BORDER_COLOR );
		g.drawLine(2, 0, 2, height-4);//left
		g.drawLine(width-3, 0, width-3, height-4);//right
		g.drawLine(3, height-3, width-4, height-3);//bottom
	}

	class DescriptionPanel extends javax.swing.JPanel{
		public DescriptionPanel(){
			setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 3));
			setOpaque(false);
                        
			//setMaximumSize(new java.awt.Dimension(400 , 40));
			setPreferredSize(new java.awt.Dimension(150 , 70));
			//setMinimumSize(new java.awt.Dimension(200 , 40));
//			setBorder(
//				new javax.swing.border.CompoundBorder(new javax.swing.border.MatteBorder(new java.awt.Insets(0, 1, 0, 0), MainTabbedPanel.OUTER_BORDER_COLOR),
//        			new javax.swing.border.MatteBorder(new java.awt.Insets(0, 1, 0, 0), MainTabbedPanel.INNER_BORDER_COLOR)));
		}
	
	 	public void  paintComponent(java.awt.Graphics g){
                    super.paintComponent(g);
			int x1 = 0;
			int y1 = 0;
			int x2 = getWidth()-1;
			int y2 = getHeight()-1;
//			int edge = 0; // no point corner.
//			int edge = 1; // zero point corner.
			int edge = 2; // one point corner.
//			int edge = 3; // two point corner.

			g.setColor( MainTabbedPanel.OUTER_BORDER_COLOR );
			g.drawLine(x1, y1+edge, x1, y2-edge);//left
			g.drawLine(x2, y1+edge, x2, y2-edge);//right
//			g.drawLine(x1+edge, y1, x2-edge, y1);//top
//			g.drawLine(x1+edge, y2, x2-edge, y2);//bottom
			
			g.drawLine(x1, y1+edge, x1+edge, y1);//top left corner
			g.drawLine(x2-edge, y1, x2, y1+edge);//top right corner
			g.drawLine(x1, y2-edge, x1+edge, y2);//bottom left corner
			g.drawLine(x2-edge, y2, x2, y2-edge);//bottom right corner
			if(edge-1>1){
				g.drawLine(x1, y1+(edge-1), x1+(edge-1), y1);//top left corner
				g.drawLine(x2-(edge-1), y1, x2, y1+(edge-1));//top right corner
				g.drawLine(x1, y2-(edge-1), x1+(edge-1), y2);//bottom left corner
				g.drawLine(x2-(edge-1), y2, x2, y2-(edge-1));//bottom right corner
			}

			g.setColor(MainTabbedPanel.INNER_BORDER_COLOR);
			g.drawLine(x1+1, y1+edge, x1+1, y2-edge);//left
			g.drawLine(x2-1, y1+edge, x2-1, y2-edge);//right
			g.drawLine(x1+edge, y1+1, x2-edge, y1+1);//top
			g.drawLine(x1+edge, y2-1, x2-edge, y2-1);//bottom
		}
	}
}
