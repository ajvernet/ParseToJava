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
			getNextToken();
			addRadioButtonGroup();
		}
		
	}
	
	private void addButton() {
		if(currentToken.equals(Statement.QUOTE)) {
			
			getNextToken();
			if(Patterns.isAlphaNumeric(currentToken)) {
			output.add(new JButton(currentToken));
				
				getNextToken();
				if(currentToken.equals(Statement.QUOTE)) {
					
					getNextToken();
					if(currentToken.equals(Statement.SEMICOLON)) {
			
							getNextToken();
							if(isWidget(currentToken)) {
								addWidget();
							}
						}
					}
			}
		}
	}
	
	private void addRadioButtonGroup() {
		ButtonGroup group = new ButtonGroup();
		if(currentToken.equals(RadioButton.RADIO)) {
			getNextToken();
			addRadioButton(group);
			
			getNextToken();
			if(currentToken.equals(Statement.END)) {
				
				getNextToken();
				if(currentToken.equals(Statement.SEMICOLON)) {
						//add logic here
				}
			}
		}
	}
	
	private void addRadioButton(ButtonGroup group) {
		if(currentToken.equals(Statement.QUOTE)) {
			
			getNextToken();
			if(Patterns.isAlphaNumeric(currentToken)) {
				
				JRadioButton button = new JRadioButton(currentToken);
				group.add(button);
				
				getNextToken();
				if(currentToken.equals(Statement.QUOTE)) {
					
				getNextToken();
				if(currentToken.equals(Statement.SEMICOLON)){
					output.add(button);
					
					getNextToken();
					if(currentToken.equals(RadioButton.RADIO)) {
						
						getNextToken();
						addRadioButton(group);
						}
					}
				}
			}
		}
	}
		
	private boolean isWidget(String token) {
		return token.equals(Widget.BUTTON) || token.equals(Widget.GROUP) || 
			token.equals(Widget.LABEL) || token.equals(Widget.PANEL);
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
	
