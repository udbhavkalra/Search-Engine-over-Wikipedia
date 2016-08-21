package wikipackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



class UserHandler extends DefaultHandler {

	long starttime = System.currentTimeMillis();
	
	
   /* Below tag flags are used to denote which tag is currently opened*/
	int count=0, countForFileNumber = 0;
	StringBuilder filetext = new StringBuilder();
   int indexInWholeText = 0;
   boolean pagetag = false, titletag = false, idtag = false, texttag = false, timestamp=false, idstored=false, infoboxtag=false, referencetag=false, extlinktag=false, cattag=false;
   /* Following StringBuilder Objects will store corresponding Strings to add that to the DataStructure Object & finally to TreeMap*/
   StringBuilder titleStringBuilder = new StringBuilder();
   StringBuilder idStringBuilder = new StringBuilder();   
   StringBuilder textStringBuilder = new StringBuilder();
   StringBuilder timeStringBuilder = new StringBuilder();
   StringBuilder infoBoxStringBuilder = new StringBuilder();
   StringBuilder referenceStringBuilder = new StringBuilder();
   StringBuilder extLinkStringBuilder = new StringBuilder();
   StringBuilder categoryStringBuilder = new StringBuilder();
   
//   String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};          
   public String[] stopwords = { "ref","a", "able", "about", "above", "abroad", "according", "accordingly", "across", "actually", "adj", "after", "afterwards", "again", "against", "ago", "ahead", "ain't", "all", "allow", "allows", "almost", "alone", "along", "alongside", "already", "also", "although", "always", "am", "amid", "amidst", "among", "amongst", "amoungst", "amount", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "back", "backward", "backwards", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "bill", "both", "bottom", "brief", "but", "by", "call", "came", "can", "cannot", "cant", "can't", "caption", "cause", "causes", "certain", "certainly", "changes", "clearly", "c'mon", "co", "co.", "com", "come", "comes", "computer", "con", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "couldn't", "course", "cry", "c's", "currently", "dare", "daren't", "de", "definitely", "describe", "described", "despite", "detail", "did", "didn't", "different", "directly", "do", "does", "doesn't", "doing", "done", "don't", "down", "downwards", "due", "during", "each", "edu", "eg", "eight", "eighty", "either", "eleven", "else", "elsewhere", "empty", "end", "ending", "enough", "entirely", "especially", "et", "etc", "even", "ever", "evermore", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "fairly", "far", "farther", "few", "fewer", "fifteen", "fifth", "fify", "fill", "find", "fire", "first", "five", "followed", "following", "follows", "for", "forever", "former", "formerly", "forth", "forty", "forward", "found", "four", "from", "front", "full", "further", "furthermore", "get", "gets", "getting", "give", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadn't", "half", "happens", "hardly", "has", "hasnt", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "here's", "hereupon", "hers", "herse", "herself", "he's", "hi", "him", "himse", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "hundred", "i", "i'd", "ie", "if", "ignored", "i'll", "i'm", "immediate", "in", "inasmuch", "inc", "inc.", "indeed", "indicate", "indicated", "indicates", "inner", "inside", "insofar", "instead", "interest", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "its", "it's", "itse", "itself", "i've", "just", "k", "keep", "keeps", "kept", "know", "known", "knows", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "likewise", "little", "look", "looking", "looks", "low", "lower", "ltd", "made", "mainly", "make", "makes", "many", "may", "maybe", "mayn't", "me", "mean", "meantime", "meanwhile", "merely", "might", "mightn't", "mill", "mine", "minus", "miss", "more", "moreover", "most", "mostly", "move", "mr", "mrs", "much", "must", "mustn't", "my", "myse", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needn't", "needs", "neither", "never", "neverf", "neverless", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "no-one", "nor", "normally", "not", "nothing", "notwithstanding", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "one's", "only", "onto", "opposite", "or", "other", "others", "otherwise", "ought", "oughtn't", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provided", "provides", "put", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "recent", "recently", "regarding", "regardless", "regards", "relatively", "respectively", "right", "round", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somebody", "someday", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "system", "take", "taken", "taking", "tell", "ten", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that's", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "there'd", "therefore", "therein", "there'll", "there're", "theres", "there's", "thereupon", "there've", "these", "they", "they'd", "they'll", "they're", "they've", "thick", "thin", "thing", "things", "think", "third", "thirty", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "till", "to", "together", "too", "took", "top", "toward", "towards", "tried", "tries", "truly", "try", "trying", "t's", "twelve", "twenty", "twice", "two", "un", "under", "underneath", "undoing", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "up", "upon", "upwards", "us", "use", "used", "useful", "uses", "using", "usually", "v", "value", "various", "versus", "very", "via", "viz", "vs", "want", "wants", "was", "wasn't", "way", "we", "we'd", "welcome", "well", "we'll", "went", "were", "we're", "weren't", "we've", "what", "whatever", "what'll", "what's", "what've", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "where's", "whereupon", "wherever", "whether", "which", "whichever", "while", "whilst", "whither", "who", "who'd", "whoever", "whole", "who'll", "whom", "whomever", "who's", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wonder", "won't", "would", "wouldn't", "yes", "yet", "you", "you'd", "you'll", "your", "you're", "yours", "yourself", "yourselves", "you've", "zero", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v","w", "x", "y", "z", "ga", "lt", "link", "fa", "eu", "bs", "hr", "ref", "ref", "en", "em", "refbegin","0","1","2","3","5","6","7","8","9"};
   public Set<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
   
