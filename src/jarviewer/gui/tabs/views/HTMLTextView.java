package jarviewer.gui.tabs.views;


import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class HTMLTextView extends JPanel {
	private JTextPane text;
	private File file;
	private StringBuffer unmodifiedCode; //with html
	private String originalCode; //no html
	public HTMLTextView() {
	}
	public HTMLTextView(StringBuffer code, JPopupMenu contextMenu) {
		init(code, contextMenu);
	}
	public HTMLTextView(StringBuffer code, JPopupMenu contextMenu, File where) {
		this.file = where;
		init(code, contextMenu);
	}

	private void init(StringBuffer code,JPopupMenu contextMenu) {
		this.setUnmodifiedCode(code);
		setLayout(new GridLayout(1, 1));
		text = new JTextPane();
		javax.swing.text.html.HTMLEditorKit eKit = new javax.swing.text.html.HTMLEditorKit();
		text.setEditorKit(eKit);
		text.setContentType("text/html");
		JScrollPane scrollText = new JScrollPane(text);
		scrollText.setAutoscrolls(true);
		//text.setFont(new Font("Verdana", Font.BOLD, 12));
		text.setText(code.toString());
		text.add(contextMenu);
		this.add(scrollText);
	}
	public String getText() {
		return text.getText();
	}


	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @return Returns the unmodifiedCode.
	 */
	public StringBuffer getUnmodifiedCode() {
		return unmodifiedCode;
	}
	/**
	 * @param unmodifiedCode The unmodifiedCode to set.
	 */
	public void setUnmodifiedCode(StringBuffer unmodifiedCode) {
		this.unmodifiedCode = unmodifiedCode;
	}
	/**
	 * @param originalCode
	 */
	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}
	/**
	 * @return Returns the originalCode.
	 */
	public String getOriginalCode() {
		return originalCode;
	}
}
