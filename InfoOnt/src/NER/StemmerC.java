package NER;

 
import java.io.File;
import java.io.IOException;
import java.util.List;
 
import org.tartarus.martin.*;
import org.apache.commons.io.FileUtils;
 
public class StemmerC {
 
        public static void main(String[] args) throws IOException {
                Stemmer st = new Stemmer();
               
                List<String> lines = FileUtils.readLines(new File("inputFile.txt"));
                for(String line: lines){
                        String [] words = line.split(" ");
                        for(String w: words){
                                String stemmed = stemWord(st,w);
                        }
                }
        }
 
        public static String stemWord(Stemmer st, String w) {
                for (int c = 0; c < w.length(); c++) {
                        if(!Character.isLetter(w.charAt(c))) break;
                        st.add(w.charAt(c));
                }
                st.stem();
                String retVal = st.toString();
                //System.out.println("stemWord("+w+") => "+retVal);
                return retVal;
        }
}

