package application;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class Follow {
	private LinkedHashMap<Character, String> followSets = new LinkedHashMap<Character, String>();

	public LinkedHashMap<Character, String> followSets(LinkedHashMap<Character, String> rules, LinkedHashMap<Character, String> firstSets) {

	Graphics.debugMessages.clear();
		
	// Initialization of Follow Sets //
	Graphics.toDebug("Initialization of Follow Sets");
	for (char key : rules.keySet()) {
		if (followSets.isEmpty() && followSets.size() == 0)
			followSets.put(key, "$"); 
		else
			followSets.put(key, "");
	}
		
		for (char key : rules.keySet()) {
			Graphics.toDebug("Current key: " + key);
			for (char key2 : rules.keySet()) {
				String tempSets="";
				String temp = rules.get(key2);
				for (int i = 0; i < temp.length(); i++){
					boolean found= false;
					if (key == temp.charAt(i)) {
					 Graphics.toDebug("Found " + key + " in " + key2 + "->" + rules.get(key2));
					 if( key == temp.charAt(temp.length() -1) ){
						 tempSets+=followSets.get(key2);
						 Graphics.toDebug(key + " found in the last position of " + rules.get(key2) +".\nFollow("+ key2 +") added to Follow(" + key + ")");
					 }
					 else if( temp.charAt(i+1)=='|'){
						tempSets+=followSets.get(key2);
						temp= temp.substring(i+2);
						i=-1;
						Graphics.toDebug("\"OR\" symbol found right after " + key +".\nFollow("+ key2 +") added to Follow(" + key + ")");
					 }
					 else {
						 boolean terminal=false;
						 for (char c1 : Compiler.grammarTerminal)
							 if (c1 == temp.charAt(i + 1) && temp.charAt(i + 1) != 'ε'){
								 terminal=true; 
								 break;
							 }
						 if(terminal) {
								tempSets += temp.charAt(i + 1);
								Graphics.toDebug("Terminal '" + temp.charAt(i + 1) + "' found and inserted in Follow(" + key2 + ")");
								break;
						 }
						 else {
								String temp2 = firstSets.get(temp.charAt(i + 1));
								Graphics.toDebug("Non-Terminal found. Searching First(" + temp.charAt(i+1) + ")");
								for (int j2 = 0; j2 < temp2.length(); j2++) {
									if (temp2.charAt(j2) == 'ε'){
										found = true;
										Graphics.toDebug("Found 'ε' in First(" + temp.charAt(i+1) + ")");
										if (temp.charAt(i+1)!=temp.charAt(temp.length()-1)) {
											temp=temp.substring(0,i+1)+temp.substring(i+2);
											Graphics.toDebug(temp.charAt(i+1) + " deleted from " + temp);
										}
										else {
											Graphics.toDebug(temp.charAt(i+1) + " deleted from " + temp);
											temp=temp.substring(0,i+1);
										}
										if('ε' == temp2.charAt(temp2.length() - 1)) {
											tempSets+=temp2.substring(0,j2-1);
											Graphics.toDebug(temp2.substring(0,j2-1) + " added to Follow(" + key + ")");
										}
										else {
											tempSets+=temp2.substring(0,j2-1)+temp2.substring(j2+1);
											Graphics.toDebug(temp2.substring(0,j2-1)+temp2.substring(j2+1) + " added to Follow(" + key + ")");
										}
										i--;
										break;
									}
								}	
								if (found == false){
									tempSets+=temp2;
									Graphics.toDebug("First(" + temp.charAt(i + 1) + ") added to Follow(" + key + ")");
									break;
								}
						 }
					 	}
					}
				}
				if(tempSets!=""){
					String followString = followSets.get(key)+tempSets;
					char[] chars = followString.toCharArray();
					LinkedHashSet<Character> charSet = new LinkedHashSet<Character>();
					for (char c : chars) {
					    charSet.add(c);
					}

					StringBuilder sb = new StringBuilder();
					for (Character character : charSet) {
					    sb.append(character);
					}
					followString=sb.toString();
					StringBuilder result = new StringBuilder();

					for(int i = 0 ; i < followString.length(); i++)
					{
					   if(followString.charAt(i)!=','){
					   result = result.append(followString.charAt(i));
					   if(i == followString.length()-1)
					      break;
					   result = result.append(',');
					   }
					}
				 followSets.put(key,result.toString());
				}
					
			}
		}
		return followSets;
	}
}
