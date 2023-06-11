
package com.ksu.mailnoti.start.irispen;

import com.ksu.mailnoti.start.StartPanel;
import com.ksu.mailnoti.ui.util.BorderPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Hamdi
 */
public class IRISPenPanel extends BorderPanel implements DocumentListener, ActionListener, FocusListener{
    protected ImageIcon bgImageI = new ImageIcon("icons/Default.jpg");    
    private final StartPanel parentPanel;  
    private Timer timer;
    public TransparentTextArea irisPenInputArea;   
    public boolean irisPenActivateState = false;
    
    public IRISPenPanel(StartPanel parentPanel) {        
        this.parentPanel = parentPanel;        
        timer = new Timer(20, this);                
        setBorder(null);
        
        irisPenInputArea = new TransparentTextArea();      
        irisPenInputArea.getDocument().addDocumentListener(this); 
        irisPenInputArea.setOpaque(false);
        irisPenInputArea.addFocusListener(this);       
        
        remove(this.innerPanel);
        add(irisPenInputArea);        
    }
      
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        if(e.getDocument().getLength()==1){
            timer.start ();
        }
    }
    @Override
    public void removeUpdate(DocumentEvent e) {    }
    @Override
    public void changedUpdate(DocumentEvent e) {    }
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        String capturedName = irisPenInputArea.getText();
        irisPenInputArea.setText("");             
        parentPanel.textRecognition.fuzzyNameMatching.startFuzzyNameMatching(capturedName, false);                                  
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        irisPenActivateState = true;
        irisPenInputArea.setBackground(new Color(204, 255, 204)); 
    }
    @Override
    public void focusLost(FocusEvent e) {
        irisPenActivateState = false;
        irisPenInputArea.setBackground(Color.white); 
    }

    public void requestFocus() {
        irisPenInputArea.requestFocusInWindow(); 
    }
}
