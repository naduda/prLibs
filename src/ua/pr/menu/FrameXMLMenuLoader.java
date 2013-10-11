package ua.pr.menu;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JFrame;

public class FrameXMLMenuLoader extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String MAIN_MENU = "mainMenu";

	private XMLMenuLoader loader;
	
	public FrameXMLMenuLoader(String title, String xmlFilePath) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		try {
			InputStream stream = new FileInputStream(xmlFilePath);
			setLoader(new XMLMenuLoader(stream));
			loader.parse();
			setJMenuBar(loader.getMenuBar(MAIN_MENU));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setSize(300, 200);
		setVisible(true);
	}

	public XMLMenuLoader getLoader() {
		return loader;
	}

	public void setLoader(XMLMenuLoader loader) {
		this.loader = loader;
	}
}
