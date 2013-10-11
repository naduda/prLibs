package ua.pr.ui.login;

public interface ILogin {
	String getUser();
	String getPassword();
	String getServer();
	String getDB();
	Boolean isSave();
	String getStrConn();
	
	void setUser(String value);
	void setPassword(String value);
	void setServer(String value);
	void setDB(String value);
	void setStrConn(String value);
	void setSave(boolean value);
}