   String[] removeThese = {"0", "1","2","3","4","5","6","7","8","9","|","'",",",".","|","`","~","!","@","#","$","%","^","&","*", "(",")","-","_","+","=",";",":","{","[","}","]", "\\","/","?","<",">"};
   Set<String> removeTheseSets = new HashSet<String>(Arrays.asList(removeThese));
   
   /* Following is to store the final index and to put that into the file */
//	TreeMap <String, HashMap<String, DataStructure >> tm = new TreeMap <String, HashMap<String, DataStructure >>();
	TreeMap <String, TreeMap<String, DataStructure >> tm = new TreeMap <String, TreeMap<String, DataStructure >>();
	int countForTreeMapFlush =0, fileNumber=1;
	String tempFileName = vars.filename;
	
   @Override
   public void startElement(String uri, 
      String localName, String qName, Attributes attributes)
         throws SAXException {
	   if(qName.equalsIgnoreCase("page")){
		   pagetag = true;
		   titleStringBuilder.setLength(0);										//At the start of each page tag, clear all StringBuilder Objects
		   textStringBuilder.setLength(0);
		   idStringBuilder.setLength(0);
		   timeStringBuilder.setLength(0);
		   categoryStringBuilder.setLength(0);
		   referenceStringBuilder.setLength(0);
		   extLinkStringBuilder.setLength(0);
		   indexInWholeText = 0;
	   }
	   else if(qName.equalsIgnoreCase("title"))													//check this else if or if
		   titletag = true;
	   else if(qName.equalsIgnoreCase("id"))
		   idtag = true;
//	   else if(qName.equalsIgnoreCase("timestamp"))
//		   timestamp = true;
	   else if(qName.equalsIgnoreCase("text"))
		   texttag = true;
   }

   @Override
   public void endElement(String uri, 
      String localName, String qName) throws SAXException {
	   	if(qName.equalsIgnoreCase("page")){
	   		countForTreeMapFlush++;
	   		pagetag = titletag = idtag = texttag = timestamp = false;
	   		//below line is commented to stop file 
	   		processText();												//this method will take the text, and check if the words are alphanumeric/stop words
	   		checkSizeAndFlush();
	   	}
	   	else if(qName.equalsIgnoreCase("id")){
	   		idtag = false;
	   		idstored = true;										//this will denote that the ID has been stored, so that it will not take the next 'id' tag
	   	}
	   	else if(qName.equalsIgnoreCase("title"))
	   		titletag = false;
	   	else if(qName.equalsIgnoreCase("text")){
	   		texttag = false;
	   		}
	   		
	   	else if (qName.equalsIgnoreCase("mediawiki")){
	   		countForTreeMapFlush = 100;
	   		vars.countIndexFiles = countForFileNumber;
	   		checkSizeAndFlush();
//	   		mergeFiles();
	   	}
      }// endelement
   
