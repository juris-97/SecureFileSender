package Events;

import GUI.Center.Left;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

public class LeftSideListeners {

    private final Left leftSide;
    //Enumeration<AbstractButton> radioButtons;

    public LeftSideListeners(Left leftSide){
        this.leftSide = leftSide;

        this.leftSide.getLeftTop().getGenerateButton().addActionListener(new GenerateSessionKeyListener());
        this.leftSide.getLeftTop().getExchangeButton().addActionListener(new ExchangeSessionKeyListener());

        //radioButtons = leftSide.getLeftBottom().getButtonGroup().getElements();
    }


    class GenerateSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("GENERATE SESSION KEY CLICKED");
        }
    }

    class ExchangeSessionKeyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("EXCHANGE SESSION KEY CLICKED");
        }
    }
}
