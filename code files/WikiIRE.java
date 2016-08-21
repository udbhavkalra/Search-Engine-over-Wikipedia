package wikipackage;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class PairTitle{
	public PairTitle(long i, long j) {
		id = i;
		position = j;
	}
	long id;
	long position;
}

public class WikiIRE {	
	static String fileNameForTheIndex = "";
	static long valueSeekTitleFile = 0;
	static List<PairTitle> pairoftitle = new ArrayList<PairTitle>();
	static Map<String,Double> fieldMap = new HashMap<String, Double>();			//to deal with field query
	static Map<Character, Double> valuesmap = new HashMap<Character, Double>();
	
	public static void main(String[] args){
	   	  
      try {
    	  valuesmap.put('t', 1000.0);
    	  valuesmap.put('x', 1.0);
    	  valuesmap.put('c', 0.05);
    	  valuesmap.put('i', 0.05);
    	  valuesmap.put('r', 0.001);
    	  valuesmap.put('e', 0.001);
    	  
    	  long startTime = System.currentTimeMillis();
//    	  File inputFile = new File("try.txt");
//         File inputFile = new File("test.xml");
    	 vars.inputFile = args[0];
    	 vars.filename = args[1];
    	 fileNameForTheIndex =  args[1];
//    	 int indexSlash = 0;
//    	 for(int i=0; i<fileNameForTheIndex.length(); i++){
// 			if(fileNameForTheIndex.charAt(i)=='/')										//to get the directory name..so as to make the merged file index file
// 				indexSlash = i;
// 		}
//    	 fileNameForTheIndex = fileNameForTheIndex.substring(0,indexSlash);
    	 
         SAXParserFactory factory = SAXParserFactory.newInstance();
         @SuppressWarnings("unused")
		SAXParser saxParser = factory.newSAXParser();
         @SuppressWarnings("unused")
		UserHandler userhandler = new UserHandler();
//         saxParser.parse(vars.inputFile, userhandler); 
//         mergeFiles();
//         LevelOneLevelTwoIndexCreate();
         makeTitleMappingIndex();
         makeHashTitle();
                 
  		int flag = 1;
  		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
     	while(flag!=0){
         searchQuery();
         System.out.println("Press 0 to exit.. or any other number to continue..");
         flag = scanner.nextInt();
     	}
         long endTime   = System.currentTimeMillis();
         long totalTime = endTime - startTime;
         System.out.println(totalTime*0.001/60+"min.");
      } catch (Exception e) {
    	  System.exit(0);
      }
   }

	private static void makeHashTitle() throws IOException {
		try{
			int indexSlash = 0;
			String docstring = "", offsetstring = "";
			for(int i =0; i<fileNameForTheIndex.length(); i++){
				if(fileNameForTheIndex.charAt(i)=='/')
					indexSlash = i;
			}
			@SuppressWarnings("resource")
			BufferedReader bf = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleSecondLevelIndex"));
			String cline = "";
			
			while( (cline = bf.readLine()) != null ){
				for(int i=0; i<cline.length(); i++){
					
					while(i<cline.length() && cline.charAt(i)!=':'){
						docstring += cline.charAt(i);
						i++;
					}
					if(i+1<cline.length())
						offsetstring = cline.substring(i+1, cline.length());
					if(offsetstring.length()>0 && docstring.length()>0){
						PairTitle p = new PairTitle(Long.parseLong(docstring),Long.parseLong(offsetstring));
						pairoftitle.add(p);
					}
					docstring = "";
					offsetstring = "";
				}
			}
		}
		catch(Exception e){
//			return;
		}
		
	}

