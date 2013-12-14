package NER;

//package edu.cmu.lti.ws4j.demo;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;


public class SimilarityCalculationDemo {
       
        private static ILexicalDatabase db = new NictWordNet();
        private static RelatednessCalculator[] rcs = {new WuPalmer(db)
                       
                        };
        /////////////////////////////////////////////////////////////
        // new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db),
        //new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
        
        /////////////////////////////////////////////////////////////
       
        public static double run( String word1, String word2 ) {
                WS4JConfiguration.getInstance().setMFS(true);
                double d=0;
                for ( RelatednessCalculator rc : rcs ) {
                        d = rc.calcRelatednessOfWords(word1, word2);
                        System.out.println( rc.getClass().getName()+"\t"+d );
                        
                }
				
				return d;
                
                
        }
        public static void main(String[] args) {
                long t0 = System.currentTimeMillis();
                run( "Country","Chicago" );
                long t1 = System.currentTimeMillis();
                System.out.println( "Done in "+(t1-t0)+" msec." );
                
                
        }
}
