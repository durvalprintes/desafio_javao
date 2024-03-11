package sge.gui.window;

import java.awt.*;

import javax.swing.JDialog;

public class Padrao extends JDialog {

  public Padrao() {
    setResizable(true);
    setMinimumSize(new Dimension(800, 600));
    setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
    pack();
  }

}
