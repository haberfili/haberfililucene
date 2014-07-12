package lucene.lucene;

import org.apache.lucene.analysis.tr.TurkishAnalyzer;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.TurkishStemmer;

import service.SimilarNewsService;

public class App1 {

	public static void main(String[] args) throws Exception {
		
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("53c1924ce4b04a53e0fd28b7");
		
//		TurkishStemmer stemmer= new TurkishStemmer();
//		stemmer.setCurrent("kazanma");
//		stemmer.stem();
//		System.out.println(stemmer.getCurrent());
//		stemmer.setCurrent("'kazanır'");
//		stemmer.stem();
//		System.out.println(stemmer.getCurrent());

	}

}
