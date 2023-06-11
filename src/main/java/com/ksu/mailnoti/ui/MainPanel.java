 /*/
  *     Republic of Yemen
  *		2011
  *     E / Hamdi Taher Altahery
  *
  * Project of -( EasyNet )-
 /*/

package com.ksu.mailnoti.ui;
import com.ksu.mailnoti.ui.util.BottomPanel;
import java.awt.event.MouseMotionListener;

public class MainPanel extends javax.swing.JPanel {
    public MainTabbedPanel tabbedPanel = new MainTabbedPanel();
    public BottomPanel bottom = new BottomPanel();
    
    public MainPanel(MouseMotionListener mouseMotionListener){
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        add(tabbedPanel);
        add(bottom);
    }
}



