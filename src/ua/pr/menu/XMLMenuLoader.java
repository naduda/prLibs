package ua.pr.menu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import ua.pr.common.ToolsPrLib;

public class XMLMenuLoader {

	private InputSource source;
	private SAXParser parser;
	private DefaultHandler documentHandler;
	@SuppressWarnings("rawtypes")
	private Map menuStorage = new HashMap();
	
	public XMLMenuLoader(InputStream stream) {
		try {
			Reader reader = new InputStreamReader(stream, "UTF-8");
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
	
	public JComponent getMenuItem(String name) {
		return (JComponent) menuStorage.get(name);
	}

	public void addActionListener(String uniqueID, ActionListener listener) {
		JComponent item = getMenuItem(uniqueID);

		if (item.getClass() == JMenuItem.class) {
			((JMenuItem)item).addActionListener(listener);
		} else if (item.getClass() == JButton.class) {
			((JButton)item).addActionListener(listener);
		}
	}
	
	private JMenuBar currentMenuBar;
	@SuppressWarnings("rawtypes")
	private LinkedList menus = new LinkedList();
	
	class XMLParser extends DefaultHandler {
		private static final String ATTRIBUTE_NAME = "name";
		private static final String MENUBAR = "menubar";
		private static final String MENU = "menu";
		private static final String MENUITEM = "menuitem";
		private static final String ATTRIBUTE_MNEMONIC = "mnemonic";
		private static final String ATTRIBUTE_ACCELERATOR = "accelerator";
		private static final String ATTRIBUTE_ENABLED = "enabled";
		private static final String ATTRIBUTE_ALIGN = "align";
		private static final String ATTRIBUTE_TYPE = "type";
		private static final String ATTRIBUTE_FORMAT = "format";
		private static final String ATTRIBUTE_UNIQUE_ID = "uniqueID";
		private static final String ATTRIBUTE_COLOR = "color";
		private static final String ATTRIBUTE_SIZE = "size";
		private static final String ATTRIBUTE_ICON = "icon";
		private static final String ATTRIBUTE_BORDER_INSETS = "borderInsets";
		private static final String FALSE = "false";
		private static final String TOOLBAR = "toolbar";
		private static final String ITEM = "item";
		private static final String PANEL = "panel";
		private static final String BUTTON = "button";
		private static final String COMBOBOX = "combobox";
		private static final String COMBOBOX_ITEM = "cmbItem";
		private static final String LEFT = "left";
		private static final String RIGHT = "right";
		private static final String CENTER = "center";
		private static final String CALENDAR = "calendar";
		private static final String LABEL = "label";				
		
		private JComboBox<Object> curCombo = null;
		Vector<Object> cmbItems = new Vector<Object>();
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.equals(MENUBAR))
				parseMenuBar(attributes);
			else if (qName.equals(MENU))
				parseMenu(attributes);
			else if (qName.equals(MENUITEM))
				parseMenuItem(attributes);
			else if (qName.equals(TOOLBAR))
				parseToolBar(attributes);
			else if (qName.equals(ITEM))
				parseItem(attributes);
			else if (qName.equals(PANEL))
				parsePanel(attributes);
			else if (qName.equals(COMBOBOX_ITEM))
				parseComboItem(attributes);
		}

		public void endElement(String uri, String localName, String qName) {
			if (qName.equals(MENU)) {
				menus.removeFirst();
			}
			if (qName.equals(TOOLBAR)) {
				menus.removeFirst();
			}
			if (qName.equals(PANEL)) {
				menus.removeFirst();
			}
			if (qName.equals(ITEM)) {
				if (cmbItems.size() > 0) {
					curCombo.setSelectedIndex(curCombo.getItemCount() - 1);
					curCombo.setSelectedIndex(0);
				}
			}
		}

		@SuppressWarnings("unchecked")
		protected void parseMenuBar(Attributes attrs) {
			JMenuBar menuBar = new JMenuBar();

			menuStorage.put(attrs.getValue(ATTRIBUTE_UNIQUE_ID), menuBar);
			currentMenuBar = menuBar;
		}

		@SuppressWarnings("unchecked")
		protected void parseMenu(Attributes attrs) {
			JMenu menu = new JMenu();

			adjustProperties(menu, attrs);
			menuStorage.put(attrs.getValue(ATTRIBUTE_UNIQUE_ID), menu);
			
			if ( menus.size() != 0 ) {
				((JMenu)menus.getFirst()).add(menu);
			} else {
				currentMenuBar.add(menu);
			}

			menus.addFirst(menu);
		}
		
		@SuppressWarnings("unchecked")
		protected void parseToolBar(Attributes attrs) {
			JToolBar tBar = new JToolBar(attrs.getValue(ATTRIBUTE_NAME));
			
			adjustPropertiesToolBar(tBar, attrs);
			menuStorage.put(attrs.getValue(ATTRIBUTE_UNIQUE_ID), tBar);
			
			if ( menus.size() != 0 ) {
				((JToolBar)menus.getFirst()).add(tBar);
			} else {
				currentMenuBar.add(tBar);
			}

			menus.addFirst(tBar);
		}
		
		@SuppressWarnings("unchecked")
		protected void parsePanel(Attributes attrs) {
			JPanel panel = new JPanel();
			
			String align = attrs.getValue(ATTRIBUTE_ALIGN);
			if (align.equals(LEFT)) {
				panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			} else if (align.equals(RIGHT)) {
				panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			} else if (align.equals(CENTER)) {
				panel.setLayout(new FlowLayout(FlowLayout.CENTER));
			}
			
			menuStorage.put(attrs.getValue(ATTRIBUTE_UNIQUE_ID), panel);
			
			if ( menus.size() != 0 ) {
				((JPanel)menus.getFirst()).add(panel);
			} else {
				currentMenuBar.add(panel);
			}

			menus.addFirst(panel);
		}

		@SuppressWarnings("unchecked")
		protected void parseMenuItem(Attributes attrs) {
			String id = attrs.getValue(ATTRIBUTE_UNIQUE_ID);
			
			if (id.equals("separator")) {
				((JMenu)menus.getFirst()).addSeparator();
				return;
			}

			JMenuItem menuItem = new JMenuItem();

			adjustProperties(menuItem, attrs);
			menuStorage.put(id, menuItem);
			((JMenu)menus.getFirst()).add(menuItem);
		}

		@SuppressWarnings("unchecked")
		protected void parseItem(Attributes attrs) {
			String typeItem = attrs.getValue(ATTRIBUTE_TYPE);		

			JComponent comp = null;
			if (typeItem.equals(BUTTON)) {
				comp = new JButton(attrs.getValue(ATTRIBUTE_NAME));								
			} else if (typeItem.equals(COMBOBOX)) {
				cmbItems = new Vector<>();
				curCombo = new JComboBox<Object>(cmbItems);	
				comp = curCombo;
			} else if (typeItem.equals(CALENDAR)) {
				JDatePickerImpl dp = (JDatePickerImpl) JDateComponentFactory.createJDatePicker();
				Properties props = new Properties();
				props.put("messages.today", "Сьогодні");
				props.put("messages.nextMonth", "Наступний місяць");
				props.put("messages.previousMonth", "Попередній місяць");
				props.put("messages.nextYear", "Наступний рік");
				props.put("messages.previousYear", "Попередній рік");
				props.put("messages.clear", "Очистити");
				dp.setI18nStrings(props);
//				dp.getJFormattedTextField().setValue(Calendar.getInstance().getTime());
				dp.setDateFormate(attrs.getValue(ATTRIBUTE_FORMAT));
				
				comp = dp;
			} else if (typeItem.equals(LABEL)) {
				comp = new JLabel(attrs.getValue(ATTRIBUTE_NAME));
			}
			
			String sColor = attrs.getValue(ATTRIBUTE_COLOR);
			if (sColor != null) {
				String[] arr = sColor.split(";");

				int r = Integer.parseInt(arr[0].substring(arr[0].indexOf("(") + 1));
				int g = Integer.parseInt(arr[1]);
				int b = Integer.parseInt(arr[2].substring(0, arr[2].indexOf(")")));

				comp.setForeground(new Color(r, g, b));
			}
			
			String sSize = attrs.getValue(ATTRIBUTE_SIZE);
			if (sSize != null) {
				String[] arr = sSize.split(";");
				int w = Integer.parseInt(arr[0]);
				int h = Integer.parseInt(arr[1]);
				
				if (typeItem.equals(CALENDAR)) {
					((JDatePickerImpl)comp).setSizeElement(w, h);
				} else {
					comp.setSize(w, h);
				}
			}
			
			menuStorage.put(attrs.getValue(ATTRIBUTE_UNIQUE_ID), comp);
			((JComponent)menus.getFirst()).add(comp);
		}
		
		protected void parseComboItem(Attributes attrs) {
			String name = attrs.getValue(ATTRIBUTE_NAME);
			int id = Integer.parseInt(attrs.getValue("id"));

			cmbItems.add(new NamedItem(id, name, 10));
		}
		
		private void adjustProperties(JMenuItem menuItem, Attributes attrs) {
			String text = attrs.getValue(ATTRIBUTE_NAME);
			String mnemonic = attrs.getValue(ATTRIBUTE_MNEMONIC);
			String accelerator = attrs.getValue(ATTRIBUTE_ACCELERATOR);
			String enabled = attrs.getValue(ATTRIBUTE_ENABLED);
			String icon = attrs.getValue(ATTRIBUTE_ICON);
			String borderInsets = attrs.getValue(ATTRIBUTE_BORDER_INSETS);
//			----------------------------------------------------------------
			for (int i = 0; i < attrs.getLength(); i++) {
				menuItem.putClientProperty(attrs.getLocalName(i), attrs.getValue(i));
			}
//			----------------------------------------------------------------
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
			if (icon != null) {
				menuItem.setIcon(new ImageIcon(ToolsPrLib.getFullPath(icon)));
			}
			if (borderInsets != null) {
				String[] arr = borderInsets.split(";"); 
				int t = Integer.parseInt(arr[0]);
				int l = Integer.parseInt(arr[1]);
				int b = Integer.parseInt(arr[2]);
				int r = Integer.parseInt(arr[3]);
				menuItem.setBorder(new EmptyBorder(t, l, b, r));
			}
		}
		
		private void adjustPropertiesToolBar(JToolBar tBarItem, Attributes attrs) {
			String enabled = attrs.getValue(ATTRIBUTE_ENABLED);

			if (enabled != null) {
				boolean isEnabled = true;
				if (enabled.equals(FALSE)) {
					isEnabled = false;
				}
				tBarItem.setEnabled(isEnabled);
			}
		}

	}
}
