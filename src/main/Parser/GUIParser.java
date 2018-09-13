package main.Parser;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import Output.Output;
import main.Tokens.Gui;
import main.Tokens.Layout;
import main.Tokens.LayoutType;
import main.Tokens.RadioButton;
import main.Tokens.Statement;
import main.Tokens.Widget;

public class GUIParser {
	private File tokenizedFile;
	private Output output;
	private Scanner scanner;
	private String currentToken;
	
	public GUIParser() {
		this.output = new Output();
	}
	
	public GUIParser(File tokenizedFile) throws FileNotFoundException {
		this.tokenizedFile = tokenizedFile;
		this.scanner = new Scanner(tokenizedFile);
		this.output = new Output();
	}
	
	public void createGUI() {
		
		while(scanner.hasNext()) {
		
			getNextToken();
			if(currentToken.equals(Gui.WINDOW)){
					createWindow();
			}
		}
		
		this.output.setVisible(true);
				
	}
	

	
	// create the window
	private void createWindow() {

			getNextToken();
			
			if(currentToken.equals(Statement.QUOTE)) {
				
				getNextToken();
				if(Patterns.isAlphaNumeric(currentToken)) {
				this.output.setTitle(currentToken);

					getNextToken();
					if(currentToken.equals(Statement.QUOTE)) {
						
						getNextToken();
						if(currentToken.equals("(")) {
							setSize();
						}
					}
				}
			}
			
	}
		
		
	
	// set size after parsing ( NUMBER , NUMBER , )
	private void setSize() {

			int width, height;
			
			if(scanner.hasNextInt()) {
				getNextToken();
				width = Integer.parseInt(currentToken);
				
				getNextToken();
				if(currentToken.equals(",")) {
					if(scanner.hasNextInt()) {
						getNextToken();
						height = Integer.parseInt(currentToken);
	
						getNextToken();
						if(currentToken.equals(")")){
							
							output.setSize(width, height);
							
							getNextToken();
							
							if(currentToken.equals(Layout.LAYOUT)) {
								
								setLayout();
								
							}
						}
					}
				}
			}
	}
	
	private void setLayout() {
		
		getNextToken();
		
		if(currentToken.equals(LayoutType.FLOW)) {
			output.setLayout(new FlowLayout());

			getNextToken();
			if(currentToken.equals(":")) {
				getNextToken();
				if(isWidget(currentToken)) {
					addWidget();
						}
					}
		}

		if(currentToken.equals(LayoutType.GRID)) {
			
			int rows, cols;
			
			getNextToken();
			if(currentToken.equals("(")) {
				if(scanner.hasNextInt()) {
					getNextToken();
					rows = Integer.parseInt(currentToken);
					
					getNextToken();
					if(currentToken.equals(",")) {

						if(scanner.hasNextInt()) {
							getNextToken();
							cols = Integer.parseInt(currentToken);
							
							getNextToken();
							if(currentToken.equals(")")) {
								output.setLayout(new GridLayout(rows, cols));
								
								getNextToken();
								if(currentToken.equals(":")) {
									getNextToken();
									if(isWidget(currentToken)) {
										addWidget();
											}
										}
									}
								}

							}
						}
					}
				}
			}
	
	
	private void addWidget() {
		switch(currentToken) {
			
		case Widget.BUTTON: 
			getNextToken();
			addButton();
			
			break;
		
		case Widget.GROUP:
			addRadioButtonGroup();
		}
		
	}
	
	private void addButton() {
		if(isString(currentToken)) {
		String buttonText = extractString(currentToken);
		output.add(new JButton(buttonText));
	
		getNextToken();
		if(currentToken.equals(";")) {

				getNextToken();
				if(isWidget(currentToken)) {
					addWidget();
				}
			}
		}

	}
	
	private void addRadioButtonGroup() {
		ButtonGroup group = new ButtonGroup();
		getNextToken();
		if(currentToken.equals(RadioButton.RADIO)) {
			getNextToken();
			addRadioButton(group);
			
			getNextToken();
			if(currentToken.equals(Statement.END)) {
				
				getNextToken();
				if(currentToken.equals(";")) {

				}
			}
		}
	}
	
	private void addRadioButton(ButtonGroup group) {
		if(isString(currentToken)) {
			String buttonText = extractString(currentToken);
		JRadioButton button = new JRadioButton(buttonText);
		group.add(button);
		
		getNextToken();
		if(currentToken.equals(";")){
			output.add(button);
			
			getNextToken();
			if(currentToken.equals(RadioButton.RADIO)) {
				getNextToken();
				addRadioButton(group);
			}
			}
		}
	}
		
	private boolean isWidget(String token) {
		return token.equals(Widget.BUTTON) || token.equals(Widget.GROUP) || 
			token.equals(Widget.LABEL) || token.equals(Widget.PANEL);
	}
	
	private boolean isString(String token) {
		return token.startsWith("\"") & token.endsWith("\"");
	}
	
	private String extractString(String token) {
		return token.substring(1, token.length() - 1);
	}
	
	private void getNextToken() {
		if(scanner.hasNext()) {
		currentToken = scanner.next();
		}
		
		else{
			System.out.println("Error: Not enough tokens to complete program.");
		}
	}
	
	
}
	
