package Events;

import GUI.Center.Right;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RightSideListeners {
    Right rightSide;
    JList receivedFiles;

    public RightSideListeners(Right rightSide){
        this.rightSide = rightSide;
        this.receivedFiles = rightSide.getList();
        receivedFiles.addMouseListener(new ReceivedFileChosen());
    }

    class ReceivedFileChosen implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            JList list = (JList) e.getSource();
            if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
                int index = list.locationToIndex(e.getPoint());
                System.out.println("DOUBLE CLICKED FOR RECEIVED FILE AR INDEX = " + index);
            }
        }

        public void mousePressed(MouseEvent e) {

        }
        public void mouseReleased(MouseEvent e) {

        }
        public void mouseEntered(MouseEvent e) {

        }
        public void mouseExited(MouseEvent e) {

        }
    }
}
