package main.Parser;

import java.util.Scanner;

import Output.Output;
import main.Tokens.Gui;

public class GUIParser {
	private Output output;
	Scanner scanner;
	
	public GUIParser() {
		this.output = new Output();
	}
	
	public GUIParser(Scanner scanner) {
		this.scanner = scanner;
		this.output = new Output();
	}
	
	public void createGUI() {
		
		String currentToken;
		boolean buildingGui = true;
		
		while(scanner.hasNext()) {
		
			currentToken = scanner.next();
				if(currentToken.equals(Gui.WINDOW))
					if(scanner.hasNext()) {
						StringBuilder title = new StringBuilder();
						
						boolean buildingTitle = true;
						while(buildingTitle) {
							
							if(scanner.hasNext()) {
							currentToken = scanner.next();
							
								switch(currentToken) {
								case "(":
									buildingTitle = false;
									break;
									
								default:
									title.append(currentToken + " ");
									this.output.setTitle(title.toString());
								}			
							}
							
							if(!scanner.hasNext()) {
								buildingTitle=false;
								printNoTokenError();
							}

						}
						
					}
				
						
					this.output.setVisible(true);
				}
		}
	
	private void printNoTokenError() {
			System.out.println("Insufficient Tokens for Processing");
	}
	
}
	
