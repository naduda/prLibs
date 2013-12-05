package ua.pr.common;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author pavel.naduda
 *
 */
public class ToolsPrLib {
	
	/*
	 *  Повертає повний шлях до файлу заданого за шаблоном:
	 *  path = "d:/1.txt"
	 *  path = "./1.txt"
	 *  path = "../../1.txt"
	 */
	public static String getFullPath(String path) {
		String res = "";
		if (path == null) {
			return res;
		}
		path = path.replace("\\", "/");
		
		if (path.length() != 0) {
			if (path.substring(0,1).equals(".")) {
				if (path.substring(0, 2).equals("./")) {
					res = System.getProperty("user.dir") + "/" + path.subSequence(2, path.length());
				} else if (path.substring(0, 3).equals("../")) {
					String rPath = path;
					String lPath = System.getProperty("user.dir").replace("\\", "/");
					while (rPath.substring(0, 3).equals("../")) {
						lPath = lPath.substring(0, lPath.lastIndexOf("/"));
						rPath = rPath.substring(rPath.indexOf("/") + 1);
					}
					res = lPath + "/" + rPath;
				}
			} else {
				res = path;
			}
		}
		
		return res.replace("\\", "/");
	}	
	
	/**
	 * Повертає список рядків, які знаходяться між вказаними символами begin і end
	 * List<String> res = getFindingText("qwe [first] dasg [second] asd", "[", "]")
	 */
	public static List<String> getFindingText(String text, String begin, String end) {
		List<String> ret = new ArrayList<>();
		if (text.indexOf(begin) != -1) {
			StringTokenizer st = new StringTokenizer(text.substring(text.indexOf(begin)), begin);
			while (st.hasMoreElements()) {
				String temp = (String) st.nextElement();
				if (temp.indexOf(end) != -1) {
					ret.add(temp.substring(0, temp.indexOf(end)));
				}
			}
		}
		
		return ret;
	}
}
