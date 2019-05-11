package application;

import java.util.LinkedHashMap;

public class First {

	private LinkedHashMap<Character, String> firstSets = new LinkedHashMap<Character, String>();
	private LinkedHashMap<Character, Boolean> firstSetsComplete = new LinkedHashMap<Character, Boolean>();
	
	public LinkedHashMap<Character, String> getFirstSets(LinkedHashMap<Character, String> rules) {
		for (Character key : rules.keySet()) {
			  System.out.println(key);
			}
		return firstSets;
	}

	public LinkedHashMap<Character, String> firstSets(LinkedHashMap<Character, String> rules) {
		
		// Calculation of Initial First Sets //
		Graphics.debugMessages.clear();
		Graphics.toDebug("Initialization of First Sets");
		String tempString;
		for (char key : rules.keySet()) {
			tempString = Character.toString(rules.get(key).charAt(0));
			for (int i = 0; i < rules.get(key).length(); i++) {
				if (rules.get(key).charAt(i) == '|') {
					tempString += "," + rules.get(key).charAt(i + 1);
				}
			}
			firstSets.put(key, tempString);
			firstSetsComplete.put(key, false);
		}

		// Calculation of Final First Sets //
		LinkedHashMap<Character, String> tempRuleSets = new LinkedHashMap<Character, String>();
		LinkedHashMap<Character, String> replaceSets = new LinkedHashMap<Character, String>();
		
		// Loop while there are non-terminal symbols
		boolean terminalsOnly = false;
		while (!terminalsOnly) {
			terminalsOnly = true;	
			// Iterate through all Keys
			for (char key : firstSets.keySet()) {
				// Check if current set is found
				if (firstSetsComplete.get(key) == true) {
					continue; 
				}
				String infoLabel = "";
				infoLabel += "Trying to find First(" + key + ")";
				boolean isParseable = true;
				
				// Iterate through all Characters of current Key
				check:
				for (int i = 0; i < firstSets.get(key).length(); i++) {	
					
					// Iterate through NonTerminal Grammar Characters
					for (char c : Compiler.grammarNonTerminal) {
						if (firstSets.get(key).charAt(i) == c) {
							infoLabel += "\nFound non-terminal symbol: " + firstSets.get(key).charAt(i);
							terminalsOnly = false;
							
							// Check if child symbol contains non-terminals
							for (int j=0; j < firstSets.get(firstSets.get(key).charAt(i)).length(); j++) {
								for (char c1 : Compiler.grammarNonTerminal) {
									if (firstSets.get(firstSets.get(key).charAt(i)).charAt(j) == c1) {
										infoLabel += "\nIs First(" + firstSets.get(key).charAt(i) + ") found?"
												   + "\nFirst(" + firstSets.get(key).charAt(i) + ") not found yet.";
										isParseable = false;
										break check;
									}
								}
							}							
						}
					}
				}
				
				if (!isParseable) {
					infoLabel += "\nFirst(" + key + ") can't be found right now.";
					Graphics.toDebug(infoLabel);
					continue;
				}
				else {
					
					// Initialize tempRuleSets and replaceSets
					infoLabel += "\nChecking rule: " + key + "->" + rules.get(key);
					int commasFound = 0;
					for (int i=0;i<firstSets.get(key).length();i++) {
						int separaFound=0, separatorStart = 0, separatorEnd = 0;
						if (firstSets.get(key).charAt(i) == ',') {
							commasFound++;
						}
						else {
							for (char c : Compiler.grammarNonTerminal) {
								if (firstSets.get(key).charAt(i) == c) {															
									for (int j=0; j<rules.get(key).length(); j++) {
										if (rules.get(key).charAt(j) == '|') {
											separaFound++;
										}
										if (commasFound == separaFound) {
											separatorStart = j+1;
											separatorEnd = 0;
											for (int k=j+1; k<rules.get(key).length(); k++) {
												if (rules.get(key).charAt(k) == '|') {
													separatorEnd = k;
													break;
												}
											}
											break;
										}
									}
									if (replaceSets.isEmpty() || !replaceSets.containsKey(key)) {
										replaceSets.put(firstSets.get(key).charAt(i), firstSets.get(firstSets.get(key).charAt(i)));
									}
									if (tempRuleSets.isEmpty() || !tempRuleSets.containsKey(key)) {
										if (commasFound>0) {
											if (separatorEnd==0) {
												tempRuleSets.put(firstSets.get(key).charAt(i), rules.get(key).substring(separatorStart));
												break;
											}
											else {
												tempRuleSets.put(firstSets.get(key).charAt(i), rules.get(key).substring(separatorStart,separatorEnd));
												break;
											}
										}
										else {
											if (separatorEnd>0) {
												tempRuleSets.put(firstSets.get(key).charAt(i), rules.get(key).substring(0,separatorEnd));
												break;
											}
											else {
												tempRuleSets.put(firstSets.get(key).charAt(i), rules.get(key));
												break;
											}
										}
									}
								}
							}
						}
					}
				}
				
				// Replace non-terminal symbols			
				for (char keyReplace : replaceSets.keySet()) {									
						String newValue = replaceSets.get(keyReplace);
						boolean replaceComplete=false;
						while (!replaceComplete) {
							// Check if epsilon exists
							if (newValue.charAt(newValue.length()-1) == 'ε') {
								infoLabel += "\nFound the empty symbol (epsilon)";
								Graphics.toDebug(infoLabel);
								tempRuleSets.put(keyReplace, tempRuleSets.get(keyReplace).substring(1));
								if (tempRuleSets.get(keyReplace).length()>0) {
									newValue = newValue.substring(0, newValue.length()-1) + tempRuleSets.get(keyReplace).charAt(0);
									for (char c : Compiler.grammarTerminal) {
										if (newValue.charAt(newValue.length()-1) == c && newValue.charAt(newValue.length()-1) != 'ε') {
											replaceSets.put(keyReplace, newValue);
											tempRuleSets.remove(keyReplace);
											replaceComplete=true;
											break;
										}
									}
								}
								else {
									tempRuleSets.remove(keyReplace);
									replaceComplete=true;
								}
							}
							else {
								for (char c : Compiler.grammarTerminal) {
									if (newValue.charAt(newValue.length()-1) == c) {
										tempRuleSets.remove(keyReplace);
										replaceComplete=true;
										break;
									}
								}
								if (!replaceComplete) {
									String tempValue = firstSets.get(newValue.charAt(newValue.length()-1));
									newValue = newValue.substring(0,newValue.length()-1);
									for (int i=0; i<tempValue.length();i++) {
										if (newValue.indexOf(tempValue.charAt(i))>0 && tempValue.charAt(i) != ',') {
											if (i==0){
												tempValue = tempValue.substring(2);
											}
											else {
												tempValue = tempValue.substring(0,i) + tempValue.substring(i+1);
											}
										}
									}
									newValue += tempValue;
									replaceSets.put(keyReplace, newValue);
								}
							}
						}
				}	
				
				// Replace non-Terminals with terminals in first sets
				String tempReplace = firstSets.get(key);
				for (int i=0;i<tempReplace.length();i++) {
					if (tempReplace.charAt(i)!=',') {
						for (char c : replaceSets.keySet()) {
							if (tempReplace.charAt(i)==c) {
								if (i==0) {
									tempReplace = replaceSets.get(c) + tempReplace.substring(1);
								}
								else {
									for (int j=0;j<replaceSets.get(c).length();j++) {
										if (tempReplace.indexOf(replaceSets.get(c).charAt(j))>0 && replaceSets.get(c).charAt(j) != ',') {
											if (j==0) {
												replaceSets.put(c, replaceSets.get(c).substring(2));
											}
											else {
												replaceSets.put(c, (replaceSets.get(c).substring(0,j) + replaceSets.get(c).substring(j+2)));
											}
										}
									}
									String tempKeep = tempReplace.substring(i+1);
									tempReplace = tempReplace.substring(0, i) + replaceSets.get(c) + tempKeep;
								}
							}	
						}						
					}					
				}
				replaceSets.clear();
				firstSets.put(key, tempReplace);
				firstSetsComplete.put(key, true);
				infoLabel += "\nFirst(" + key + ")->{" + tempReplace + "}";
				Graphics.toDebug(infoLabel);
			}
		}
		Graphics.toDebug("First Sets Found successfully");
		
		// Delete "ε" of the first sets //
		if (Compiler.deleteEpsilon) {
			for (char key : firstSets.keySet()) {
				String deleteEpsilon = firstSets.get(key).substring(0, firstSets.get(key).length());
				for (int i = 0; i < firstSets.get(key).length(); i++) {
					if (deleteEpsilon.charAt(deleteEpsilon.length() - 1) == 'ε') {
						deleteEpsilon = deleteEpsilon.substring(0, deleteEpsilon.length() - 2);
					}
				}
				firstSets.put(key, deleteEpsilon);
			}
		}
		return firstSets;
	}
}
