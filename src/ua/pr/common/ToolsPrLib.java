package ua.pr.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

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

	public static void addLibraryPath(String pathToAdd) throws Exception{
	    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);

	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);

	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return;
	        }
	    }

	    //add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    usrPathsField.set(null, newPaths);
	}
	
	public static Container getActiveForm(JComponent b, Object class_) {
		Container curObject = b.getParent();
		while (!curObject.getClass().equals(class_)) {
			curObject = curObject.getParent();
		}
		return curObject;
	}
	
	/**
	 * Повертає рядок заданої довжини
	 * В кінці доставляє пробєли, або обрізає
	 */
	public static String fixedLenthString(String string, int length) {
		if (length <= string.length()) {
			return string.substring(0, length);
		} else {
			return String.format("%s%" + (length - string.length()) + "s", string, "");
		}
	}
	
	/**
	 * 
	 * Example: customFormat("\u00a5###,###.###", 12345.67);
	 */
	public static String customFormat(String pattern, double value ) {
	    DecimalFormat myFormatter = new DecimalFormat(pattern);
	    return myFormatter.format(value);
	}
	
	public static void HideOnEsc(final Object comp, final boolean exit) {
		
		ActionListener escListener = new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	if (exit) {
	        		if (comp instanceof JFrame) {
	        			((JFrame)comp).dispose();
	        		} else {
	        			System.exit(0);
	        		}
				} else {
					((Component) comp).setVisible(false);
				}
	        }
	    };

	    if (comp instanceof JDialog) {
	    	((JDialog) comp).getRootPane().registerKeyboardAction(escListener,
		            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		            JComponent.WHEN_IN_FOCUSED_WINDOW);
		} else if (comp instanceof JFrame) {
	    	((JFrame) comp).getRootPane().registerKeyboardAction(escListener,
		            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
		            JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
	    
	}

}
