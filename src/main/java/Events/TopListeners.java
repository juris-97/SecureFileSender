package Events;

import GUI.Top;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopListeners {

    private final Top top;

    public TopListeners(Top top){
        this.top = top;
        top.getChooseFileButton().addActionListener(new ChooseFileListener());
    }

    class ChooseFileListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            // choose file clicked
            System.out.println("CHOOSE FILE CLICKED");
        }
    }
}