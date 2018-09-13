package main.Parser;

import java.util.Scanner;

import Output.Output;
import main.Tokens.Gui;

public class GUIParser {
	private Output output;
	private Scanner scanner;
	private String currentToken;
	
	public GUIParser() {
		this.output = new Output();
	}
	
	public GUIParser(Scanner scanner) {
		this.scanner = scanner;
		this.output = new Output();
	}
	
	public void createGUI() {
		
		while(scanner.hasNext()) {
		
			currentToken = scanner.next();
			if(currentToken.equals(Gui.WINDOW)){
					createWindow();
			}
		}
		
		this.output.setVisible(true);
				
	}
	
	private void checkForCorrectToken() {

			System.out.println("Insufficient Tokens for Processing");
			System.exit(0);
	}
	
	// create the window
	private void createWindow() {
		StringBuilder title = new StringBuilder();
		while(scanner.hasNext()) {
			currentToken = scanner.next();
			
			if(currentToken.equals("(")) {
				setSize();
				break;
			}
			
			else {
				title.append(currentToken + " ");
				this.output.setTitle(title.toString());
				}
			}
		
		}
	
	// set size after parsing ( NUMBER , NUMBER , )
	private void setSize() {

			int width, height;
			
			if(scanner.hasNextInt()) {
				currentToken = scanner.next();
				width = Integer.parseInt(currentToken);
				
				currentToken = scanner.next();
				if(currentToken.equals(",")) {
					if(scanner.hasNextInt()) {
						currentToken = scanner.next();
						height = Integer.parseInt(currentToken);
						
						output.setSize(width, height);
					}
				}
			}
	}
	
	
}
	
