package main.Parser;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

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
	private Container currentContainer;
	private Container previousContainer;
	
	public GUIParser() {
		this.output = new Output();
		this.currentContainer = this.output;
	}
	
	public GUIParser(File tokenizedFile) throws FileNotFoundException {
		this.tokenizedFile = tokenizedFile;
		this.scanner = new Scanner(tokenizedFile);
		this.output = new Output();
		this.currentContainer = this.output;
	}
	
	public void createGUI() {
		
		while(scanner.hasNext()) {
		
			getNextToken();
			if(currentToken.equals(Gui.WINDOW)){
					createWindow();
					
					if(currentToken.equals(Statement.END)) {
						
						getNextToken();
						if(currentToken.equals(Statement.PERIOD)) {
						System.out.println("Program executed successfully");
						}
					}
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
						if(currentToken.equals(Statement.LEFT_PARENS)) {
							
							//parse size
							setSize();
						}
						
						getNextToken();
						if(currentToken.equals(Layout.LAYOUT)) {
							
							//parse layout
							output.setLayout(parseLayout());
							
						}

						
						
						// parse widgets
						getNextToken();
						if(isWidget(currentToken)) {
							addWidget();
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
				if(currentToken.equals(Statement.COMMA)) {
					if(scanner.hasNextInt()) {
						getNextToken();
						height = Integer.parseInt(currentToken);
	
						getNextToken();
						if(currentToken.equals(Statement.RIGHT_PARENS)){
							
							output.setSize(width, height);
							
						}
					}
				}
			}
	}
	
	private LayoutManager parseLayout() {
		
		LayoutManager layout;
		
		getNextToken();
		
		if(currentToken.equals(LayoutType.FLOW)) {
			
			layout = new FlowLayout();
			output.setLayout(new FlowLayout());

			getNextToken();
			if(currentToken.equals(Statement.COLON)) {
				
				return layout;

					}
		}

		if(currentToken.equals(LayoutType.GRID)) {
			
			int rows = 0, cols = 0, hgap = 0, vgap = 0;
			
			getNextToken();
			if(currentToken.equals(Statement.LEFT_PARENS)) {
				if(scanner.hasNextInt()) {
					getNextToken();
					rows = Integer.parseInt(currentToken);
					
					getNextToken();
					if(currentToken.equals(Statement.COMMA)) {

						if(scanner.hasNextInt()) {
							getNextToken();
							cols = Integer.parseInt(currentToken);
							
							getNextToken();
							
							if(currentToken.equals(Statement.COMMA)) {
								
		
								if(scanner.hasNextInt()) {
									getNextToken();
									hgap = Integer.parseInt(currentToken);
									
									getNextToken();
									
									if(currentToken.equals(Statement.COMMA)) {

										if(scanner.hasNextInt()) {
											getNextToken();
											vgap = Integer.parseInt(currentToken);
											
											getNextToken();
											
										}
								}
								}
							}
							if(currentToken.equals(Statement.RIGHT_PARENS)) {
								
								
								layout = new GridLayout(rows, cols, hgap, vgap);
								
								getNextToken();
								if(currentToken.equals(Statement.COLON)) {
									
									return layout;
									
										}
									}
								}

							}
						}
					}
				}
		return null;
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

			break;
			
		case Widget.LABEL:
			getNextToken();
			addLabel();
			
			break;
			
		case Widget.PANEL:
			getNextToken();
			addPanel();
			
			break;
			
		case Widget.TEXTFIELD:
			getNextToken();
			addTextField();
			
			break;
			
		default: break;
			
		}
	}
	
	private void addTextField() {
		
		if(Patterns.isNumeric(currentToken)) {
			currentContainer.add(new JTextField(Integer.parseInt(currentToken)));
			
			getNextToken();
			if(currentToken.equals(Statement.SEMICOLON)) {
				
				getNextToken();
				if(isWidget(currentToken)) {
					addWidget();
				}
			}
		}	
	}
	private void addButton() {
		if(currentToken.equals(Statement.QUOTE)) {
			
			getNextToken();
			if(Patterns.isAlphaNumeric(currentToken)) {
				currentContainer.add(new JButton(currentToken));
				
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
						
					getNextToken();
					if(isWidget(currentToken)) {
						addWidget();
					}
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
					currentContainer.add(button);
					
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
	
	private void addLabel() {
		
		if(currentToken.equals(Statement.QUOTE)) {
			
			getNextToken();
			if(Patterns.isAlphaNumeric(currentToken)) {
				
				currentContainer.add(new JLabel(currentToken));
				
				getNextToken();
			}
			
			else currentContainer.add(new JLabel());
			
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
	
	
	private void addPanel() {
		
		JPanel panel = new JPanel();
		
		previousContainer = currentContainer;
		currentContainer = panel;
		
		if(currentToken.equals(Layout.LAYOUT)) {
			panel.setLayout(parseLayout());
			getNextToken();
			
			if(isWidget(currentToken)) {
				addWidget();
			}
			
			if(currentToken.equals(Statement.END)) {
				
				getNextToken();
				
				if(currentToken.equals(Statement.SEMICOLON)) {
					
					previousContainer.add(panel);
					currentContainer = previousContainer;
					getNextToken();
					if(isWidget(currentToken)) {
						
						addWidget();
					}
					
				}
			}
		}
	}
		
	private boolean isWidget(String token) {
		return token.equals(Widget.BUTTON) || token.equals(Widget.GROUP) || 
			token.equals(Widget.LABEL) || token.equals(Widget.PANEL) || token.equals(Widget.TEXTFIELD);
	}
	
	
	private void getNextToken() {
		if(scanner.hasNext()) {
		currentToken = scanner.next();
		}
		
		else{
			System.out.println("Error: Not enough tokens to complete program.");
		}
	}
	
	private void invalidToken() throws Exception {
		throw new Exception("Invalid Token encountered");
	}
	
	
}
	
