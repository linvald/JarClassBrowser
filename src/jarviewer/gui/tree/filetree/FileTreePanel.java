package jarviewer.gui.tree.filetree;

import jarviewer.compilation.DecompilerWrapper;
import jarviewer.compilation.JavaToHTML;
import jarviewer.fileutil.FileHandler;
import jarviewer.fileutil.FileTypes;
import jarviewer.gui.JarGui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class FileTreePanel extends JPanel {
	private JTree tree;
	private JScrollPane scrollTree;
	private JarGui gui = null;
	protected Object jarReader;
	
	public FileTreePanel(File jarFile, JarGui gui) {
		this.gui = gui;
		init(jarFile);
	}
	
	
	public void init(File topNode){
		String type = FileTypes.getFileType(topNode);
		if(type.equals(".jar")|| type.equals("jar")){
			topNode= DecompilerWrapper.decompileWholeJar(topNode.getAbsolutePath(), gui.getDestinationDir().getPath());
		}
		setLayout(new GridLayout(1, 1));
		FileSystemModel fileSystemModel = new FileSystemModel(topNode);
		// create JTree for FileSystemModel
		tree = new JTree(fileSystemModel);
		
		// make JTree editable for renaming Files
		tree.setEditable(true);
		tree.setCellRenderer(new FileTreeRenderer());
	
		// add a TreeSelectionListener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			// display details of newly selected File when
			// selection changes
			public void valueChanged(TreeSelectionEvent event) {
				File file = (File) tree.getLastSelectedPathComponent();
				if (file != null) {
					String ext =
						file.getName().substring(
							file.getName().lastIndexOf(".") + 1,
							file.getName().length());
					if (ext.equals("txt")
						|| ext.equals("bat")
						|| ext.equals("html")
						|| ext.equals("log")
						|| ext.equals("cs")
						|| ext.equals("css")
						|| ext.equals("xml")
						|| ext.equals("xsl")
						|| ext.equals("xslt")
						|| ext.equals("php")
						|| ext.equals("htm")
						|| ext.equals("cpp")
						|| ext.equals("classpath")
						|| ext.equals("project")
						|| ext.equals("build")
						|| ext.equals("dtd")
						|| ext.equals("xsd")) {
						//open it
						StringBuffer b = FileHandler.getTextFileText(file);
						FileTreePanel.this.gui.getTabs().addTab(file.getName(), file.getName(), b);
					} else if (ext.equals("jar")) {
						if (gui.getDestinationDir() == null) {
							JOptionPane
									.showMessageDialog(gui,
											"Please select a destination dir above where to decompiled files should go...");
						} else {
							FileTreePanel panel = new FileTreePanel(file, FileTreePanel.this.gui);
							FileTreePanel.this.gui.tree = panel.getTree();
							FileTreePanel.this.gui.addTree(panel, "jar");
							FileTreePanel.this.gui.getTreeTab().setSelectedIndex(1);
						}
					}else if(ext.equals("java")){
						//pretty print
						StringBuffer pretty = JavaToHTML.textJavaToHTML(FileHandler.getTextFileText(file).toString());
						gui.getTabs().addHTMLTab(file.getName(),pretty.substring(0,10),pretty,file,FileHandler.getTextFileText(file).toString());				
					}else if(ext.equals("class")){
						//we need user to give package - we cant deduce package from filesystem...
						String msg = "No support for direct decompilation in this version - maybe later..\nSelect a jarfile..";
						JOptionPane.showMessageDialog(gui,msg);				
					}
				}
			}
		}); // end addTreeSelectionListener
		scrollTree = new JScrollPane(tree);
		Dimension minimumSize = new Dimension(200, 400);
		scrollTree.setMinimumSize(minimumSize);
		add(scrollTree);
	}
	
	public JTree getTree() {
		return this.tree;
	}
}