   //this function will merge all the files into one single file
//   private void mergeFiles() {
//	     
//	   int count =0, indexOfSlash=0;
//	   String minStringToWrite = "";
//	// TODO Auto-generated method stub
//	try{
//	
//	 for(int i=0; i<tempFileName.length(); i++){
//		 if(tempFileName.charAt(i)=='/')
//			 indexOfSlash = i;
//	 }
//	 Path filePath = Paths.get(tempFileName.substring(0,indexOfSlash+1)+"/MergedIndex");			//to get the folder/directory name
//	 
//	 if (!Files.exists(filePath)) 
//	    Files.createFile(filePath);
//	
//	String stringForFiles[] = new String[countForFileNumber];
//	BufferedReader bf_files_array[] = new BufferedReader[countForFileNumber];
////	
//	for(int i=0; i<countForFileNumber-1;i++){
//		bf_files_array[i] = new BufferedReader(new FileReader(tempFileName+"_"+i));
//		stringForFiles[i] = bf_files_array[i].readLine();
//	}
//	
//	minStringToWrite = stringForFiles[0];
//	for(int i=0; i<countForFileNumber-1; i++){
//		//-1 means ith is lesser than (i+1)th
//		if( (stringForFiles[i].substring(0,stringForFiles[i].indexOf(':'))+1).compareTo(minStringToWrite) == -1 )
//			minStringToWrite = stringForFiles[i];			
//	}
//		Files.write(filePath, minStringToWrite.getBytes(), StandardOpenOption.APPEND);
//       }//end of try
////       String everything = sb.toString();
//
//	
//	catch(Exception e){
//		e.printStackTrace();
//		System.out.print("Error in file merge !!");
//	}
//	
//}

public void checkSizeAndFlush() 
   {
//		char startCharacter = ' ';
	   if(countForTreeMapFlush==100){
		
//		FileWrite fw = new FileWrite();
		int x=0,t=0,i=0,r=0,e=0,c=0, tempFileNumber=0,lastslash=0;
		String filenameafterslash ="";
//		tempFileNumber = fileNumber++;
//		vars.filename = tempFileName+tempFileNumber;
		
		for(i=0; i<tempFileName.length(); i++){
			if(tempFileName.charAt(i)=='/')
				lastslash=i;
		}
		i=0;
		File dir = new File(tempFileName.substring(0, lastslash)+"/outputIndexTemporary");
		dir.mkdir();
		filenameafterslash = tempFileName.substring(lastslash+1,tempFileName.length() );
		vars.filename = tempFileName.substring(0, lastslash)+"/outputIndexTemporary/"+filenameafterslash+"_"+(countForFileNumber++);
		for(Entry<String, TreeMap<String, DataStructure>> entry : tm.entrySet()) {
		  String keyWord = entry.getKey();
		  TreeMap<String, DataStructure> value = entry.getValue();
		  
		  
//		  startCharacter = keyWord.charAt(0);
		  filetext.append(keyWord+":");
			  for (Entry<String,DataStructure> entry1 : value.entrySet()){
				  String tempID= entry1.getKey();
				  String tempString ="";
				  
				  DataStructure obj = entry1.getValue();
				  x=obj.textCount;
				  t=obj.titleCount;
				  i=obj.infoboxCount;
				  r=obj.refCount;
				  e=obj.extCount;
				  c=obj.catCount;
				  
				  if(x>0)
					  tempString += "x"+x;
				  if(t>0)
					  tempString += "t"+t;
				  if(i>0)
					  tempString += "i"+i;
			      if(r>0)
					  tempString += "r"+r;
				  if(e>0)
					  tempString += "e"+e;
				  if(c>0)
					  tempString += "c"+c;
				  filetext.append(tempID+tempString+";");
			  }
			  filetext.append("\n");

	  		try {
	//			fw.writeToFile(filetext);
				
				Path filePath = Paths.get(vars.filename);
	//  			Path filePath = filename;
			      if (!Files.exists(filePath)) {
			    	    Files.createFile(filePath);
			    	}
			    	Files.write(filePath, filetext.toString().getBytes(), StandardOpenOption.APPEND);
			    	
				filetext.setLength(0);
			} 
	  		catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//end of catch
		}//end of for loop
		tm.clear();																		//flush the treemap
		countForTreeMapFlush=0;	
		System.out.println(tm.size());
		long timethis  = System.currentTimeMillis();
        System.out.println((timethis-starttime)*0.001/60+"min.");
		//make countForTreeMapFlush=0 again, so to start over
	   }//end of outer if (c-ountForTreeMapFlush==10000)
		
}

