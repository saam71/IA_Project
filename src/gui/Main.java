package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public Main() {
		/*comm. test*/
		MainFrame frame = new MainFrame();

		frame.setSize(new java.awt.Dimension(1076, 690));
		// Center the window
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
   				}
   				new Main();
            }
   	});
    }
}