	@SuppressWarnings( "resource" )
	private static void makeTitleMappingIndex() throws IOException {
	// TODO Auto-generated method stub
	int indexSlash=0, firstNumber = 0;
	for(int i=0; i<fileNameForTheIndex.length(); i++){
		if(fileNameForTheIndex.charAt(i)=='/')
			indexSlash = i;
	}
	BufferedReader bfTitleFile = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleMapping"));
	Path filePath = Paths.get(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleSecondLevelIndex");		//to get the folder/directory name + / MergedIndex for our main index file
	 
	 if (!Files.exists(filePath)) 
	    Files.createFile(filePath);
	
	String currLine = "", tempLine = "", currWord = "";
	if((currLine  = bfTitleFile.readLine())!=null)											//for the first line
		 Files.write(filePath, (currLine.substring(0,currLine.indexOf(' '))+":0\n").getBytes(), StandardOpenOption.APPEND);
	 firstNumber=currLine.length()+1;
	 
	 while(true){
		 int count = 0;																			//to take 1500 lines for title
		 while((currLine = bfTitleFile.readLine())!=null && count<10000){
			 tempLine = currLine;
			 firstNumber = firstNumber+currLine.length()+1;
			 count++;
		 }
		 if(count==10000){																				//we have reached at count=1500
			 if(tempLine.contains(" ")==true){
				 currWord = currLine.substring(0,currLine.indexOf(' '));
				 Files.write(filePath, (currWord+":"+firstNumber+"\n").getBytes(), StandardOpenOption.APPEND);
				 firstNumber = firstNumber+currLine.length()+1;
			 }
		 }
		 
		 else if(currLine==null){																	//we reached at the EOF
//			 firstNumber = firstNumber+tempLine.length()+1;
			 currWord = tempLine.substring(0,tempLine.indexOf(' '));
			 Files.write(filePath, (currWord+":"+firstNumber+"\n").getBytes(), StandardOpenOption.APPEND);
			 break;
		 }
		 else
			 break;
	 }	
}
	//query processing
	@SuppressWarnings("resource")
	private static void searchQuery() throws Exception {
	TreeMap<String, Integer> querytm = new TreeMap<String, Integer>();
	String thisword = "", currentLine="", prevWord = "", prevLine="";		//to store currentLine for indexReading, prev word to store last word to compare
	char thischar = ' ';
	int i=0, j=0, size=0, length=0, indexForPositions = 0, compareWithPrev =0, compareWithThis=0,somethingIsThere = 0;
	long[] positions;
//	String[] wordsInQuery = new String[10000];
	System.out.print("Enter the query:");
	Scanner scanner = new Scanner(System.in);					//input from the user
	String query = scanner.nextLine();
	String[][] everyTerm = new String[1000][10];										//max 100 terms allowed
	Map <String,Integer> docIDMap = new HashMap<String,Integer>();
	String[] docEntryOrder = new String[10];
	int docentryIndex = 0;
	size = query.length();
	query= query.toLowerCase();
	
	for(i=0; i<query.length()-1; i++){												//to check whether this is a field query or normal query
		if(i+1<query.length() && query.charAt(i)>='a' && query.charAt(i)<='z' && query.charAt(i+1)==':'){
			fieldQuery(query);
			fieldMap.clear();
			return;
		}
	}
	
	for(i=0; i<size; i++){												//to take all words out, remove stop words, stem it, add to treemap
		thischar = query.charAt(i);
		if(thischar>='a' && thischar<='z' && i!=query.length()-1){
			thisword += thischar;
			somethingIsThere = 1;
		}
		else if(somethingIsThere==1){
			if(i==query.length()-1)
				thisword += thischar;
			
			if(!querytm.containsKey(thisword) && !(vars.stopWordSet.contains(thisword)) ){				
				Stemmer stemmer = new Stemmer();
				length = thisword.length();
				for(j=0; j<length; j++)
					stemmer.add(thisword.charAt(j));
				
				stemmer.stem();
				{  String u;
			      u = stemmer.toString();
			      thisword = u;
				}
				querytm.put(thisword, 1);
			} //end of if (check
			thisword ="";
			somethingIsThere=0;
		}
	}
	size = querytm.size();
	positions = new long[size];											//to store the byte positions for every word
	indexForPositions = 0;
	//following code is to have Random Access from the file
	int indexSlash=0;
	for(i=0; i<fileNameForTheIndex.length(); i++){
		if(fileNameForTheIndex.charAt(i)=='/')
			indexSlash = i;
	}
	 
	 BufferedReader bufTempLine = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/SecondIndexFile"));
	 if((currentLine = bufTempLine.readLine()) != null){							//for the first line to store "prevWord"
		 size = currentLine.length();
		 prevWord = "";
		 for(i=0; i<size; i++){
			 thischar = currentLine.charAt(i);
			 if(thischar==':')
				 break;
			 prevWord += thischar;
		 }
		 prevLine = currentLine;											//to get the byte positions if needed in future
	 }

	 //start of traversal in secondlevelindex
	 for(String thiskey : querytm.keySet()){			//check in SecondLevelIndex and go to every key and find the byte position in the firstLevelIndex
		 while( ((currentLine = bufTempLine.readLine()) != null) && indexForPositions<querytm.size()){				//read till the end of file or every key has got the positions

			 size = currentLine.length();
			 thisword = "";
			 for(i=0; i<size; i++){
				 thischar = currentLine.charAt(i);
				 if(thischar==':')
					 break;
				 thisword += thischar;
			 }
			 compareWithPrev = prevWord.compareTo(thiskey);
			 compareWithThis = thisword.compareTo(thiskey);
			 if( (compareWithPrev==0) || (compareWithPrev<0 && compareWithThis>0)){	//thiskey==prev || (prev<thiskey<thisword)
				 size = prevLine.length();
				 for(j=0; j<size; j++){
					 while(j<size && prevLine.charAt(j)!=':') j++;							//till we reach the colon
					 if(j+1<size){
						 positions[indexForPositions] = Long.parseLong(prevLine.substring(j+1,size));
						 indexForPositions++;
						 break;
					 }
					 
				 }//end of for
				 break;
			 }//end of if
			 
			 else if(compareWithThis==0){										//if thiskey==thisword
				 size = currentLine.length();
				 for(j=0; j<size; j++){
					 while(j<size && currentLine.charAt(j)!=':') j++;							//till we reach the colon
					 if(j+1<size){
					 	positions[indexForPositions] = Long.parseLong(currentLine.substring(j+1,size));
					 	indexForPositions++;
					 	break;
					 }
					 
				 }//end of for
				 break;
			 }
			 
			 else{
				 prevWord =  thisword;
				 prevLine = currentLine;
			 }
			 
		 }
	 }//end of searching in secondLevelIndex
	 
	 
	 indexForPositions=0;
	 RandomAccessFile raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/FirstIndexFile", "rw");
	 try{
	 raf.seek(0);									//to seek to the first key's position
	 }
	 catch(Exception e){
		 System.out.println("Try searching something else..");
		 return;
	 }
	 //start of traversal in firstlevelindex
	 for(String thiskey : querytm.keySet()){			//check in SecondLevelIndex and go to every key and find the byte position in the firstLevelIndex
		 raf.seek(positions[indexForPositions]);
		 while( ((currentLine = raf.readLine()) != null) && indexForPositions<querytm.size()){				//read till the end of file or every key has got the positions
			 size = currentLine.length();
			 thisword = "";
			 for(i=0; i<size; i++){
				 thischar = currentLine.charAt(i);
				 if(thischar==':')
					 break;
				 thisword += thischar;
			 }
			 compareWithThis = thisword.compareTo(thiskey);

			 if( (compareWithThis==0)){	//thiskey==prev || (prev<thiskey<thisword)
				 size = currentLine.length();
				 for(j=0; j<size; j++){
					 while(j<size && currentLine.charAt(j)!=':') j++;							//till we reach the colon
					 if(j+1<size){
						 positions[indexForPositions] = Long.parseLong(currentLine.substring(j+1,size));
						 indexForPositions++;
						 break;
					 }
					 
				 }//end of for
				 break;
			 }//end of if
			 
			 else if(compareWithThis > 0){										// if word not found
			 	positions[indexForPositions] = -1;
			 	indexForPositions++;
			 	break;
			 }			 
		 }
	 }//end of searching in firstLevelIndex
	 
	 String thisdocid = "";
	 int currentTermIndex = 0;
	 for(int l=0; l<indexForPositions; l++){									//for 'l' = 0 to no of terms-1
		 long no = positions[l];
		 if(no==-1){
		 		continue;
		 }
		 raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/mergedfile", "rw");
		 raf.seek(no);
		 
		 byte[] b = new byte[5000];
//		 char ch = ' ';
		 @SuppressWarnings("unused")
		int readthis = raf.read(b, 0, 4900), docIDStarted=0;
		 currentTermIndex = 0;
		 for(i=0; i<b.length && (char)b[i]!='\n' && currentTermIndex<10; i++){
			 
			 if( (char)b[i] == ':' || (char)b[i]==';'){
				 docIDStarted = 1;
				 if(thisdocid.length()>0)
					 everyTerm[l][currentTermIndex++] = thisdocid;
				 thisdocid = "";
			 }
			
			 else if( (char)b[i] >= '0' && (char)b[i] <= '9' && docIDStarted==1){
				 thisdocid += (char)b[i];
			 }
				 
			 else if( (char)b[i] >= 'a' && (char)b[i] <= 'z' )
				 docIDStarted = 0;
			 
		 }
	}
	
	int flag=1;
	for(j=0; j<10; j++){
		for(i=0; i<querytm.size(); i++){
			if(everyTerm[i][j]!=null){

				if(docIDMap.size()>=10)
					flag=0;
					
				if( !docIDMap.containsKey(everyTerm[i][j])){
					if(flag==1){
						docIDMap.put(everyTerm[i][j], 1);
						docEntryOrder[docentryIndex++] = everyTerm[i][j];
					}
				}
				else{
//					count = docIDMap.get(everyTerm[i][j]);
//					docIDMap.remove(everyTerm[i][j]);
					docIDMap.put(everyTerm[i][j], docIDMap.get(everyTerm[i][j])+1);
				}
			}//end if 'some docID is present there'
		}
	}
	
	int sizeofDocIDMap = docIDMap.size();
	Set<java.util.Map.Entry<String, Integer>> set = docIDMap.entrySet();
	List<java.util.Map.Entry<String, Integer>> list = new ArrayList<java.util.Map.Entry<String, Integer>>(set);
    Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
    {
        public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
        {
            return (o2.getValue()).compareTo( o1.getValue() );
        }
    } );	

    for(Map.Entry<String, Integer> entry:list){
    	if(entry.getValue()>1){
//    		System.out.println(entry.getKey());
    		docIDMap.remove(entry.getKey());
    	}    	
    }

	for(i=0; i<sizeofDocIDMap; i++){
		if(docIDMap.containsKey(docEntryOrder[i])){
			getTitleName(docEntryOrder[i]);
			docIDMap.remove(docEntryOrder[i]);
			}
	}
		
}// end of query processing method

