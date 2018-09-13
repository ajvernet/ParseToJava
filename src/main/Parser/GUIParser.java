package main.Parser;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Scanner;

import javax.swing.JButton;

import Output.Output;
import main.Tokens.Gui;
import main.Tokens.Layout;
import main.Tokens.LayoutType;
import main.Tokens.Statement;
import main.Tokens.Widget;

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
		
			getNextToken();
			if(currentToken.equals(Gui.WINDOW)){
					createWindow();
			}
		}
		
		this.output.setVisible(true);
				
	}
	

	
	// create the window
	private void createWindow() {
		StringBuilder title = new StringBuilder();
		while(scanner.hasNext()) {
			getNextToken();
			
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
					setWidget();
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
										setWidget();
											}
										}
									}
								}

							}
						}
					}
				}
			}
	
	
	private void setWidget() {
		switch(currentToken) {
			
		case Widget.BUTTON: 
			System.out.println("detected button");
			getNextToken();
			setButton();
		}
	}
	
	private void setButton() {
		System.out.println("in set button");
		output.add(new JButton(currentToken));
		
		getNextToken();
		if(currentToken.equals(";")) {

				getNextToken();
				if(isWidget(currentToken)) {
					setWidget();
				}
			}

	}
	
//	private void checkForCorrectToken() {
//
//		System.out.println("Insufficient Tokens for Processing");
//		System.exit(0);
//	}
	
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
	
