package jarviewer.compilation;
import jarviewer.fileutil.FileHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import jode.decompiler.Main;
import jode.decompiler.Options;
import jode.decompiler.ProgressListener;
/**
 * @author Jesper Linvald (jesper@linvald.net)
 *
 */
public class DecompilerWrapper {
	
	
	/**
	 * @return the top most directory where the jar was extracted
	 */
	public static File decompileWholeJar(String jarFile, String destDir){
		if(destDir==null){
			destDir=new File(".").getAbsolutePath();
		}
		
		String []CMD = new String[]{ "--style","sun","--pretty","--dest", destDir+"/tmp/", jarFile};
		jode.decompiler.Main.main(CMD);
		return new File(destDir+"/tmp/");
	}
	public static StringBuffer decompileSingleClass(String fullyQuallifiedPath, String classPath, String destDir){
		System.out.println("fullly:" + fullyQuallifiedPath);
		String pathName = fullyQuallifiedPath.substring(0, fullyQuallifiedPath.lastIndexOf("\\")+1);
		//System.out.println("Path:" + pathName);
		String fileClazz = fullyQuallifiedPath.substring(fullyQuallifiedPath.lastIndexOf("\\")+1);
		String clazz = fileClazz.substring(0,fileClazz.indexOf(".class"));
		System.out.println("Class:" + clazz);

		String cp = pathName +","+destDir +"," + fileClazz + ",C://eclipse_work//CycleShop,." ;

		
		String []CMD = new String[]{"--pretty", "--cp", cp , "--style","sun","--dest", destDir, clazz};
		
		jode.decompiler.Main.main(CMD);
		return FileHandler.getTextFileText(new File(fileClazz.replaceFirst("class", "java")));
	}
}