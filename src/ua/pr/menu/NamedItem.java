package ua.pr.menu;

public class NamedItem {
	private int id;
	private String text;
	private int maxSize = 10;
	
	public NamedItem() {

	}
	
	public NamedItem(final int id, final String text, int maxCharsCount) {
		this.id = id;
		this.text = text;
		this.maxSize = maxCharsCount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(text);
		ret.setLength(maxSize);
		return ret.toString();
	}
}