   private void processText() {							//called after "page" tag is ended

	   String thisword = "";
	   char thischar = ' ';
	   
	   //for text data
	   processTextWithParameter(textStringBuilder,"text");							//to process words from textStringBuilder
	   
//	   idStringBuilder.setLength(0);
	   infoBoxStringBuilder.setLength(0);
	   idstored = false;
	   
	   //for title data
	   int length = titleStringBuilder.length();
	   for(int i=0; i<length; i++){
		   
		   thischar = titleStringBuilder.charAt(i);
		   
		   String temp = ""+thischar;    
		   if(Character.isLowerCase(thischar) || Character.isUpperCase(thischar) && (!removeTheseSets.contains(temp)))										//cross check this again
			   thisword += Character.toLowerCase(thischar);
		   
		   else if((thischar==' ' || (i==length-1 && thischar != ' ') || !(Character.isLowerCase(thischar) || Character.isUpperCase(thischar)) && (thisword.length()>0))) {
			   thisword = thisword.toLowerCase();
			   String temp1="";
			   for (int i1=0; i1<thisword.length(); i1++){
				   if(thisword.charAt(i1)>='a' && thisword.charAt(i1)<='z')
				   		temp1 += thisword.charAt(i1);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){
				   processWord(temp1,1);										//process this current word
			   thisword = "";
			   }
		   }
	   }
   }

private void processTextWithParameter(StringBuilder thisStringBuilder, String whichStringBuilder){
	 String thisword = "";
	 char thischar = ' ';
	 
	 int length = thisStringBuilder.length();
	   for(; indexInWholeText<length ; indexInWholeText++){
		   thischar = Character.toLowerCase(thisStringBuilder.charAt(indexInWholeText));
		   String temp =""+thischar;
// System.out.println("-------------------came ch:"+thischar);
//		   if(thischar=='h' && thisStringBuilder.charAt(indexInWholeText+1)=='t' && thisStringBuilder.charAt(indexInWholeText+2)=='t' && thisStringBuilder.charAt(indexInWholeText+3)=='p'){
//			   for(;indexInWholeText<length && (thisStringBuilder.charAt(indexInWholeText)!=' ' || thisStringBuilder.charAt(indexInWholeText)=='|' );indexInWholeText++);
//			   indexInWholeText++;
//		   }
	
		   if( (thischar>='a' && thischar<='z')  && !removeTheseSets.contains(temp))											//cross check this again
			   thisword += thischar;
		   
		   
		   else if(indexInWholeText==length-1 ){
//			   System.out.println("3 for ch:"+thischar);
			   String temp1 = ""+thischar;
			   if(thischar>='a' && thischar<='z' && !removeTheseSets.contains(temp1))
			   		thisword += Character.toLowerCase(thischar);

			   if(!stopWordSet.contains(thisword)  && thisword.length()>1){								//if the current word not present in the stopword HashSet
				   if(whichStringBuilder.equals("text"))
				   		processWord(thisword,0);			   
			   }
			   thisword = "";
		   }
		   
		   else if(thischar=='{' && indexInWholeText+1<length && thisStringBuilder.charAt(indexInWholeText+1)=='{'){
//			   System.out.println("4 for ch:"+thischar);
			  
			   thisword = thisword.toLowerCase();
			   String temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){
				   if(whichStringBuilder.equals("text"))
				   		processWord(temp1,0);										//process this current word
			   }
			   thisword = "";
			   
			   String strcite="cite";
			   int ind=indexInWholeText+2, citeind=1;										//citeind=1 => check from "ite" after checking "c"
			   
			   if(ind<length && Character.toLowerCase(thisStringBuilder.charAt(ind)) == 'c'){
				   ind++;
				   for(; ind<length && ind<indexInWholeText+6; ind++){					//to check for '{{cite'
					   if(Character.toLowerCase(thisStringBuilder.charAt(ind)) !=strcite.charAt(citeind))
						   break;
					   else
						   citeind++;
				   }   
				   if(ind==indexInWholeText+6)
					   for(;indexInWholeText<length && thisStringBuilder.charAt(indexInWholeText)!='}';indexInWholeText++); 
				   indexInWholeText++;													//to remove second '}' in "}}"
			   }
			   
			   else if(ind<length && Character.toLowerCase(thisStringBuilder.charAt(ind))=='i'){
				   ind++;
				   String wordcheck="";
				   for(;ind<length && ind<indexInWholeText+9; ind++)
					   wordcheck += Character.toLowerCase(thisStringBuilder.charAt(ind));
				   if(wordcheck.equals("nfobox") && indexInWholeText+10<length)
					   		makeInfoBoxText(indexInWholeText+10,length);									//get the text from the infobox
				   	   processInfoText(infoBoxStringBuilder,0);
				   	   indexInWholeText = infoBoxStringBuilder.length()+12+indexInWholeText;
			   }
			   
		   }//end of '{{' '}}' combination
		   
		   else if(thischar=='[' && indexInWholeText+1<length && thisStringBuilder.charAt(indexInWholeText+1)=='['){				//check for "file" and "image"
//			   System.out.println("5 for ch:"+thischar);
			   thisword = thisword.toLowerCase();
			   String temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){

				   if(whichStringBuilder.equals("text"))
				   		processWord(temp1,0);										//process this current word
					   
			   }
			   thisword = "";
			   
			   String wordcheck="";
			   int ind=indexInWholeText+2;
			   boolean exists = false;
			   for(; ind<indexInWholeText+6 && ind<length; ind++)
				   wordcheck += Character.toLowerCase(thisStringBuilder.charAt(ind));
			   //check for file
			   if(wordcheck.equals("file")){
				   indexInWholeText = indexInWholeText+6;
				   exists = true;
			   }
			   //check for image
			   else if(wordcheck.equals("imag") && indexInWholeText+6<length && Character.toLowerCase(thisStringBuilder.charAt(indexInWholeText+6))=='e'){
				   indexInWholeText = indexInWholeText+7;
				   exists = true;
			   }
			   
			   else if(wordcheck.equals("cate")){
				   wordcheck = "";
				  	for(;ind<length && ind<indexInWholeText+10; ind++)
				  		wordcheck += Character.toLowerCase(thisStringBuilder.charAt(ind));
				  	if(wordcheck.equals("gory")){
				  		indexInWholeText = ind+1;											//next character to ':'
				  		for(ind=ind+1; ind<length;ind++){
//				  			System.out.println(thisStringBuilder.charAt(ind));
				  			if(thisStringBuilder.charAt(ind)==']' || thisStringBuilder.charAt(ind)=='\n')
				  				break;
				  			categoryStringBuilder.append(Character.toLowerCase(thisStringBuilder.charAt(ind)));
//				  			System.out.println(categoryStringBuilder);
				  		}
				  		processInfoText(categoryStringBuilder,1);
//System.out.println(categoryStringBuilder);
				  		categoryStringBuilder.setLength(0);						//changed
//				  		indexInWholeText = categoryStringBuilder.length()+14+indexInWholeText;				//removed
				  		indexInWholeText = ind;										//added
				  	}
			   }
			   
			   	if(exists){
			   		for(;indexInWholeText<length && thisStringBuilder.charAt(indexInWholeText)!=']';indexInWholeText++);
				   	indexInWholeText++;
			   	}
		   }   // end of '[[' ']]' combination
		  
