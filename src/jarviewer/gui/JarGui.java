package jarviewer.gui;

/*
 * Created on 17-10-2003 by jesper
 * This class should 
 */
import jarviewer.fileutil.FileHandler;
import jarviewer.gui.tabs.views.TabView;
import jarviewer.gui.tree.filetree.FileTreePanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

/**
 * @author jesper
 */
public class JarGui extends JFrame {
	
	private Container frame = null;
	private final String title = "Jar & Zip browser";
	public JTree tree = null;
	private TabView tabs;
	private JTabbedPane treeTab;
	private JLabel noSelectedLabel = new JLabel("Browse for jar og zip file or select it in file view!");
	private File destinationDir = null;
	
	//panels
	private JPanel  bottom;
	public JPanel treePanel, treePanelDirs;
	private ButtonPanel top;
	private JSplitPane splitPane;
	
	
	public JarGui(){
		frame = this.getContentPane();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initGUI();
		this.setTitle(title);
	}
	

	
	private void initGUI(){	
		//Quit this app when the big window closes.
		   addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			   	/*File tmp  = new File("./" + "tmp" );
			   	if(tmp.isDirectory()){
					FileHandler.deleteFileTree(tmp);
			   	}*/
				   System.exit(0);
			   }
		   });
		
		treePanel = new JPanel();
		treePanelDirs = new JPanel();
		tabs = new TabView();
		treeTab = new JTabbedPane();
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeTab, tabs);
		top = new ButtonPanel(this);
		//top.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
		top.setBackground(Color.lightGray);
		this.setBackground(Color.lightGray);
		bottom = new JPanel();
		frame.setLayout(new BoxLayout(frame,BoxLayout.Y_AXIS));
		
		treePanel.setLayout(new GridLayout(1,1));
		treePanelDirs.setLayout(new GridLayout(1,1));
		treePanel.add(this.noSelectedLabel);
		bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
		
		frame.add(top);
		frame.add(bottom);
			
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		bottom.add(splitPane);
		this.setSize(600,500);
		
		treeTab.setTabPlacement(JTabbedPane.BOTTOM);
		treeTab.addTab("Dir view", treePanelDirs);
		treeTab.addTab("Jar view", treePanel);
	}

	public JTree getTree(){
		return this.tree;
	}
	
	public void addTree(JPanel treeWithPanel, String fileOrJar) {
		if (fileOrJar.equals("file")) {
			if (treePanelDirs.getComponents().length > 0)
				treePanelDirs.removeAll();
			treePanelDirs.add(treeWithPanel);
			if (treeWithPanel instanceof FileTreePanel) {
				FileTreePanel p = (FileTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if(treeTab.getTabCount()>0)
			this.treeTab.setSelectedIndex(0);
		} else if (fileOrJar.equals("jar")) {
			if (treePanel.getComponents().length > 0)
				treePanel.removeAll();
			treePanel.add(treeWithPanel);
			if (treeWithPanel instanceof FileTreePanel) {
				FileTreePanel p = (FileTreePanel) treeWithPanel;
				this.tree = p.getTree();
			}
			if(treeTab.getTabCount()>0)
			this.treeTab.setSelectedIndex(1);
		}
		splitPane.setDividerLocation(splitPane.getDividerLocation());
		//tree.repaint();
	}

	public static void main(String[] args) {
		JarGui gui = new JarGui();
		gui.show();
	}
	/**
	 * @return Returns the tabs.
	 */
	public TabView getTabs() {
		return tabs;
	}
	/**
	 * @param tabs The tabs to set.
	 */
	public void setTabs(TabView tabs) {
		this.tabs = tabs;
	}
	/**
	 * @return Returns the treeTab.
	 */
	public JTabbedPane getTreeTab() {
		return treeTab;
	}
	/**
	 * @param treeTab The treeTab to set.
	 */
	public void setTreeTab(JTabbedPane treeTab) {
		this.treeTab = treeTab;
	}
	/**
	 * @return Returns the destinationDir.
	 */
	public File getDestinationDir() {
		return destinationDir;
	}
	/**
	 * @param destinationDir The destinationDir to set.
	 */
	public void setDestinationDir(File destinationDir) {
		this.destinationDir = destinationDir;
	}
}
