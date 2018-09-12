package Runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Parser.GUIParser;

public class Runner {

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner scanner = new Scanner(new File("src/window.txt"));
		
		GUIParser parser = new GUIParser(scanner);
		parser.createGUI();
	}

}
