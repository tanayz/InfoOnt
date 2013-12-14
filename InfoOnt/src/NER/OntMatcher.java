package NER;

import java.io.BufferedReader;

import com.cs586.dbpedia.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.tartarus.martin.Stemmer;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;

public class OntMatcher {

	public static void connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> fin = new ArrayList<String>();
		//fin.add("");
		ArrayList<String> sent = new ArrayList<String>();
		ArrayList<String> sub = new ArrayList<String>();
		ArrayList<String> sner = new ArrayList<String>();
		ArrayList<String> pro = new ArrayList<String>();
		ArrayList<String> obj = new ArrayList<String>();
		ArrayList<String> oner = new ArrayList<String>();
		String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		try {
			BufferedReader in = new BufferedReader(new FileReader(args[0]));

			String str0;
			// str0 = in.readLine();
			while ((str0 = in.readLine()) != null) {
				// System.out.println("From File :"+str0);
				sent.add(str0);
			}
			in.close();

		} catch (IOException e) {
			System.out.println("File Read Error");
		}
		// connecttomysql("'Bank_of_America'","'foundedBy'");
		String[] ar = sent.get(0).split(";");
		String s, sn, o, on;
		for (int l = 0; l < sent.size(); l++) {
			ar = sent.get(l).split(";"); 								//Splitting the test sentence in subject,predicate and object
			sub.add(ar[0].substring(ar[0].indexOf("(") + 1, ar[0].length()).replace('_', ' ')); 		//replacing _ from subject
			s = classifier.classifyWithInlineXML(sub.get(l));
			sn = s.substring(s.indexOf("<") + 1, s.indexOf("<") + 4);
			if (!(sn.matches("PER") || sn.matches("LOC") || sn.matches("ORG")))// Determining the entity type of test subject
				sn = "MISC";
			sner.add(sn);
			pro.add(ar[1]);
			obj.add(ar[2].substring(1, ar[2].length() - 1));
			o = classifier.classifyWithInlineXML(obj.get(l));
			on = o.substring(o.indexOf("<") + 1, o.indexOf("<") + 4);
			if (!(on.matches("PER") || on.matches("LOC") || on.matches("ORG")))// Determining the entity type of test object
				on = "MISC";
			oner.add(on);
		}

		// ///////////////////////////////////////////////Connect to mysql function////////////////////////////////////////////////////////

		connection();
		String host = "jdbc:mysql://localhost/mysql";
		String username = "root";
		String password = "root";

		ArrayList<Integer> ID = new ArrayList<Integer>();			//ID from training table
		ArrayList<String> subject = new ArrayList<String>();		//subject from training table
		ArrayList<String> property = new ArrayList<String>();	//predicate from training table
		ArrayList<String> object = new ArrayList<String>();		//object from  training table
		ArrayList<String> sub_uri = new ArrayList<String>();		//subject uri from training table
		ArrayList<String> pro_uri = new ArrayList<String>();		//predicate uri  training table
		ArrayList<String> obj_uri = new ArrayList<String>();		//object uri  training table
		ArrayList<Float> confidence = new ArrayList<Float>();
		ArrayList<String> ner = new ArrayList<String>();

		ArrayList<String> syn = new ArrayList<String>();		//list of property synonyms
			