	@SuppressWarnings("unused")
	private static void fieldQuery(String query) throws IOException {		
		List<String> tchar = new ArrayList<String>();
		List<String> bchar = new ArrayList<String>();
		List<String> ichar = new ArrayList<String>();
		List<String> cchar = new ArrayList<String>();
		List<String> rchar = new ArrayList<String>();
		List<String> echar = new ArrayList<String>();
		
		int i=0, len = query.length();
		char thischar = ' ';
		String word = "";
		for(i=0; i<len-1; i++){
			thischar = query.charAt(i);
			if(thischar>='a' && thischar<='z' && query.charAt(i+1)>=':'){
				i=i+2;
				if(i+2>=len)	break;
				
				while(i<len){
					
					if(i+1<len && query.charAt(i)>='a' && query.charAt(i)<='z' && query.charAt(i+1)==':'){
						thischar = query.charAt(i);
						i += 2;
					}
					while(i<query.length() && query.charAt(i)>='a' && query.charAt(i)<='z'){
						word += query.charAt(i);
						++i;
					}
					++i;
					switch(thischar){
					
						case 't': 
								  tchar.add(word);
								  break;
						case 'b': 
								  bchar.add(word);
								  break;
						case 'c': 
								  cchar.add(word);
								  break;
						case 'i': 
								  ichar.add(word);
								  break;
						case 'r': 
								  rchar.add(word);
								  break;
						case 'e': 
								  rchar.add(word);
								  break;
					}
				}//now,we have got the t,b,..Lists     -- end of while
			}//end of if
		}//end of for query.length()
			
//		
		for(i=0; i<tchar.size(); i++)
			getPostingList(tchar.get(i), 't');
		for(i=0; i<bchar.size(); i++)
			getPostingList(bchar.get(i), 'x');
		for(i=0; i<cchar.size(); i++)
			getPostingList(cchar.get(i), 'c');
		for(i=0; i<ichar.size(); i++)
			getPostingList(ichar.get(i), 'i');
		for(i=0; i<rchar.size(); i++)
			getPostingList(rchar.get(i), 'r');
		for(i=0; i<echar.size(); i++)
			getPostingList(echar.get(i), 'e');
		
		Set<Entry<String, Double>> set = fieldMap.entrySet();
		List<java.util.Map.Entry<String, Integer>> list = new ArrayList<java.util.Map.Entry<String, Integer>>();
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
			    {
			        public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			        {
			            return (o2.getValue()).compareTo( o1.getValue() );
			        }
			    } );
		
