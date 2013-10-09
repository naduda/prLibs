package ua.pr.menu;

import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class XMLMenuLoader {

	private InputSource source;
	private SAXParser parser;
	private DefaultHandler documentHandler;
	@SuppressWarnings("rawtypes")
	private Map menuStorage = new HashMap();
	
	public XMLMenuLoader(InputStream stream) {
		try {
			Reader reader = new InputStreamReader(stream);
			source = new InputSource(reader);
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		documentHandler = new XMLParser();
	}

	public void parse() throws Exception {
		parser.parse(source, documentHandler);
	}

	public JMenuBar getMenuBar(String name) {
		return (JMenuBar) menuStorage.get(name);
	}

	public JMenu getMenu(String name) {
		return (JMenu) menuStorage.get(name);
	}
	
	public JMenuItem getMenuItem(String name) {
		return (JMenuItem) menuStorage.get(name);
	}

	public void addActionListener(String name, ActionListener listener) {
		getMenuItem(name).addActionListener(listener);
	}
	
	private JMenuBar currentMenuBar;
	@SuppressWarnings("rawtypes")
	private LinkedList menus = new LinkedList();
	

	class XMLParser extends DefaultHandler {
		private static final String ATTRIBUTE_NAME = "name";
		private static final String ITEM_MENUBAR = "menubar";
		private static final String ITEM_MENU = "menu";
		private static final String ITEM_MENUITEM = "menuitem";
		private static final String ATTRIBUTE_TEXT = "text";
		private static final String ATTRIBUTE_MNEMONIC = "mnemonic";
		private static final String ATTRIBUTE_ACCELERATOR = "accelerator";
		private static final String ATTRIBUTE_ENABLED = "enabled";
		private static final String FALSE = "false";
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.equals(ITEM_MENUBAR))
				parseMenuBar(attributes);
			else if (qName.equals(ITEM_MENU))
				parseMenu(attributes);
			else if (qName.equals(ITEM_MENUITEM))
				parseMenuItem(attributes);
		}

		public void endElement(String uri, String localName, String qName) {
			if (qName.equals(ITEM_MENU)) {
				menus.removeFirst();
			}
			
		}

		@SuppressWarnings("unchecked")
		protected void parseMenuBar(Attributes attrs) {
			JMenuBar menuBar = new JMenuBar();

			menuStorage.put(getAtrName(attrs), menuBar);
			currentMenuBar = menuBar;
		}

		@SuppressWarnings("unchecked")
		protected void parseMenu(Attributes attrs) {
			JMenu menu = new JMenu();

			adjustProperties(menu, attrs);
			menuStorage.put(getAtrName(attrs), menu);
			
			if ( menus.size() != 0 ) {
				((JMenu)menus.getFirst()).add(menu);
			} else {
				currentMenuBar.add(menu);
			}

			menus.addFirst(menu);
		}

		@SuppressWarnings("unchecked")
		protected void parseMenuItem(Attributes attrs) {
			String name = getAtrName(attrs);
			
			if (name.equals("separator")) {
				((JMenu)menus.getFirst()).addSeparator();
				return;
			}

			JMenuItem menuItem = new JMenuItem();

			adjustProperties(menuItem, attrs);
			menuStorage.put(name, menuItem);

			((JMenu)menus.getFirst()).add(menuItem);
		}

		private void adjustProperties(JMenuItem menuItem, Attributes attrs) {
			String text = attrs.getValue(ATTRIBUTE_TEXT);
			String mnemonic = attrs.getValue(ATTRIBUTE_MNEMONIC);
			String accelerator = attrs.getValue(ATTRIBUTE_ACCELERATOR);
			String enabled = attrs.getValue(ATTRIBUTE_ENABLED);

			menuItem.setText(text);
			if (mnemonic != null) {
				menuItem.setMnemonic(mnemonic.charAt(0));
			}
			if (accelerator != null) {
				menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
			}
			if (enabled != null) {
				boolean isEnabled = true;
				if (enabled.equals(FALSE)) {
					isEnabled = false;
				}
				menuItem.setEnabled(isEnabled);
			}
		}

		public String getAtrName(Attributes attrs) {
			return attrs.getValue(ATTRIBUTE_NAME);
		}
		
	}
}
