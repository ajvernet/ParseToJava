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
import main.Tokens.Statement;
import main.Tokens.Widget;

public class GUIParser {
	private File tokenizedFile;
	private Output output;
	private Scanner scanner;
	private String currentToken;
	private Container currentContainer;
	private Container previousContainer;
	private JPanel panel;
	
	private JTextField screen;
	
	public GUIParser() {
		this.output = new Output();
		this.currentContainer = this.output;
	}
	
	public GUIParser(File tokenizedFile) throws FileNotFoundException {
		this.tokenizedFile = tokenizedFile;
		this.scanner = new Scanner(tokenizedFile);
		this.output = new Output();
		this.currentContainer = this.output;
		panel = new JPanel();
	}
	
	public void createGUI() throws Exception {
		
		while(scanner.hasNext()) {
		
			getNextToken();
			if(currentToken.equals(Gui.WINDOW)){
					createWindow();
					
					if(currentToken.equals(Statement.END)) {
						
						getNextToken();
						if(currentToken.equals(Statement.PERIOD)) {
						System.out.println("Program executed successfully");
						} else invalidToken();
					}
			}
		}
		
		this.output.setVisible(true);
				
	}
	

	
	// create the window
	private void createWindow() throws Exception {

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
						}  else invalidToken();
						
						getNextToken();
						if(currentToken.equals(Layout.LAYOUT)) {
							
							//parse layout
							output.setLayout(parseLayout());
							
						} else invalidToken();

						
						
						// parse widgets
						getNextToken();
						if(isWidget(currentToken)) {
							addWidget();
						} else invalidToken();
					} else invalidToken();
				} else invalidToken();
			} else invalidToken();
			
	}
		
		
	
	// set size after parsing ( NUMBER , NUMBER , )
	private void setSize() throws Exception {

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
							
						} else invalidToken();
					}
				} else invalidToken();
			} else invalidToken();
	}
	
	private LayoutManager parseLayout() throws Exception {
		
		LayoutManager layout;
		
		getNextToken();
		
		if(currentToken.equals(LayoutType.FLOW)) {
			
			layout = new FlowLayout();
			output.setLayout(new FlowLayout());

			getNextToken();
			if(currentToken.equals(Statement.COLON)) {
				
				return layout;

			} else invalidToken();
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
									
										} else invalidToken();
									} else invalidToken();
								} else invalidToken();

							} else invalidToken();
						} else invalidToken();
					} else invalidToken();
				}
		return null;
			}
	
	
	private void addWidget() throws Exception {
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
			
		case Widget.RADIO:
			addRadioButtonGroup();
			
			break;
			
		default: break;
			
		}
	}
	
	private void addTextField() throws Exception {
		
		if(Patterns.isNumeric(currentToken)) {
			screen = new JTextField(Integer.parseInt(currentToken));
			currentContainer.add(screen);
			
			getNextToken();
			if(currentToken.equals(Statement.SEMICOLON)) {
				
				getNextToken();
				if(isWidget(currentToken)) {
					addWidget();
				}
			} else invalidToken();
		} else invalidToken();	
	}
	
	private void addButton() throws Exception {
		if(currentToken.equals(Statement.QUOTE)) {
			
			getNextToken();
			if(Patterns.isAlphaNumeric(currentToken) || Patterns.isOperator(currentToken)) {
				JButton btn = new JButton(currentToken);
				btn.addActionListener(click -> screen.setText(
						screen.getText() + " " + btn.getText() + " "));
				currentContainer.add(btn);
				
				getNextToken();
				if(currentToken.equals(Statement.QUOTE)) {
					
					getNextToken();
					if(currentToken.equals(Statement.SEMICOLON)) {
			
							getNextToken();
							if(isWidget(currentToken)) {
								addWidget();
							}
						} else invalidToken();
					} else invalidToken();
			} else invalidToken();
		} else invalidToken();
	}
	
	private void addRadioButtonGroup() throws Exception {
		ButtonGroup group = new ButtonGroup();
		if(currentToken.equals(Widget.RADIO)) {
			getNextToken();
			addRadioButton(group);
			
			if(currentToken.equals(Statement.END)) {
				
				getNextToken();
				if(currentToken.equals(Statement.SEMICOLON)) {
						
					getNextToken();
					if(isWidget(currentToken)) {
						addWidget();
					}
				} else invalidToken();
			} else invalidToken();
		} else invalidToken();
	}
	
	private void addRadioButton(ButtonGroup group) throws Exception {
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
					if(currentToken.equals(Widget.RADIO)) {
						
						getNextToken();
						addRadioButton(group);
						}
					} else invalidToken();
				} else invalidToken();
			} else invalidToken();
		} else invalidToken();
	}
	
	private void addLabel() throws Exception {
		
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
					} else invalidToken();
				} else invalidToken();
			} else invalidToken();
		}
	
	
	private void addPanel() throws Exception {
		panel = new JPanel();
		
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
					


			} else invalidToken();
		} else invalidToken();
		}
	}
		
	private boolean isWidget(String token) {
		return token.equals(Widget.BUTTON) || token.equals(Widget.GROUP) || 
			token.equals(Widget.LABEL) || token.equals(Widget.PANEL) || token.equals(Widget.TEXTFIELD)||
			token.equals(Widget.RADIO);
			
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
	
