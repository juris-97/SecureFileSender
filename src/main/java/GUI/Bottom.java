package GUI;

import javax.swing.*;
import java.awt.*;

public class Bottom extends JPanel {

    JButton send;
    JFrame mainFrame;

    // [TODO] implement progress bar depending on cypher and sending time
    JProgressBar progressBar;

    private static final int HEIGHT = 2 * 40; // 80 pixels

    public Bottom(JFrame mainFrame){
        this.mainFrame = mainFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(mainFrame.getWidth(), HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(0, 10,0,10));


        send = new JButton("Send");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        progressBar.setForeground(new Color(0, 47, 8));

        this.add(send);
        this.add(progressBar);

        mainFrame.getContentPane().add(BorderLayout.SOUTH, this);
    }

    public JButton getSend() {
        return send;
    }
    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