		   else if(thischar=='=' && indexInWholeText+1<length && thisStringBuilder.charAt(indexInWholeText+1)=='='){
			   thisword = thisword.toLowerCase();
			   String temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){

				   if(whichStringBuilder.equals("text"))
				   		processWord(temp1,0);										//process this current word
					   
			   }
			   thisword = "";
			   
			   int indexOfRef = 0, indexOfExternal=0;
			   indexInWholeText+=2;
			   if(indexInWholeText<length && thisStringBuilder.charAt(indexInWholeText)==' '){			//check for "== References ==" and for "== External links"
				   String tempstring ="";
				   for(int i=indexInWholeText+1; i<length && i<indexInWholeText+14; i++)
					   tempstring += thisStringBuilder.charAt(i);
				   if(tempstring.equals("References =="))
					   indexOfRef = indexInWholeText+14;
				   else if(tempstring.equals("External link"))
					   indexOfExternal = indexInWholeText+18;
//				   System.out.println(tempstring);
			   }
			   else{														//for "==References==" or "==External links=="
				   String tempstring ="";
				   for(int i=indexInWholeText; i<length && i<indexInWholeText+12; i++)
					   tempstring += thisStringBuilder.charAt(i);
				   if(tempstring.equals("References=="))
					   indexOfRef = indexInWholeText+13;
				   else if(tempstring.equals("External lin"))
					   indexOfExternal = indexInWholeText+17;
				   
			   }
			   
