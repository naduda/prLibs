package ua.pr.ui.login;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

public class ButtonAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private ILogin log;
	private String strConn;
	
	public ButtonAction(String name, ILogin log) {
		super(name);
		this.log = log;
	}
	
	public enum EnAction {
	    OK,
	    CANCEL
	}
	
	public void actionPerformed(ActionEvent event) {
		EnAction enumval = EnAction.valueOf(getValue(NAME).toString().trim().toUpperCase());

		switch (enumval) {
		case OK:
			try {
				strConn = String.format(log.getStrConn(),
						log.getServer(), log.getUser(), log.getPassword());
				strConn = strConn + ";databaseName=" + log.getDB();
				
				Container curObject = ((JButton)event.getSource()).getParent();
				while (!curObject.getClass().equals(LoginDialog.class)) {
					curObject = curObject.getParent();
				}
				
				LoginDialog ld = (LoginDialog) curObject;
				ld.setStrConnect(strConn);
				ld.setVisible(false);
			} catch (Exception e) {
				System.out.println("ERROR: set Login fields ...");
			}
			break;
		case CANCEL:
			System.exit(0);
			break;
		}
	}

}