		String subs = "'Bank_of_America'";								//Subject to be tested
		String pros ="'locationCity','keyPerson','division','subsidiary','name','foundationPlace','foundedBy','location','locationCountry','regionServed','predecessor'";
																					//Properties to be tested
		try {
			Connection connect = DriverManager.getConnection(host, username,password);
			PreparedStatement statement = (PreparedStatement) connect	.prepareStatement("Select * from ds_rdf_train where subject ="
							+ subs + " and property in (" + pros+")");
			// "Select * from ds_rdf_train where subject = '"+subs+"' and "+
			// "object ='"+objs+"'");
			ResultSet Data = statement.executeQuery();
			int i = 0;
			while (Data.next()) {
				ID.add(i, (int) Data.getObject("ID"));
				subject.add(i, (String) Data.getObject("subject"));
				property.add(i, (String) Data.getObject("property"));
				object.add(i, (String) Data.getObject("object"));
				sub_uri.add(i, (String) Data.getObject("sub_uri"));
				pro_uri.add(i, (String) Data.getObject("pro_uri"));
				obj_uri.add(i, (String) Data.getObject("obj_uri"));
				confidence.add((Float) Data.getObject("confidence"));
				ner.add((String) Data.getObject("ner"));
				i++;
			}
			statement.close();
			connect.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		////////////////////////////////Searching for multiple properties//////////////////////////////////////////////////////////
		for(int pr=0;pr<property.size();pr++)
		{
		String[] r = property.get(pr).split("(?=\\p{Upper})");

		TestJAWS TS = new TestJAWS();
		syn = TS.main(r);
		DBPediaLookUp db=new DBPediaLookUp();

		// ////////////////////////////////////////////Object ontology matching//////////////////////////////////////////////////
		//System.out	.println("\n////////////////////////////////////////////////////////////////////////////////////////////////");
		String rsub = subject.get(pr).replace('_', ' ');
		/*System.out.println("\nTraining  Subject : " + rsub);
		for (int k = 0; k < r.length; k++)
			System.out.println("Training Property : " + r[k].toString());
		System.out.print("Training Property Synonym : ");
		for (int y = 0; y < syn.size(); y++)
			System.out.print(syn.get(y) + ",");
		System.out.print("\nTesting Subject : ");
		for (int y = 0; y < sub.size(); y++)
			System.out.print(sub.get(y) + ",");
		System.out.print("\nTesting Subject NER : ");
		for (int y = 0; y < sner.size(); y++)
			System.out.print(sner.get(y) + ",");
		System.out.print("\nTesting Property : ");
		for (int y = 0; y < pro.size(); y++)
			System.out.print(pro.get(y) + ",");
		System.out.print("\nTesting Object: ");
		for (int y = 0; y < obj.size(); y++)
			System.out.print(obj.get(y) + ",");
		System.out.print("\nTesting Object NER: ");
		for (int y = 0; y < oner.size(); y++)
			System.out.print(oner.get(y) + ",");
*/
		System.out	.println("\n////////////////////////////////////////////////////////////////////////////////////////////////");
		// ////////////////////////////////////Get the verb out of it/////////////////////////////////////////////////////////////
		Findverb fv = new Findverb();
				// ///////////////////////////////////////////////Computation////////////////////////////////////////////////////////
		StemmerC sd = new StemmerC();
		Stemmer st = new Stemmer();
		int c=0,k=0,a=0,b=0,flag=0;
		//for (int c = 0; c < sub.size(); c++) 
		do {
			if (obj.get(c).contains(rsub) && (sner.get(c).matches(ner.get(pr)))&&k==0) {
					List<List<HasWord>> sentences = convertStringToListOfSentences(pro.get(c));
					List<List<HasWord>> syno1 = convertStringToListOfSentences(pro.get(a));
					
					String vb = Findverb.exvb(sentences);
					
					vb = StemmerC.stemWord(st, vb);
									
					for(int f=0;f<syn.size();f++)
					{
					if (syn.get(f).contains(vb)&&k==0) 
					  {
						k++;
						//System.out.println(rsub + "->" + property.get(pr) + " :"+ sub.get(c)+db.getDBPediaUrl(sub.get(c),ner.get(pr)));	
						System.out.println( sub_uri.get(pr)+ " "+pro_uri.get(pr) +  " <"+db.getDBPediaUrl(sub.get(c),ner.get(pr))+">");
						for(int z=0;z<fin.size();z++)
						{
							if(fin.get(z).contains(sub_uri.get(pr)+ " "+pro_uri.get(pr) +  " <"+db.getDBPediaUrl(sub.get(c),ner.get(pr))+">"))
							flag++;		
						}
						if(flag==0)
						fin.add(sub_uri.get(pr)+ " "+pro_uri.get(pr) +  " <"+db.getDBPediaUrl(sub.get(c),ner.get(pr))+">");
						flag=0;
					  }
					//System.out.println("syn:"+syn.get(f));
					}
				} 
			else {if (sub.get(c).contains(rsub) && (oner.get(c).matches(ner.get(pr)))&&k==0) {
					List<List<HasWord>> sentences = convertStringToListOfSentences(pro.get(c));
					//List<List<HasWord>> syno1 = convertStringToListOfSentences(pro.get(b));
					String vb = fv.exvb(sentences);
					vb = sd.stemWord(st, vb);
					for(int f=0;f<syn.size();f++)
					{
					if (syn.get(f).contains(vb)&&k==0) 
					  {
						k++;
						//System.out.println(rsub + "->" + property.get(pr) + " :"+ obj.get(c)+db.getDBPediaUrl(obj.get(c),ner.get(pr)));
						//System.out.println( sub_uri.get(pr)+ " "+pro_uri.get(pr)+ " <"+db.getDBPediaUrl(obj.get(c),ner.get(pr))+">");
						for(int z=0;z<fin.size();z++)
						{
							if(fin.get(z).contains(sub_uri.get(pr)+ " "+pro_uri.get(pr)+ " <"+db.getDBPediaUrl(obj.get(c),ner.get(pr))+">"))
								flag++;	
								}
						if(flag==0)
						fin.add(sub_uri.get(pr)+ " "+pro_uri.get(pr)+ " <"+db.getDBPediaUrl(obj.get(c),ner.get(pr))+">");
						flag=0;
						 }
					
					//System.out.println("syn:"+syn.get(f));
					}
			    }				
			}
		c++;
		}
		while (c<obj.size()&&k==0);
		}
	//System.out.println("/////////////////////////////////At the safe coding zone////////////////////////////////////");
		BufferedWriter writer = new BufferedWriter(new FileWriter("Data/test.n3"));
		System.out.println("*******************************************************Data in n3 file***********************************************");
	for(int z=0;z<fin.size();z++)
	{
		System.out.println(fin.get(z).toString());
	writer.write(fin.get(z).toString()+"\n");
	}
	writer.close();
	}

	private static List<List<HasWord>> convertStringToListOfSentences(String str) {

		List<List<HasWord>> sentences = new ArrayList<List<HasWord>>();
		List<HasWord> sentence = null;
		String[] stArr = { str };
		for (String st : stArr) {
			sentence = new ArrayList<HasWord>();
			String[] sArr = st.split(" ");
			for (String word : sArr) {
				Word w = new Word(word);
				sentence.add(w);
			}
			sentences.add(sentence);
		}
		return sentences;
	}

}
