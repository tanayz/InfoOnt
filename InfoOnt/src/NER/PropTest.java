package NER;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
//import jubilee.propbank.PBReader;

public class PropTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("wordnet.database.dir", "/home/tanay/Copy/Data/EntityLkp/WordNet-3.0/dict/");
		//PBReader PB = new PBReader("Read","Apply");
		 
		NounSynset nounSynset;
		NounSynset[] hyponyms;

		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets("fly", SynsetType.NOUN);
		for (int i = 0; i < synsets.length; i++) {
		    nounSynset = (NounSynset)(synsets[i]);
		    hyponyms = nounSynset.getHyponyms();
		    System.err.println(nounSynset.getWordForms()[0] +
		            ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
		}

	}
	/*
	 * public static void connecttomysql(String subs,String pros) {
	 * connection(); String host = "jdbc:mysql://localhost/mysql"; String
	 * username = "root"; String password = "root";
	 * 
	 * ArrayList<Integer> ID = new ArrayList<Integer>(); ArrayList<String>
	 * subject = new ArrayList<String>(); ArrayList<String> property= new
	 * ArrayList<String>(); ArrayList<String> object= new ArrayList<String>();
	 * ArrayList<String> sub_uri= new ArrayList<String>(); ArrayList<String>
	 * pro_uri= new ArrayList<String>(); ArrayList<String> obj_uri= new
	 * ArrayList<String>(); ArrayList<Float> confidence=new ArrayList<Float>();
	 * ArrayList<String> ner= new ArrayList<String>();
	 * 
	 * ArrayList<String> syn = new ArrayList<String>();
	 * 
	 * 
	 * 
	 * try { Connection connect =
	 * DriverManager.getConnection(host,username,password); PreparedStatement
	 * statement = (PreparedStatement)
	 * connect.prepareStatement("Select * from ds_rdf_train where subject ="
	 * +subs +" and property= "+pros+" and object is null");
	 * //"Select * from ds_rdf_train where subject = '"+subs+"' and "+
	 * "object ='"+objs+"'"); ResultSet Data=statement.executeQuery(); int i=0;
	 * while(Data.next()) {
	 * 
	 * System.out.println("ID :"+Data.getObject("ID")+"-> "+Data.getObject("subject"
	 * ) +"-> "+Data.getObject("property")+"-> "+Data.getObject("object")
	 * +"-> "+Data.getObject("sub_uri")+"-> "+Data.getObject("pro_uri")
	 * +"-> "+Data.getObject("obj_uri")+"-> "+Data.getObject("confidence"));
	 * 
	 * ID.add(i,(int) Data.getObject("ID")); subject.add(i,(String)
	 * Data.getObject("subject")); property.add(i,(String)
	 * Data.getObject("property")); object.add(i,(String)
	 * Data.getObject("object")); sub_uri.add(i,(String)
	 * Data.getObject("sub_uri")); pro_uri.add(i,(String)
	 * Data.getObject("pro_uri")); obj_uri.add(i,(String)
	 * Data.getObject("obj_uri")); confidence.add((Float)
	 * Data.getObject("confidence")); ner.add((String) Data.getObject("ner"));
	 * i++; } statement.close(); connect.close();
	 * System.out.println("It Works :)"); for(int j=0;j<ID.size();j++) {
	 * System.out
	 * .println(ID.get(j)+"->"+subject.get(j)+"->"+property.get(j)+"->"
	 * +object.get(j)
	 * +"->"+sub_uri.get(j)+"->"+pro_uri.get(j)+"->"+obj_uri.get(j
	 * )+confidence.get(j)+"->"+ner.get(j)); } } catch (SQLException e) {
	 * e.printStackTrace(); } String[] r =
	 * property.get(0).split("(?=\\p{Upper})"); for(int k=0;k<r.length;k++)
	 * System.out.println("Test String : "+k+"-> "+r[k].toString()); TestJAWS TS
	 * = new TestJAWS(); syn=TS.main(r);
	 * 
	 * for (int s=0;s<syn.size();s++)
	 * System.out.println("Returned string :"+syn.get(s)); }
	 */
}
