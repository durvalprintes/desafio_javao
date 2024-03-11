package sge;

import javax.swing.SwingUtilities;

import sge.gui.window.Principal;

public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Principal::new);
	}

}
