package jarviewer.compilation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.Regexp;

import de.java2html.converter.Java2HtmlConversionOptions;
import de.java2html.converter.JavaSource;
import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.converter.JavaSourceParser;
import de.java2html.converter.JavaSourceStyleTable;

/*
 * Created on 01-11-2003 by jesper
 * This class should 
 */
/**
 * @author jesper
 */
public class JavaToHTML {
	
	public static StringBuffer textJavaToHTML(String code){
			StringReader stringReader = new StringReader(code);
		
//			  Parse the raw text to a JavaSource object
			JavaSource source = null;
			try {
			  source = new JavaSourceParser().parse(stringReader);
	
			} catch (IOException e) {
			  e.printStackTrace();
			  System.exit(1);
			}

//			  Create a converter and write the JavaSource object as Html
		
			JavaSource2HTMLConverter converter = new JavaSource2HTMLConverter(source);
			Java2HtmlConversionOptions options =  Java2HtmlConversionOptions.getDefault();
		
			options.setShowLineNumbers(false);
			options.setAddLineAnchors(false);
			options.setShowTableBorder(true);

			options.setTabSize(4);
			options.setStyleTable(JavaSourceStyleTable.getDefaultEclipseStyleTable());
			converter.setConversionOptions(options);
			StringWriter writer = new StringWriter(); 
			try {
			  converter.convert(writer);
		  
			} catch (IOException e) {
			}
			JavaSource2HTMLConverter.java2HtmlHomepageLinkEnabled = false;
			String html = writer.getBuffer().toString();
			
			String header = "http://www.java2html.de     = -->";
			String noHeader = html.substring(html.indexOf(header)+header.length(), html.length()-1);
			String noFooter = noHeader.substring(0,noHeader.lastIndexOf("<!-- = "));
			String noEndSource = noFooter.replaceFirst("<!-- end source code -->","");
			String noStartSource = noEndSource.replaceFirst("<!-- start source code -->","");
			
			return new StringBuffer(noStartSource);

		}
		
}
