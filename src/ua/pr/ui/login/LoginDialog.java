package ua.pr.ui.login;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ua.pr.db.ServerSQL;
import ua.pr.ui.GUITools;

public class LoginDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Вхід до системи";
	private static final String USER = "Користувач:";
	private static final String PASSWORD = "Пароль:";
	private static final String OK_TEXT = "OK";
	private static final String CANCEL_TEXT = "Cancel";
	private static final String TAB_USER_NAME = "Основні";
	private static final String TAB_SERVER_NAME = "Додаткові";
	private static final String SERVER = "Сервер:";
	private static final String DATABASE = "База даних:";
	private static final String SAVE_TEXT = "Зберегти введені дані";
	private static final String MESSAGE_TITLE  = "Доступні бази даних на сервері:";
	
	private String strConnect = null;
	
	private JTextField txtUser, txtPassword, txtServer, txtDB;
	private JButton btnOK, btnCANCEL;
	private boolean save;
	private JFrame frm;
	
	public LoginDialog(JFrame parent, ILogin log) {
		super(parent, TITLE + " - " + log.getDB(), true);
		this.frm = parent;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		getContentPane().add(createGUI(log));
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel createGUI(final ILogin log) {
		JPanel pMain = new JPanel();
		
		JTabbedPane tabPanel = new JTabbedPane();
		Box pUser = new Box(BoxLayout.Y_AXIS);
		pUser.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Box pServer = new Box(BoxLayout.Y_AXIS);
		pServer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		tabPanel.addTab(TAB_USER_NAME, pUser);
		tabPanel.addTab(TAB_SERVER_NAME, pServer);
		
		Box name = new Box(BoxLayout.X_AXIS);
		JLabel lbUser = new JLabel(USER);
		name.add(lbUser);
		name.add(Box.createHorizontalStrut(12));
		txtUser = new JTextField(10);
		txtUser.setText(log.getUser());
		name.add(txtUser);
		
		Box password = new Box(BoxLayout.X_AXIS);
		JLabel lbPassword = new JLabel(PASSWORD);
		password.add(lbPassword);
		password.add(Box.createHorizontalStrut(12));
		txtPassword = new JPasswordField(10);
		txtPassword.setText(log.getPassword());
		password.add(txtPassword);
		
		GUITools.makeSameSize(new JComponent[] {lbUser, lbPassword});
		
		pUser.add(name);
		pUser.add(Box.createVerticalStrut(12));
		pUser.add(password);
		
		Box server = new Box(BoxLayout.X_AXIS);
		JLabel lbServer = new JLabel(SERVER);
		server.add(lbServer);
		server.add(Box.createHorizontalStrut(12));
		txtServer = new JTextField(10);
		txtServer.setText(log.getServer());
		server.add(txtServer);
		
		Box db = new Box(BoxLayout.X_AXIS);
		JLabel lbDB = new JLabel(DATABASE);
		db.add(lbDB);
		db.add(Box.createHorizontalStrut(12));
		txtDB = new JTextField(10);
		txtDB.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Connection conn = null;
				if (e.getClickCount() == 2 )
				{
					String strConn = String.format(log.getStrConn(), 
							log.getServer(), log.getUser(), log.getPassword());
					conn  = ServerSQL.getConnection(strConn);
					String ret = "";
					List<String> listDB = ServerSQL.getDBmsSQL(conn);
					for (int i = 0; i < listDB.size(); i++) {
						ret = ret + listDB.get(i) + "\n";
					}
					JOptionPane.showMessageDialog(frm,ret,MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		txtDB.setText(log.getDB());
		db.add(txtDB);
		
		GUITools.makeSameSize(new JComponent[] {lbServer, lbDB});
		
		pServer.add(server);
		pServer.add(Box.createVerticalStrut(12));
		pServer.add(db);
//		--------------------------------------------------------------------
		Box pBottom = new Box(BoxLayout.Y_AXIS);
		
		JPanel pCheckBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		JCheckBox chbSave = new JCheckBox(SAVE_TEXT);
		chbSave.setSelected(log.isSave());
		chbSave.addChangeListener(new ChangeListener() {		
			@Override
			public void stateChanged(ChangeEvent e) {
				setSave(((JCheckBox)e.getSource()).isSelected());
			}
		});
		pCheckBox.add(chbSave);
		pCheckBox.setBorder(BorderFactory.createEmptyBorder(-2, 5, 5, 0));
		
		
		JPanel pButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		JPanel grid = new JPanel(new GridLayout(1, 2, 10, 0));
		btnOK = new JButton(new ButtonAction(OK_TEXT, log));
		btnCANCEL = new JButton(new ButtonAction(CANCEL_TEXT, log));
		grid.add(btnOK);
		grid.add(btnCANCEL);
		pButton.add(grid);
		
		pBottom.add(pCheckBox);
		pBottom.add(pButton);
		getContentPane().add(pBottom, BorderLayout.SOUTH);
		
		setGroupAligmentX(new JComponent[] {pMain, pUser, pServer}, Component.LEFT_ALIGNMENT);
		setGroupAligmentY(new JComponent[] {txtUser, txtPassword, txtServer, txtDB, 
											lbUser, lbPassword, lbServer, lbDB}, Component.CENTER_ALIGNMENT);
		
		
		GUITools.createRecommendedMargin(new JButton[] {btnOK, btnCANCEL});
		GUITools.fixTextFieldSize(txtUser);
		GUITools.fixTextFieldSize(txtPassword);
		GUITools.fixTextFieldSize(txtServer);
		GUITools.fixTextFieldSize(txtDB);

		pMain.add(tabPanel);
		return pMain;
	}
	
	private void setGroupAligmentX(JComponent[] components, float aligment) {
		for (int i = 0; i < components.length; i++) {
			components[i].setAlignmentX(aligment);
		}
	}
	
	private void setGroupAligmentY(JComponent[] components, float aligment) {
		for (int i = 0; i < components.length; i++) {
			components[i].setAlignmentY(aligment);
		}
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public String getStrConnect() {
		return strConnect;
	}

	public void setStrConnect(String strConnect) {
		this.strConnect = strConnect;
	}
}
