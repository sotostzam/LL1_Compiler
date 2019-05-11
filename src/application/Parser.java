package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class Parser {
	
	private String[][] parsingTable;
	private Stack<Character> stack = new Stack<Character>();
	private String input = "";
	private Character notRecognized;
	
	public boolean checkRules(String input) {
		String[] commas = input.split(",");
		boolean check = false;

		if (Compiler.rules != null) Compiler.rules.clear();
		for (int i = 0; i < input.length(); i++) {
			char s = input.charAt(i);
			
			// Check Grammar Rules //
			for (int j = 0; j < Compiler.grammarNonTerminal.length; j++) {
				if (s == Compiler.grammarNonTerminal[j]) {
					check = true;
					break;
				} else {
					check = false;
				}
			}
			if (check == false) {
				for (int j = 0; j < Compiler.grammarTerminal.length; j++) {
					if (s == Compiler.grammarTerminal[j]) {
						check = true;
						break;
					} else {
						check = false;
					}
				}
			}
			if (!check) {
				notRecognized = s;
				break;
			}
		}
		if (check) {
			for (int i = 0; i < commas.length; i++) {
				String[] velos = commas[i].split("->");
				Compiler.rules.put(velos[0].charAt(0), velos[1]);
			}
		}
		return check;
	}
	
	public Character getUnrecognizedChar () {
		return notRecognized;
	}
	
	public int constructTable(LinkedHashMap<Character, String> rules, LinkedHashMap<Character, String> firstSets, LinkedHashMap<Character, String> followSets) {	
		
		// Initializing the parsing table
		ArrayList<Character> nonTerms = new ArrayList<Character>();
		ArrayList<Character> chars = new ArrayList<Character>();
		int countNonTerm=0, countTerm=0;
		for (char key : rules.keySet()) {
			nonTerms.add(key);
			countNonTerm++;
			for (int i=0;i<rules.get(key).length();i++) {
				for (int j=0;j<Compiler.grammarTerminal.length;j++) {
					if (rules.get(key).charAt(i)==Compiler.grammarTerminal[j] && rules.get(key).charAt(i)!='ε' && rules.get(key).charAt(i)!='|') {
						if (!(chars.indexOf(rules.get(key).charAt(i))>0)) {
							chars.add(rules.get(key).charAt(i));
							countTerm++;
						}
					}
				}
			}
		}
		chars.add('$');
		countTerm++;
		parsingTable = new String[countNonTerm+1][countTerm+1];
		parsingTable[0][0] = " ";
		for (int i=1;i<parsingTable[0].length;i++) {
			parsingTable[0][i] = chars.get(i-1).toString();
		}
		for (int i=1;i<parsingTable.length;i++) {
			parsingTable[i][0] = nonTerms.get(i-1).toString();
		}
		
		// Filling table
		for (char key : rules.keySet()) {
			String value = rules.get(key);
			String[] tempRules = value.split("\\|");
			int keepRow=0;
			for (int i=0;i<parsingTable.length;i++) {
				if (parsingTable[i][0].charAt(0)==key) {
					keepRow=i;
					break;
				}
			}
			for (int i=0;i<tempRules.length;i++){
				boolean isTerminal = false;
				for (int j=0;j<Compiler.grammarTerminal.length;j++) {
					if (tempRules[i].charAt(0)==Compiler.grammarTerminal[j]) {
						isTerminal=true;
						break;
					}
				}
				if (isTerminal) {
					if (tempRules[i].charAt(0)=='ε') {
						String getFollow = followSets.get(key);
						String[] getFollowValues = getFollow.split("\\,");
						for (int k=0; k<getFollowValues.length;k++) {
							for (int l=1;l<parsingTable[0].length;l++) {
								if (getFollowValues[k].charAt(0)==parsingTable[0][l].charAt(0)) {
									if (parsingTable[keepRow][l]==null) {
										parsingTable[keepRow][l]=key+"->"+tempRules[i];
										break;
									}
									else {
										System.out.println("This Grammar is not of type LL(1)");									
										return 2;
									}
								}
							}
						}
						break;
					}
					else {
						for (int k=1;k<parsingTable[0].length;k++) {
							if (tempRules[i].charAt(0)==parsingTable[0][k].charAt(0)) {
								if (parsingTable[keepRow][k]==null) {
									parsingTable[keepRow][k]=key+"->"+tempRules[i];
									break;
								}
								else {
									System.out.println("This Grammar is not of type LL(1)");									
									return 2;
								}
							}
						}
					}
				}
				else {
					String getFirst = firstSets.get(tempRules[i].charAt(0));
					String[] getFirstValues = getFirst.split("\\,");
					for (int k=0; k<getFirstValues.length;k++) {
						for (int l=1;l<parsingTable[0].length;l++) {
							if (getFirstValues[k].charAt(0)==parsingTable[0][l].charAt(0)) {
								if (parsingTable[keepRow][l]==null) {
									parsingTable[keepRow][l]=key+"->"+tempRules[i];
									break;
								}
								else {
									System.out.println("This Grammar is not of type LL(1)");									
									return 2;
								}
							}
						}
					}
					if (i!=tempRules.length) continue;
					else break;
				}
			}	
		}
		return 0;
	}
	
	public String[][] getParsingTable() {
		return parsingTable;
	}

	public void initializeString(LinkedHashMap<Character, String> rules, String inputString, String[][] parsingTable) {
		stack.push('$');
		Graphics.stackInteract('$', 0);
		input = inputString + '$';
		Graphics.inputInteract(input);
		stack.push(Compiler.rules.keySet().toArray()[0].toString().charAt(0));
		Graphics.stackInteract(Compiler.rules.keySet().toArray()[0].toString().charAt(0), 0);
	}
	
	public void autoAnalyzeString(String type, double timer) {
		switch (type) {
		case "Timer":
			Task<Void> task = new Task<Void>() {
				int result = 0;
				@Override
				protected Void call() throws Exception {
					while (result == 0) {
						Platform.runLater(() -> {
							result = analyzeString();
						});
						Thread.sleep((long)(timer * 1000));
					}
					return null;
				}
			};
			Thread th = new Thread(task);
			th.start();
			break;
		case "OneTime":
			int result = 0;
			while (result == 0) {
				result = analyzeString();
			}
			break;
		}
	}
	
	public int analyzeString() {
		boolean loop = false;
		if (!loop) {
			char top = stack.peek();
			int keepRow = 0;
			String value = "";
			if (top == '$' && input.charAt(0) == '$') {
				Graphics.toStringAnalysis("The string is recognized", 2);
				loop = true;
				return 2;
			}
			else if (top == input.charAt(0)) {
				Graphics.toStringAnalysis("Symbol absorption: " + input.charAt(0), 0);
				stack.pop();
				Graphics.stackInteract(' ', 1);
				input = input.substring(1);
				Graphics.inputInteract(input);
			}
			else {
				for (int j = 1; j < parsingTable.length; j++) {
					if (parsingTable[j][0].charAt(0) == top) {
						keepRow = j;
						break;
					}
				}
				for (int j = 1; j < parsingTable[0].length; j++) {
					if (keepRow==0) {
						Graphics.toStringAnalysis("String not recognized", 1);
						return 1;
					}
					else if (parsingTable[0][j].charAt(0) == input.charAt(0)) {
						value = parsingTable[keepRow][j];
						if (value == null) {
							Graphics.toStringAnalysis("String not recognized", 1);
							loop = true;
							return 1;
						}
						value = value.substring(3);
						Graphics.toStringAnalysis("Rule: " + parsingTable[keepRow][0] + "->" + value, 0);
						break;
					}
				}
				if (value != "") {
					if (value.charAt(0) != 'ε') {
						stack.pop();
						Graphics.stackInteract(' ', 1);
						// Value from right to left
						for (int j = value.length() - 1; j >= 0; j--) {
							stack.push(value.charAt(j));
							Graphics.stackInteract(value.charAt(j), 0);
						}
					} 
					else {
						Graphics.toStringAnalysis("Symbol removal: " + stack.peek(), 0);
						stack.pop();
						Graphics.stackInteract(' ', 1);
					}
				} 
				else {
					loop = true;
					Graphics.toStringAnalysis("String not recognized", 1);
					return 1;
				}
			}
		}
		return 0;
	}
}