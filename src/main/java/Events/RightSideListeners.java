package Events;

import GUI.Center.Right;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class RightSideListeners {
    Right rightSide;
    JList<?> receivedFiles;

    public RightSideListeners(Right rightSide){
        this.rightSide = rightSide;
        this.receivedFiles = rightSide.getList();
        receivedFiles.addMouseListener(new ReceivedFileChosen());
    }

    class ReceivedFileChosen implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            JList<?> list = (JList<?>) e.getSource();
            if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){

                if(!Desktop.isDesktopSupported())
                    return;

                try{
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open((File)list.getSelectedValue());
                }catch (IOException err){
                    System.out.println("There is no default application " +
                            "for opening files with this extension");
                }
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
