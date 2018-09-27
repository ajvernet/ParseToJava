package Runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import main.LexicalAnalyzer.Lexer;
import main.Parser.GUIParser;

public class Runner {

	public static void main(String[] args) throws Exception {
		File guiSourceFile = new File("window.txt");
		Lexer lexer = new Lexer(guiSourceFile);
		
	
		GUIParser parser = new GUIParser(lexer.tokenizeFile());
		parser.createGUI();
	}

}