			   if(indexOfRef!=0){									//means References is present here
				   int i=indexOfRef;
				   for (; i<length && thisStringBuilder.charAt(i)!='=';i++)
					   referenceStringBuilder.append(Character.toLowerCase(thisStringBuilder.charAt(i)));
				   processInfoText(referenceStringBuilder,2);
				   indexInWholeText=i;
			   }
			   
			   else if(indexOfExternal!=0){							//means External links is present here
				   
				   int i=indexOfExternal;
				   boolean catflag = false;							//till we get "[[Category"
				   while(!catflag && i<length){
					   for(;i<length && thisStringBuilder.charAt(i)!='['; i++)
						   extLinkStringBuilder.append(Character.toLowerCase(thisStringBuilder.charAt(i)));
					   if(i+9>=length)
						   break;
					   if(thisStringBuilder.substring(i+1, i+10).equals("[Category"))
							   catflag = true;
					   else 
						   i++;
				   }
				   processInfoText(extLinkStringBuilder, 3);
				   indexInWholeText = i;
			   }
		   }// end of '==' check
		   
		   else if((thischar==' '  && (thisword.length()>1)) || (removeTheseSets.contains(temp))) {
//			   System.out.println("2 for ch:"+thischar);
			   thisword = thisword.toLowerCase();
			   String temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){

				   if(whichStringBuilder.equals("text"))
				   		processWord(temp1,0);										//process this current word
					   
			   }
			   thisword = "";
		   }
		   
	   }
}
   
