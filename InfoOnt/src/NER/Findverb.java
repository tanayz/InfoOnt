package NER;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Findverb {
	
	public static String exvb (List<List<HasWord>> sentences) throws FileNotFoundException
	{
		
		
		String vb=new String();
		List<String> results = new ArrayList<String>();
//		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader("/home/tanay/workspace/InfoOnt/Data/data.txt")));
		MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");//Loading the model for the Tagger
		
		
		for (List<HasWord> sentence : sentences)
        { // Loop to process each sentence
                ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
                //System.out.println("Position of I:"+Sentence.listToString(tSentence, true));
                vb=Sentence.listToString(tSentence, false);
        }
		//System.out.println("String :"+vb);
		
		Pattern pattern1 = Pattern.compile("([^\\s]+)(/JJ)"); 
 		Pattern pattern2 = Pattern.compile("([^\\s]+)(/VBN)"); 
 		Pattern pattern3 = Pattern.compile("([^\\s]+)(/VB)"); 
 		Matcher matcher1 = pattern1.matcher(vb);
 		Matcher matcher2 = pattern2.matcher(vb);
 		Matcher matcher3 = pattern3.matcher(vb);
 		String sc=null;
 		
 		while(matcher2.find()){
 			//System.out.println("Found: /VBN");
 			sc = matcher2.group(0);
 		}
 		
 		if(sc == null){
	 		while (matcher1.find())
	 	    {
	 			//System.out.println("Found: /JJ");
	 			sc= matcher1.group(0);
	 	    }		
	 		
	 		if(sc == null){
		 		while (matcher3.find())
		 	    {
		 			//System.out.println("Found: /VB");
		 			sc= matcher3.group(0);
		 	    }	
 		}
	 		       
 		
	}
 		//System.out.println("SC:"+sc);
        vb=sc.substring(0,sc.indexOf('/'));
 		return vb;
	
	}
}