		int count =0 ;
		for(String key: fieldMap.keySet()){
			if(count==10)
				break;
			getTitleName(key);
			count++;
		}
		
		
	}
	

	@SuppressWarnings("unused")
	public static void getPostingList(String word, char type) throws IOException{

		int indexSlash=0, i=0, size = 0,lineLength=0, compareWithPrev=0, compareWithThis=0;
		char thischar = ' ';
		long position = 0;
		for(i=0; i<fileNameForTheIndex.length(); i++){
			if(fileNameForTheIndex.charAt(i)=='/')
				indexSlash = i;
		}
		@SuppressWarnings("resource")
		BufferedReader bufTempLine = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/SecondIndexFile"));
		
		String currentLine ="", thisword = "", prevWord ="", prevLine="";

		
		if((currentLine = bufTempLine.readLine()) != null){							//for the first line to store "prevWord"
			 size = currentLine.length();
			 prevWord = "";
			 for(i=0; i<size; i++){
				 thischar = currentLine.charAt(i);
				 if(thischar==':')
					 break;
				 prevWord += thischar;
			 }
			 prevLine = currentLine;											//to get the byte positions if needed in future
		 }
		
		//start searching from the index filesthiskey=thisList.get(i);
			while( ((currentLine = bufTempLine.readLine()) != null)){				//read till the end of file or every key has got the positions

				 lineLength = currentLine.length();
				 thisword = "";
				 for(i=0; i<lineLength; i++){
					 thischar = currentLine.charAt(i);
					 if(thischar==':')
						 break;
					 thisword += thischar;
				 }
				 compareWithPrev = prevWord.compareTo(word);
				 compareWithThis = thisword.compareTo(word);
				 if( (compareWithPrev==0) || (compareWithPrev<0 && compareWithThis>0)){	//thiskey==prev || (prev<thiskey<thisword)
					 size = prevLine.length();
					 for(int j=0; j<size; j++){
						 while(j<size && prevLine.charAt(j)!=':') j++;							//till we reach the colon
						 if(j+1<size){
							 position = Long.parseLong(prevLine.substring(j+1,size));
							 break;
						 }
						 
					 }//end of for
					 break;
				 }//end of if
				 
				 else if(compareWithThis==0){										//if thiskey==thisword
					 size = currentLine.length();
					 for(int j=0; j<size; j++){
						 while(j<size && currentLine.charAt(j)!=':') j++;							//till we reach the colon
						 if(j+1<size){
						 	position= Long.parseLong(currentLine.substring(j+1,size));
						 	break;
						 }
						 
					 }//end of for
					 break;
				 }
				 
				 else{
					 prevWord =  thisword;
					 prevLine = currentLine;
				 }
				 
			 }
		 //end of searching in secondLevelIndex

		@SuppressWarnings("resource")
		 RandomAccessFile raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/FirstIndexFile", "rw");
//		 try{
//		 raf.seek(0);									//to seek to the first key's position
//		 }
//		 catch(Exception e){
//			 e.printStackTrace();
////			 System.out.println("Try searching something else..");
//		 }
		 //start of traversal in firstlevelindex
		
		//check in SecondLevelIndex and go to every key and find the byte position in the firstLevelIndex
			 raf.seek(position);
			 while( ((currentLine = raf.readLine()) != null)){				//read till the end of file or every key has got the positions
	
				 size = currentLine.length();
				 thisword = "";
				 for(i=0; i<size; i++){
					 thischar = currentLine.charAt(i);
					 if(thischar==':')
						 break;
					 thisword += thischar;
				 }
				 compareWithThis = thisword.compareTo(word);

				 if( (compareWithThis==0)){	//thiskey==prev || (prev<thiskey<thisword)
					 size = currentLine.length();
					 for(int j=0; j<size; j++){
						 while(j<size && currentLine.charAt(j)!=':') j++;							//till we reach the colon
						 if(j+1<size){
							 position = Long.parseLong(currentLine.substring(j+1,size));
							 break;
						 }
						 
					 }//end of for
					 break;
				 }//end of if
				 
				 else if(compareWithThis > 0){										// if word not found
				 	position  = -1;
				 	break;
				 }			 
			 }//end of traversal in second
			 
			 double rank = 0;
			 String returnThis = "", valuemultiply = "";
			 String thisdocid = "";
			 int currentTermIndex = 0, readdocid = 0;									//for 'l' = 0 to no of terms-1
				 long no = position;
//				 if(no==-1){
//				 		return;										//check
//				 }
				 if(no!=-1){
					 raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/mergedfile", "rw");
					 raf.seek(position);
					 byte[] b = new byte[5000];
					 char ch = ' ';
					 int readthis = raf.read(b, 0, 4900), docIDStarted=0, started = 0;
//					 currentTermIndex = 0;
					 
					 for(i=0; i<b.length && (char)b[i]!='\n' ; i++){
						 
						 if((char)b[i] == ':')
							 started = 1;
						 
						 if((char)b[i] == ';' && readdocid==1) {
//							 docIDStarted = 1;
							 
							if(thisdocid.length()>0 && thisdocid.indexOf(type)>0){							//means the character is present there
								int k=0;
								currentTermIndex++;
								while(thisdocid.charAt(k)>='0' && thisdocid.charAt(k)<='9'){
									returnThis += thisdocid.charAt(k);
									k++;
								}
							while(thisdocid.charAt(k)!=type) k++;
							k++;
							
							while(k<thisdocid.length() && thisdocid.charAt(k)>='0' && thisdocid.charAt(k)<='9')
								valuemultiply += thisdocid.charAt(k++);
							
							rank = Double.parseDouble(valuemultiply) * (Double)valuesmap.get(type);
							if(fieldMap.containsKey(returnThis)){
								fieldMap.put(returnThis, (fieldMap.get(returnThis)+rank));
							}
							else{
								fieldMap.put(returnThis, rank);
							}

							 thisdocid = "";
							 started = 1;
							 while(k<thisdocid.length() && thisdocid.charAt(k)!= ';') k++;
//								returnThis += ',';
							}
						 }
						
						 else if(started==1){
							 while(i<b.length && (char) b[i]!=';' )
								 thisdocid += (char)b[i++];
							 readdocid = 1;
							 started = 0;
						 }
							 
//						 else if( (char)b[i] >= 'a' && (char)b[i] <= 'z' )
//							 docIDStarted = 0;
						 valuemultiply = "";
						 returnThis = "";
					 }
				 }
}//end of method

	@SuppressWarnings("unused")
	private static void getTitleName(String docID) throws IOException {
		try{
			long doctosearch = Long.parseLong(docID), offsetRequired=0, parameterID= Long.parseLong(docID);
			int size = pairoftitle.size(), indexSlash = 0;
			int i=0, j=size-1, mid=0, pos=0;
			mid = (i+j)/2;
			
			while(i<j){
				mid = (i+j)/2;
	
				if(pairoftitle.get(mid).id > doctosearch){
					j= mid-1;
				}
				else if(pairoftitle.get(mid).id < doctosearch){
					if(mid+1<size && pairoftitle.get(mid+1).id > doctosearch){
						pos = mid;
						break;
					}
					else
						i = mid+1;
				}
				else{
					pos = mid;
					break;
				}
			}
				offsetRequired = pairoftitle.get(mid).position;
			
			try{
			for(i=0; i<fileNameForTheIndex.length(); i++){
				if(fileNameForTheIndex.charAt(i)=='/')
					indexSlash = i;
			}
			@SuppressWarnings("resource")
			RandomAccessFile raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleMapping", "rw");
			raf.seek(offsetRequired);
			String thisID = "",curline;
			while( (curline=raf.readLine()) != null){
				for(i=0; i<curline.length() && curline.charAt(i)!=' '; i++){
					thisID += curline.charAt(i);
				}
				try{
				if(parameterID == Long.parseLong(thisID)){
						thisID = "";
						break;
					}
	//				return curline.substring(i+1,curline.length());
//				}
				}
				catch(Exception e){
//					return;
				}
			thisID = "";
			}
//			System.out.println(curline.substring(i+1,curline.length()));
			System.out.println(curline);
			}
			catch(Exception e){
				//do nothing
//				return;
			}
		}
		catch(Exception e){
//			return;
		}
	}//end of new gettitle method
	
	
