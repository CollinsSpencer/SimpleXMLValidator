package com.sgc.validate;

import java.util.Stack;

public class XMLValidator {

	public static boolean validate(String xmlDocument) {
		
		Stack<String> tags = new Stack<String>();
		String tag = "";
		boolean inDoubleQuote = false;
		boolean inTag = false;
		boolean endTag = false;
		boolean canBePrologTag = true;
		
		try {
			for (int i=0; i<xmlDocument.length(); i++) {
				char c = xmlDocument.charAt(i);
				if (c == '"') {
					inDoubleQuote = !inDoubleQuote;
				}
				if (c == '>') {
					return false;
				}
				else if (inTag) {
					// Comment Tag
					if (c == '!') {
						if (xmlDocument.charAt(i+1) == '-' && xmlDocument.charAt(i+2) == '-') {
							i += 3;
							while (xmlDocument.charAt(i) != '-' || xmlDocument.charAt(i+1) != '-') {
								i++;
							}
							i += 2;
							if (xmlDocument.charAt(i) != '>') {
								return false;
							}
						}
					}
					// Prolog Tag, which can only be placed at the beginning of the file
					else if (c == '?') {
						if (!canBePrologTag) {
							return false;
						}
						i++;
						while (xmlDocument.charAt(i) != '?') {
							i++;
						}
						i++;
						if (xmlDocument.charAt(i) != '>') {
							return false;
						}
					}
					// Standard Tag
					else {
						// if the tag has no body
						if (xmlDocument.charAt(i) == '>') {
							return false;
						}
						if (xmlDocument.charAt(i) == '/') {
							endTag = true;
							i++;
						}
						while (xmlDocument.charAt(i) != '>' && xmlDocument.charAt(i) != ' ') {
							tag += xmlDocument.charAt(i);
							i++;
						}
						if (xmlDocument.charAt(i) == ' ') {
							while (xmlDocument.charAt(i) != '>') {
								i++;
							}
						}
						if (endTag) {
							if (!tag.equals(tags.pop())) {
								return false;
							}
							endTag = false;
						} else {
							tags.push(tag);
						}
						tag = "";
					}
					inTag = false;
					canBePrologTag = false;
				}
				else if (c == '<') {
					if (inDoubleQuote) {
						return false;
					}
					inTag = true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		if (tags.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--aa--><a><p></p></a>";
		
		for (int i=0; i<xml1.length(); i++) {
			System.out.printf("%2d ", i);
		}
		System.out.println();
		for (int i=0; i<xml1.length(); i++) {
			System.out.printf("%2c ", xml1.charAt(i));
		}
		System.out.println();
		
		System.out.println(validate(xml1));
	}
	
}
