package ua.pr.connection.ui.login;

public interface ILogin {
	String getUser();
	String getPassword();
	String getServer();
	String getDB();
	Boolean isSave();
	String getStrConn();
}