//whichProcess==0 means info, 1 means categoryStringBuilder, '2' for referenceStringBuilder, '3' for 'external link'
private void processInfoText(StringBuilder whichStringBuilder, int whichProcess) {													//to process infoboxStringBuilder or categoryStringBuilder
	// TODO Auto-generated method stub
	StringBuilder thisStringBuilder = whichStringBuilder;
	
	int length = thisStringBuilder.length();	//whichProcess=0 means infoStringBuilder, =1 means category
//System.out.println("size:"+length);
	char thischar=' ';
	String thisword="";
	
	for(int thisIndex=0; thisIndex<length; thisIndex++){
		   thischar = Character.toLowerCase(thisStringBuilder.charAt(thisIndex));
		   String temp1=""+thischar;
		   
		   if(thisIndex==length-1 ){													//changed.. from indexwholetext
			   
			   if(thischar>='a' && thischar<='z' && !removeTheseSets.contains(temp1))
			   		thisword += Character.toLowerCase(thischar);
			   if(!stopWordSet.contains(thisword)  && thisword.length()>1){								//if the current word not present in the stopword HashSet
//				   if(whichStringBuilder.equals("text"))
//				   		processWord(thisword,0);											//changed.. removed
				   if(whichProcess==1)
					   processWord(thisword,3);
				   
			   }
			   thisword = "";
		   }
		   
		   else if(thischar>='a' && thischar<='z' && !removeTheseSets.contains(temp1)){
			   if(!removeTheseSets.contains(temp1))
				   thisword += thischar;
		   	   temp1="";
		   }
 
		  
		   
		   
		   else if(thischar=='{' && thisIndex+1<length && thisStringBuilder.charAt(thisIndex+1)=='{'){
			   //modified below line
			   thisword = thisword.toLowerCase();
			   temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1) && temp1.length()>1){
				   if(whichProcess==0)
			   			processWord(temp1,2);										//process this current word
				   else if(whichProcess==1)
					   processWord(temp1,3);										//3 for [[Category
				   else if(whichProcess==2)
					   processWord(temp1,4);										//4 for ==References
				   else if(whichProcess==3)
					   processWord(temp1,5);										//5 for ==External links
			   }										//process this current word
			   thisword = "";
			   
			   thisIndex += 2;
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1.toLowerCase()) && temp1.length()>1){	
				   if(whichProcess==0)
			   			processWord(temp1,2);										//process this current word
				   else if(whichProcess==1)
					   processWord(temp1,3);										//3 means "[[Category" for processWord
				   else if(whichProcess==2)
					   processWord(temp1, 4);										//4 for ==References for processword
				   else if(whichProcess==3)
					   processWord(temp1, 5);										// 5 for External links
			   thisword = "";
			   
			   String strcite="cite",tempword="";
			   int ind=thisIndex;										//citeind=1 => check from "ite" after checking "c"
			   
				   for(; ind<length && ind<thisIndex+4; ind++)					//to check for '{{cite'
					   tempword += Character.toLowerCase(thisStringBuilder.charAt(ind));
				   if (tempword.equals(strcite) && ind<length && thisStringBuilder.charAt(ind)==' '){
					   for(;thisIndex<length && thisStringBuilder.charAt(thisIndex)!='}';thisIndex++);
					   thisIndex++;
				   }
			   }
		   }//end of '{{' '}}' checking
		   
		   else if(thischar=='[' && thisIndex+1<length && thisStringBuilder.charAt(thisIndex+1)=='['){				//check for "file" and "image"
			   
			   thisword = thisword.toLowerCase();
			   temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1) && temp1.length()>1){
				   if(whichProcess==0)
			   			processWord(temp1,2);										//process this current word
				   else if(whichProcess==1)
					   processWord(temp1,3);										//3 for [[Category
				   else if(whichProcess==2)
					   processWord(temp1,4);										//4 for ==References
				   else if(whichProcess==3)
					   processWord(temp1,5);										//5 for External links
			   }										//process this current word
			   thisword = "";
			   
			   String wordcheck="";
			   thisIndex+=2;									//modified changed
			   int ind=thisIndex;
			   boolean exists = false;
			   for(; ind<thisIndex+4 && ind<length; ind++)
				   wordcheck += Character.toLowerCase(thisStringBuilder.charAt(ind));
			   //check for file
			   if(wordcheck.equals("file")){
				   thisIndex = thisIndex+4;
				   exists = true;
			   }
			   //check for image
			   else if(wordcheck.equals("imag") && thisIndex+4<length && Character.toLowerCase(thisStringBuilder.charAt(thisIndex+4))=='e'){
				   thisIndex = thisIndex+6;												//modified
				   exists = true;
			   }
//			   
			   	if(exists){
					   for(;thisIndex<length && thisStringBuilder.charAt(thisIndex)!=']';thisIndex++);
					   thisIndex++;
			   	}
			   	
		   }
		   
		 //process it if following condition is 'true'
		   else if((thischar==' '  && (thisword.length()>1)) || (removeTheseSets.contains(temp1))) {

			   thisword = thisword.toLowerCase();
			   temp1="";
			   for (int i=0; i<thisword.length(); i++){
				   if(thisword.charAt(i)>='a' && thisword.charAt(i)<='z')
				   		temp1 += thisword.charAt(i);
			   }
			   if(!stopWordSet.contains(temp1) && temp1.length()>1){
				   if(whichProcess==0)
			   			processWord(temp1,2);										//process this current word
				   else if(whichProcess==1)
					   processWord(temp1,3);										//3 for [[Category
				   else if(whichProcess==2)
					   processWord(temp1,4);										//4 for ==References
				   else if(whichProcess==3)
					   processWord(temp1,5);										//5 for External links
			   }										//process this current word
			   thisword = "";
		   }
	}
} //end of processInfoText(whichStringBuilder, whichProcess)

private void makeInfoBoxText(int i, int textLength) {
	int bracesCount = 1, indexInWholeText=i;													//for count of '{' braces
	String twoChars = "";
	for(; indexInWholeText+1<textLength; indexInWholeText++){
			twoChars = ""+ textStringBuilder.charAt(indexInWholeText) + textStringBuilder.charAt(indexInWholeText+1);
		if(twoChars.equals("}}"))
			bracesCount--;
		else if(twoChars.equals("{{"))
			bracesCount++;
		if(bracesCount!=0)
			infoBoxStringBuilder.append(textStringBuilder.charAt(indexInWholeText));						//appending the text to the infoBoxStringBuilder 
		else{
			break;
		}												//if we encounter the end of the infobox
	}													//return new value of "i" in textStringBuilder
}

