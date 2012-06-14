package jarviewer.gui;

import jarviewer.gui.tree.filetree.FileTreePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * @author Jesper Linvald (jesper@linvald.net)
 */
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */

public class ButtonPanel extends JPanel implements ActionListener{
	
	private JLabel browseLabel, dirLabel;
	private JButton browseJarButton;
	private JarGui gui;
	private JComboBox dirsCombo;
	private File currentRootDir = null;
	private File[]rootDirs = null;
	private JLabel whatToDo = new JLabel("What´s this?", JLabel.CENTER);
	private JPanel space = new JPanel();
	private JTextField destinationText = new JTextField();
	private JButton browseDestBut = new JButton("Destination");
	private File destination = null;
	
	public ButtonPanel(JarGui gui){
		super();
		this.setLayout(new GridLayout(1,2));

		this.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		rootDirs = File.listRoots();
		for (int i = 0; i < rootDirs.length; i++) {
			File f = rootDirs[i];
			if(!f.canRead()){
				rootDirs[i] = null;
			}
		}
		ArrayList clean = new ArrayList();
		for (int i = 0; i < rootDirs.length; i++) {
			if(rootDirs[i]!= null){
				clean.add(rootDirs[i]);
			}
		}
		rootDirs = (File[])clean.toArray(new File[clean.size()]);
		currentRootDir = (File)rootDirs[0];
		this.gui = gui;
		initBrowseDirectories();
		initBrowseJarFile();
		initDestSelect();
		setRootDir(currentRootDir);
		
		
		whatToDo.addMouseListener(new MouseListener(){
			String help = 
				"<font color=\"red\" size=\"4\">Select a jar or zip file to explore it!<br><br>" + 
				"From the jar view you can select class files to watch it decompiled (class2Java)!<br><br>" +
				"The decompiled java files you can save from the context menu of the tabs!<br>"+
				"You can explore regular text based files (e.g. .cs, html etc.)!" +
				"Selecting a jar file in file view will expand it in the jar viewer!" +
				"Selecting a root dir from the drop down from the top will naturally expand<br> this dir in the file view!"+
				"<br><br>The nodes of the file view has a context menu allowing for files to be interpreted as HTML" +
				"where the default is regular txt - that is you could view a .html file both as markup and rendered..."+
				"<br><br>Thanks to the Jode project (http://jode.sourceforge.net/) the files can be decompiled...<br>" +
				"Thanks to java2Html (http://www.java2html.com/) the java files renders nicely (as in eclipse).."+
				"</font>";
			StringBuffer buf = new StringBuffer(help); 
			public void mouseClicked(MouseEvent arg0) {
				ButtonPanel.this.gui.getTabs().addHTMLTab("HELP","Help info",buf, null, "");	
			}

			public void mouseEntered(MouseEvent arg0) {
				whatToDo.setToolTipText("Click here to view help info!");
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		this.add(whatToDo);
		this.add(space);
	}
	
	/**
	 * 
	 */
	private void initDestSelect() {
		browseDestBut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Choose a destination for decompiled files");
				fileChooser.setDialogType(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showOpenDialog(ButtonPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
						gui.setDestinationDir(fileChooser.getSelectedFile());
						destinationText.setText(fileChooser.getSelectedFile().toString());
				}
			}	
		});
		add(this.destinationText);
		add(this.browseDestBut);
	}

	private void initBrowseJarFile(){
		browseLabel = new JLabel("Browse jar file:",JLabel.CENTER);
		browseJarButton = new JButton("Browse");
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a jar file");
		fileChooser.setDialogType(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new JarFilter());
		
		browseJarButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = fileChooser.showOpenDialog(ButtonPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
						FileTreePanel panel = new FileTreePanel(fileChooser.getSelectedFile(),gui);
						gui.tree = panel.getTree();
						gui.addTree(panel,"jar");
						gui.getTreeTab().setSelectedIndex(1);		
				}
			}	
		});
		
		add(browseLabel);
		add(browseJarButton);
	}
	
	private void initBrowseDirectories(){
		dirsCombo = new JComboBox();
		dirsCombo.addActionListener(this);
		dirsCombo.setBackground(Color.white);
		dirLabel = new JLabel("Directory roots:",JLabel.CENTER);

		for (int i = 0; i < rootDirs.length; i++) {
			File f = (File)rootDirs[i];
			if(f.isDirectory() && f.canRead())
				try {
					dirsCombo.addItem(f);
				} catch (RuntimeException e) {
					System.err.println("RuntimeException:" +e);
				}
		}
		add(dirLabel);
		add(dirsCombo);
	}
	
	
	class JarFilter extends FileFilter {
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String ext = null;
				String s = f.getName();
				int i = s.lastIndexOf('.');
				if (i > 0 && i < s.length() - 1) {
					ext = s.substring(i + 1).toLowerCase();
				}
				if (ext != null) {
					if (ext.equals("jar") ||ext.equals("zip") ) {
						return true;
					} else {
						return false;
					}
				}
				return false;
			}

			public String getDescription() {
				return null;
			}
		}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			JComboBox cb = (JComboBox) e.getSource();
			File f = ((File) cb.getSelectedItem());
			setRootDir(f);
		}else{
			System.out.println(e.getSource());
		}
	}

	/**
	 * @param file
	 * @return
	 */
	private void setRootDir(File file) {
		this.currentRootDir = file;
		gui.addTree(new FileTreePanel(file,this.gui),"file");
	}
	/**
	 * @return Returns the destination.
	 */
	public File getDestination() {
		return destination;
	}
	/**
	 * @param destination The destination to set.
	 */
	public void setDestination(File destination) {
		this.destination = destination;
	}
}
