package ua.pr.ui;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class GUITools {
	public static void createRecommendedMargin(JButton[] buttons) {
		for (int i = 0; i < buttons.length; i++) {

			Insets margin = buttons[i].getMargin();
			margin.left = 12;
			margin.right = 12;
			buttons[i].setMargin(margin);
		}
	}

	public static void makeSameSize(JComponent[] components) {
		int[] sizes = new int[components.length];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = components[i].getPreferredSize().width;
		}
		int maxSizePos = maximumElementPosition(sizes);
		Dimension maxSize = components[maxSizePos].getPreferredSize();
		for (int i = 0; i < sizes.length; i++) {
			components[i].setPreferredSize(maxSize);
			components[i].setMinimumSize(maxSize);
			components[i].setMaximumSize(maxSize);
		}
	}
	
	public static void fixTextFieldSize(JTextField field) {
		Dimension size = field.getPreferredSize();
		size.width = field.getMaximumSize().width;
		field.setMaximumSize(size);
	}

	private static int maximumElementPosition(int[] array) {
		int maxPos = 0;
		for (int i=1; i < array.length; i++) {
			if (array[i] > array [maxPos]) {
				maxPos = i;
			}
		}
		return maxPos;
	}
}
