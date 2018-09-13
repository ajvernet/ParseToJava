package main.LexicalAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import main.Parser.Patterns;
import main.Tokens.Gui;
import main.Tokens.Layout;
import main.Tokens.LayoutType;
import main.Tokens.RadioButton;
import main.Tokens.Statement;
import main.Tokens.Widget;

public class Lexer {

	private Scanner scanner;
	private File tokenizedFile;
	private String currentToken;
	private FileWriter fileWriter;
	
	private static final List<String> validTokens = 
			new ArrayList<String>(Arrays.asList(
					Gui.WINDOW, Layout.LAYOUT, LayoutType.FLOW, LayoutType.GRID, RadioButton.RADIO, 
					Statement.COMMA, Statement.END, Statement.LEFT_PARENS, Statement.RIGHT_PARENS,
					Statement.SEMICOLON, Widget.BUTTON, Widget.GROUP, Widget.LABEL, Widget.PANEL,
					Statement.QUOTE, Statement.COLON
				)) ;
	
	public Lexer(File file) throws IOException {
		scanner = new Scanner(file);
		tokenizedFile = new File("tokenized.txt");
		fileWriter = new FileWriter(tokenizedFile);
	}
	
	public File tokenizeFile() throws IOException {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		while(scanner.hasNext()) {
			getNextToken();
			
			Pattern.compile("").splitAsStream(currentToken)
				.forEach(
					character ->
					{
			
					if(!Patterns.isAlphaNumeric(character)){
										
							if(isValidToken(character)) {
								
								try {
									if(stringBuilder.length() > 0) {
										fileWriter.write(stringBuilder.toString());
									}
									
									clearStringBuilder(stringBuilder);
									
									fileWriter.write(" " + character + " ");
								} catch (IOException e) {
									System.err.println(e.getStackTrace());
								}
							}
						
							else {
								System.err.println("Invalid token encountered");
							}
						
						
						}
					
					else {
							stringBuilder.append(character);
						}
					}
					
					);
			}
		fileWriter.close();

		return tokenizedFile;
	}
	
	public void getAndAppendNextToken() {
		currentToken += scanner.next();
	}
		
	public void getNextToken() {
		currentToken = scanner.next();
	}
	
	public boolean isValidToken(String token) {
		return validTokens.contains(token);
	}
	
	public void clearStringBuilder(StringBuilder builder) {
		builder.setLength(0);
	}
	
	
}