//	private static void getTitleName(String docID) throws Exception {
//		try{
//		int indexSlash = 0;
//		for(int i=0; i<fileNameForTheIndex.length(); i++){
//			if(fileNameForTheIndex.charAt(i)=='/')
//				indexSlash = i;
//		}
//		
//		BufferedReader bfTitleFile = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleSecondLevelIndex"));
//		
////		RandomAccessFile bfTitleFile = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleSecondLevelIndex","r");
////		bfTitleFile.seek(valueSeekTitleFile);
//		String curline = "", thisID = "", prevID = "10", prevLine = "10:0";
//		long parameterID = Long.parseLong(docID), fileID = 0, offsetfound = 0;
//		int i=0;
//		
//		//to get the offset from the second level index, so to go to titleMapping using that offsetFound
//		while( (curline = bfTitleFile.readLine())!=null ){
////	System.out.println("line len:"+curline.length()+" line:"+curline);
////			if(valueSeekTitleFile==0)
////				valueSeekTitleFile = curline.length()+1;	
////			else
////				valueSeekTitleFile = curline.length();			//changed check
//			for(i=0; i<curline.length() && curline.charAt(i)!=':'; i++){
//				thisID += curline.charAt(i);
//				}
//			fileID = Long.parseLong(thisID);
//			
//			
//			if( (parameterID > Long.parseLong(prevID) && parameterID < fileID) || parameterID==Long.parseLong(prevID) ){
//				i=0;
//				while(prevLine.charAt(i)!=':') i++;
//				offsetfound = Long.parseLong(prevLine.substring(i+1, prevLine.length()));
//				break;
//			}
//			PairTitle obj = new PairTitle(fileID,);
//			pairoftitle.add(obj);
//			
//		prevID = thisID; prevLine = curline;
//		thisID = "";
//		}
////		valueSeekTitleFile = offsetfound;
//		
////		System.out.println("in bw:"+valueSeekTitleFile);
//		
////		bfTitleFile = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleMapping"));
//		RandomAccessFile raf = new RandomAccessFile(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/titleMapping", "rw");
//		raf.seek(offsetfound);
//		thisID = "";
//		while( (curline=raf.readLine()) != null){
//			for(i=0; i<curline.length() && curline.charAt(i)!=' '; i++){
//				thisID += curline.charAt(i);
//			}
//			try{
//			if(parameterID==Long.parseLong(thisID)){
//				break;
////				return curline.substring(i+1,curline.length());
//			}
//			}
//			catch(Exception e){
////				e.printStackTrace();
//			}
//		thisID = "";
//		}
//		System.out.println(curline);
//		
//		}
//		catch(Exception e){
//			//do nothing
//		}
//	}// end of getTitleName method..

	@SuppressWarnings("unused")
	private static void LevelOneLevelTwoIndexCreate() throws IOException {
	// TODO Auto-generated method stub
	String inputFileName = fileNameForTheIndex, currLine = "", tempLine = "", currWord = "";
//	boolean flagForWrite = false;												//false means number in first, true-> number in second number
	int indexSlash = 0, i=0;
	long firstNumber=0;
	for(i=0; i<inputFileName.length(); i++){
		if(inputFileName.charAt(i)=='/')
			indexSlash = i;
	}
		
	inputFileName = inputFileName.substring(0,indexSlash);											//directory name before slash '/'
	
//	for(i=0; i<inputFileName.length(); i++){
//		if(inputFileName.charAt(i)=='/' && i!=indexSlash)
//			secondSlash = i;
//	}
	
	@SuppressWarnings("resource")
	BufferedReader bfForFile = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/MergedIndex"));
	
	Path filePath = Paths.get(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/FirstLevelIndex");		//to get the folder/directory name + / MergedIndex for our main index file
	 
	 if (!Files.exists(filePath)) 
	    Files.createFile(filePath);
	
	currLine = bfForFile.readLine();											//for the first line
	if(currLine!=null){															//if the line exists
			i=0;
			while(currLine.charAt(i)!=':')
				i++;
			Files.write(filePath, (currLine.substring(0,i+1)+"0\n").getBytes(), StandardOpenOption.APPEND);
//			firstNumber = (currLine.length()-i-1) + 2 +i;								//+2 means ':' and '\n' and 'i' means length of the word
			firstNumber = currLine.length() +1;
	}
	
	while((currLine = bfForFile.readLine())!=null){
		i=0;
		while(currLine.charAt(i)!=':')
			i++;													//now 'i' will be the size of the current word
		Files.write(filePath, (currLine.substring(0,i+1)+firstNumber+"\n").getBytes(), StandardOpenOption.APPEND);
		firstNumber += currLine.length() +1;
	}
	
	//following code makes level 2 index
	bfForFile = new BufferedReader(new FileReader(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/FirstLevelIndex"));
	
	filePath = Paths.get(fileNameForTheIndex.substring(0,indexSlash)+"/outputIndexTemporary/SecondLevelIndex");		//to get the folder/directory name + / MergedIndex for our main index file
	 
	 if (!Files.exists(filePath)) 
	    Files.createFile(filePath);
	
	 if((currLine = bfForFile.readLine())!=null)											//for the first line
		 Files.write(filePath, (currLine+"\n").getBytes(), StandardOpenOption.APPEND);
	 firstNumber=currLine.length()+1;
	 
	 while(true){
		 int count = 0;																			//to take 1000 lines
		 while((currLine = bfForFile.readLine())!=null && count<1000){
			 tempLine = currLine;
			 firstNumber = firstNumber+currLine.length()+1;
			 count++;
		 }
		 if(currLine==null){																	//we reached at the EOF
//			 firstNumber = firstNumber+tempLine.length()+1;
			 currWord = tempLine.substring(0,tempLine.indexOf(':'));
			 Files.write(filePath, (currWord+":"+firstNumber+"\n").getBytes(), StandardOpenOption.APPEND);
			 break;
		 }
		 else if(count==1000){																				//we have reached at count=1000
			 if(tempLine.contains(":")==true){
				 currWord = currLine.substring(0,currLine.indexOf(':'));
				 Files.write(filePath, (currWord+":"+firstNumber+"\n").getBytes(), StandardOpenOption.APPEND);
				 firstNumber = firstNumber+currLine.length()+1;
			 }
		 }
		 else
			 break;

//		 count=0;
//		 break;
	 }
	 
	 //following code is to have Random Access from the file
//	 RandomAccessFile raf = new RandomAccessFile(inputFileName+"/FirstLevelIndex", "rw");
//	 raf.seek(187110);
//	 byte[] b = new byte[inputFileName.length()+11];
//	 char ch = ' ';
//	 int readthis = raf.read(b, 0, 50);
//		 for(i=0; i<b.length; i++){
//			 ch = (char) b[i];
//			 System.out.print(ch);
			 
//		 }
	}
//	
	

	@SuppressWarnings("unused")
	private static void mergeFiles() throws Exception {
		// TODO Auto-generated method stub
		
		//this is for current run
		vars.countIndexFiles = 2717;														//no of files ie serial number +1 (start from '0')
		TreeMap<String, String> tmForMerge = new TreeMap<String, String>();
		
											//change this after the modification
		int countfiles = vars.countIndexFiles, indexLastSlash=0, i=0, indexOfColon = 0, tempIndexFileLine=0, buffer=1000000;
		int count=countfiles, firsttime=1;
		
		String[] smallestWord = new String[countfiles];
		String smallestWordString = "";									//to store the smallest while iterating
		String fullString = "";
		String writeThisToFile = "";
		//ArrayList<Integer> fileIndexToBeSearched = new ArrayList<Integer>();			//to convert this into the hashSet to store the files to be accessed		
		int fileIndexToSearch[] = new int[countfiles];
		//ArrayList<Integer> incrementPointerForFile = new ArrayList<Integer>();		//for which files, we have not reached the EOF
		String thisFileString[] = new String[countfiles];
		int toIncrementLine[] = new int[countfiles];
		for(i=0; i<countfiles; i++){
			fileIndexToSearch[i] = i;
			toIncrementLine[i] =1;
		}
		
		String tempFileName = vars.filename;									//output file name location entered used
		for(i=0; i<tempFileName.length(); i++){
			if(tempFileName.charAt(i)=='/')										//to get the directory name..so as to make the merged file index file
				indexLastSlash = i;
		}
		
		BufferedReader bufReaderFiles[] = new BufferedReader[countfiles];		//to open the index partitions created
		for(i=0; i<countfiles; i++)
			bufReaderFiles[i] = new BufferedReader(new FileReader(fileNameForTheIndex+"_"+i));			//to read these index files created
		
		Path filePath = Paths.get(tempFileName.substring(0,indexLastSlash+1)+"/MergedIndex");		//to get the folder/directory name + / MergedIndex for our main index file
		 
		 if (!Files.exists(filePath)) 
		    Files.createFile(filePath);
		 i=0;												//read first line from the first file
		 indexOfColon = 0;		//take the string after the indexed word
		 
		int countKeys,flag=0;
		for(i=0; i<countfiles; i++){															//for first line only
			flag = 0; 
			while(flag==0){
				thisFileString[i]= "";
//				 String tempString = bufReaderFiles[i].readLine();
//				 System.out.println(tempString);
				 thisFileString[i] = (bufReaderFiles[i].readLine());
				 
				 if(thisFileString[i]==null)
					 break;
				 
					 for(int j=0; j<thisFileString[i].length(); j++){
						 if(thisFileString[i].charAt(j)==':')
							 indexOfColon = j;
					 }
					 smallestWord[i] = thisFileString[i].substring(0, indexOfColon);
//					 smallestWordString.setLength(0);
					 smallestWordString = (thisFileString[i].substring(indexOfColon+1, thisFileString[i].length()));
					 
					 if( tmForMerge.containsKey(smallestWord[i])){
//						 fullString.setLength(0);
						 fullString = tmForMerge.get(smallestWord[i]);
						 fullString += smallestWordString;
						 
						 tmForMerge.remove(smallestWord[i]);
						 tmForMerge.put(smallestWord[i], fullString);
					 }
					 else{
						 flag=1;
						 tmForMerge.put(smallestWord[i], smallestWordString);
					 }
				 
			}//end of while(flag=0)
		     
		 }
		 
		 filePath = Paths.get(tempFileName.substring(0,indexLastSlash+1)+"/MergedIndex");		//to get the folder/directory name + / MergedIndex for our main index file

		 if (!Files.exists(filePath)) 
		    Files.createFile(filePath);
		 
		 Set<String> allkeys = tmForMerge.keySet();
		 while(tmForMerge.keySet().size()>0){
String key = tmForMerge.firstKey();

				 writeThisToFile = "";
				 writeThisToFile = key+":";
				 writeThisToFile += tmForMerge.get(key);

				 Files.write(filePath, ((writeThisToFile)+"\n").getBytes(), StandardOpenOption.APPEND);
				 tmForMerge.remove(key);										//after taking smallest, remove that

				 for(i=0 ;i<countfiles; i++){								//to search which position has the key
					 if(smallestWord[i] == key){
						 while(true){
						 thisFileString[i] =(bufReaderFiles[i].readLine());
						 if(thisFileString[i]==null){
							 count--;
							 smallestWord[i] = "";
							 break;
						 }
					 
						 
						 for(int j=0; j<thisFileString[i].length(); j++){
							 if(thisFileString[i].charAt(j)==':'){
								 indexOfColon=j;
								 break;
							 }
						 }
						 smallestWord[i] = thisFileString[i].substring(0,indexOfColon);
						 smallestWordString= thisFileString[i].substring(indexOfColon+1,thisFileString[i].length());
//					 } 
						 //add to treemap
						 if( tmForMerge.containsKey(smallestWord[i]) ){
//							 fullString.setLength(0);
							 fullString= tmForMerge.get(smallestWord[i]).toString();
							 fullString += smallestWordString;
							 
							 tmForMerge.remove(smallestWord[i]);
							 
							 tmForMerge.put(smallestWord[i], fullString);
						 }
						 
						 else{
							 tmForMerge.put(smallestWord[i], smallestWordString);
							 break;
						 }
						 }//end of while flag==0
					 }
					
				 }
//				 allkeys = tmForMerge.keySet();
//				 if(tmForMerge.keySet().size()!=countfiles)
//				 	break;									//so to take only first key
//			 }//end of while key
//			 System.out.println("count:"+count);
		 }// end of while count>0
		 allkeys = tmForMerge.keySet();
		 for(String key: allkeys){																	//after finishing files, traverse TreeMap
//			 writeThisToFile.setLength(0);
			 writeThisToFile = key;
			 writeThisToFile += tmForMerge.get(key);
			 Files.write(filePath, ((writeThisToFile).toString()+"\n").getBytes(), StandardOpenOption.APPEND);
			 tmForMerge.remove(key);										//after taking smallest, remove that 
		 }
	} 
