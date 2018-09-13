package main.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
	public static final Pattern numPattern = Pattern.compile("\\d+");
	public static final Pattern alphaPattern = Pattern.compile("[a-zA-Z]+");
	public static final Pattern alphaNumeric = Pattern.compile("\\w+");
	
	public static boolean isNumeric(String token) {
		Matcher numMatcher = numPattern.matcher(token);
		return numMatcher.matches();
	}
	
	public static boolean isAlphabetic(String token) {
		Matcher alphaMatcher = alphaPattern.matcher(token);
		return alphaMatcher.matches();
	}
	
	public static boolean isAlphaNumeric(String token) {
		Matcher alphaNumericMatcher = alphaNumeric.matcher(token);
		return alphaNumericMatcher.matches();
	}
}
