package ua.pr.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JFrame;

public class FrameXMLMenuLoader extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String MAIN_MENU = "mainMenu";
	private static final String EXIT = "exit";

	public FrameXMLMenuLoader(String title, String xmlFilePath) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		try {
			InputStream stream = new FileInputStream(xmlFilePath);
			XMLMenuLoader loader = new XMLMenuLoader(stream);
			loader.parse();
			setJMenuBar(loader.getMenuBar(MAIN_MENU));
			loader.addActionListener(EXIT, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setSize(300, 200);
		setVisible(true);
	}
}
