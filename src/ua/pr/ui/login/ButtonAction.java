package ua.pr.ui.login;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import ua.pr.db.ServerSQL;

public class ButtonAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private static final String TITLE_MESSAGE = "Підключення";
	private static final String ERROR_CONNECT = "Помилка з'єднання. \n Перевірте параметри підключення";
	
	private String strConn;
	private ILogin log;
	
	public ButtonAction(String name) {
		super(name);
	}
	
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
			Connection conn = null;
			try {
				strConn = String.format(log.getStrConn(),
						log.getServer(), log.getUser(), log.getPassword());
				strConn = strConn + ";databaseName=" + log.getDB();
				
				try {
					conn = ServerSQL.getConnection(strConn);
				} catch (Exception e) {
					System.out.println("error - CONNECTION");
				}
				
				Container curObject = ((JButton)event.getSource()).getParent();
				while (!curObject.getClass().equals(LoginDialog.class)) {
					curObject = curObject.getParent();
				}
				
				LoginDialog ld = (LoginDialog) curObject;
				if (conn != null) {					
					ld.setStrConnect(strConn);
					ld.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(ld.getParent(),ERROR_CONNECT,TITLE_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
				}
				
			} catch (Exception e) {
				System.out.println("ERROR: set Login fields ...");
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("ERROR: conn.close()");
				}
			}
			break;
		case CANCEL:
			System.exit(0);
			break;
		}
	}

}