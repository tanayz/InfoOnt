package NER;
import java.util.ArrayList;

import edu.smu.tspell.wordnet.*;


/**
 * Displays word forms and definitions for synsets containing the word form
 * specified on the command line. To use this application, specify the word
 * form that you wish to view synsets for, as in the following example which
 * displays all synsets containing the word form "airplane":
 * <br>
 * java TestJAWS airplane
 */
public class TestJAWS
{

	/**
	 * Main entry point. The command-line arguments are concatenated together
	 * (separated by spaces) and used as the word form to look up.
	 *  
	 */
	/*public ArrayList<String> synonm(ArrayList<String> syno1)
	{
		ArrayList<String> syno = new ArrayList<String>(); 
		
		return syno;
	
	}*/
	public static  ArrayList<String> main(String[] args)
	{
		ArrayList<String> syno = new ArrayList<String>(); 
		if (args.length > 0)
		{
			//  Concatenate the command-line arguments
			System.setProperty("wordnet.database.dir", "/home/tanay/Copy/Data/EntityLkp/WordNet-3.0/dict/");
			StringBuffer buffer = new StringBuffer();
			/*for (int i = 0; i < args.length; i++)
			{
				buffer.append((i > 0 ? " " : "") + args[i]);
			}*/
			buffer.append( args[0]);
			String wordForm = buffer.toString();
			//  Get the synsets containing the word form
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			Synset[] synsets = database.getSynsets(wordForm);
			
			//  Display the word forms and definitions for synsets retrieved
			if (synsets.length > 0)
			{
				/*System.out.println("The following synsets contain '" +
						wordForm + "' or a possible base form " +
						"of that text:");*/
				for (int i = 0; i < synsets.length; i++)
				{
					/*System.out.println("");*/
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
					{
						/*System.out.print((j > 0 ? ", " : "") +
								wordForms[j]);*/
						
						syno.add(wordForms[j]);
					}
	/*				System.out.println(": " + synsets[i].getDefinition());*/
				}
			}
			else
			{
				System.err.println("No synsets exist that contain " +
						"the word form '" + wordForm + "'");
			}
			/*System.out.println("*******************************All the sysnonym forms********************************");
			for(int k=0;k<syno.size();k++)
				System.out.print(syno.get(k)+",");*/
		}
		else
		{
			System.err.println("You must specify " +
					"a word form for which to retrieve synsets.");
		}
		return syno;		
	}

}