//
  //type=0 means "text data" , & type=1 means "title data"
   /* type =2 means InfoBox
    * type =3 means Category*/
	
private void processWord(String thisword,int whichType) {				//this method will check if the word is not a stop word, apply stemming and store in TreeMap
	
	Stemmer stemmer = new Stemmer();
	int length = thisword.length();
	for(int i=0; i<length; i++)
		stemmer.add(thisword.charAt(i));
	
	stemmer.stem();
	{  String u;
      u = stemmer.toString();
      thisword = u;
	}
	
		if(stopWordSet.contains(thisword.toLowerCase()) || removeTheseSets.contains(thisword))
			return;
	
		if (!tm.containsKey(thisword)  ){
			
			DataStructure dsobj = new DataStructure(0,0,0,0,0,0);
			if(whichType == 0)												//if textStringBuilder is in process
				dsobj = new DataStructure(0, 1, 0, 0, 0, 0);
			else if (whichType == 1)											//if titleStringBuilder is in process
				dsobj = new DataStructure(1, 0, 0, 0, 0, 0);
			else if(whichType == 2)												//if infobox is in process
				dsobj = new DataStructure(0, 0 ,1, 0, 0, 0);
			else if(whichType == 3)											
				dsobj = new DataStructure(0, 0 ,0, 0, 1, 0);					// if category
			else if(whichType == 4)												//if ==References		
				dsobj = new DataStructure(0, 0 ,0, 1, 0, 0);
			else if(whichType == 5)												//if ==External links		
				dsobj = new DataStructure(0, 0 ,0, 0, 0, 1);
			
//			HashMap<String, DataStructure> tempHashMap = new HashMap<String, DataStructure>();
			TreeMap<String, DataStructure> tempHashMap = new TreeMap<String, DataStructure>();
			tempHashMap.put(idStringBuilder.toString(), dsobj);
			tm.put(thisword, tempHashMap);									//push into the TreeMap object
		}
			
		else{																//if key is present in the TreeMap
			
			if (tm.get(thisword).containsKey(idStringBuilder.toString())){										//if document contained, increment the textCount
				if(whichType==0)
					tm.get(thisword).get(idStringBuilder.toString()).textCount++;
				else if (whichType==1)
					tm.get(thisword).get(idStringBuilder.toString()).titleCount++;
				else if (whichType == 2)
					tm.get(thisword).get(idStringBuilder.toString()).infoboxCount++;
				else if (whichType == 3)
					tm.get(thisword).get(idStringBuilder.toString()).catCount++;
				else if (whichType == 4)
					tm.get(thisword).get(idStringBuilder.toString()).refCount++;
				else if(whichType == 5)														//if ==External links		
					tm.get(thisword).get(idStringBuilder.toString()).extCount++;
			}
			else{	
				//if key is there in TreeMap but this doc is not there
				DataStructure dsobj = new DataStructure(0,0,0,0,0,0);
				if(whichType==0)												//if textStringBuilder is in process
					dsobj = new DataStructure(0, 1, 0, 0, 0, 0);
				else if (whichType==1)											//if titleStringBuilder is in process
					dsobj = new DataStructure(1, 0, 0, 0, 0, 0);
				else if(whichType == 2)												//if infobox is in process
					dsobj = new DataStructure(0, 0 ,1, 0, 0, 0);
				else if(whichType == 3)												//if category is in process
					dsobj = new DataStructure(0, 0 ,0, 0, 1, 0);
				else if(whichType == 4)												//if reference is in process
					dsobj = new DataStructure(0, 0 ,0, 1, 0, 0);
				else if(whichType == 4)												//if external is in process
					dsobj = new DataStructure(0, 0 ,0, 0, 0, 1);				
				
				tm.get(thisword).put(idStringBuilder.toString(),dsobj);
			}
			
			
		}
}

@Override
   public void characters(char ch[], 
      int start, int length) throws SAXException {
	   if(idtag && !idstored)
		   idStringBuilder.append(ch,start,length);
	   else if(titletag)
		   titleStringBuilder.append(ch,start,length);
	   else if(texttag)
		   textStringBuilder.append(ch,start,length);
	   else if(timestamp)
		   timeStringBuilder.append(ch,start,length);
   }
}
	
