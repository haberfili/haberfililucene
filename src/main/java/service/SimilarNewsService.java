package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.News;
import mongo.DBConnector;
import mongo.DBConnectorLucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.bson.types.ObjectId;
import org.tartarus.snowball.ext.TurkishStemmer;

import com.google.code.morphia.Datastore;

import container.NewsContainer;

@Path("/SimilarNews")
public class SimilarNewsService {

	private Directory indexDir;
	private StandardAnalyzer analyzer;
	private IndexWriterConfig config;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findSimilarNews/{id}")
	public String findSimilarNews(@PathParam("id") String id)throws Exception {
		addToLuceneDB(id);
		Datastore datasource = DBConnectorLucene.getDatasource();
		News news =datasource.get(News.class, new ObjectId(id));
		List<News> newsList = NewsContainer.getNews();
		start();
		writerEntries(newsList);
		
		String queryString=stem(news.title);
		if(news.detail!=null){
			queryString+=" "+stem(news.detail);
		}
		if(news.detailMore!=null){
			queryString+=" "+stem(news.detailMore);
		}
		queryString=queryString.replace("\"", "");
		queryString=queryString.replace(".", "");
		queryString=queryString.replace("’", "");
		queryString=queryString.replace("'", "");
		queryString=queryString.replace(" ve ", "");
		queryString=queryString.replace(",", "");
		queryString=queryString.replace(")", "");
		queryString=queryString.replace("(", "");
		queryString=queryString.replace("?", "");
		queryString=queryString.replace("-", "");
		queryString=queryString.replace("\n", "");
		queryString=queryString.replace("!", "");
		queryString=queryString.replace(":", "");
		queryString=queryString.toLowerCase();
		findSimilar(queryString,id);
		NewsContainer.add(news);
		NewsContainer.scale();
		return "OK";
	}

	private void addToLuceneDB(String id) throws Exception {
		Datastore datasource = DBConnector.getDatasource();
		News news =datasource.get(News.class, new ObjectId(id));
		Datastore datasourceLucene = DBConnectorLucene.getDatasource();
		if(datasourceLucene.get(News.class, new ObjectId(id))==null){
			datasourceLucene.save(news);	
		}
	}

	public void start() throws IOException{
		analyzer = new StandardAnalyzer(Version.LUCENE_42);
		config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);

		indexDir = new RAMDirectory(); //don't write on disk
		//indexDir = FSDirectory.open(new File("/Path/to/luceneIndex/")); //write on disk
	}

	public void writerEntries(List<News> newsList) throws IOException{
		IndexWriter indexWriter = new IndexWriter(indexDir, config);
		indexWriter.commit();

		for(News news: newsList){
			Document doc1 = createDocument(news.id.toString(),stem(news.title)+" "+stem(news.detail)+" "+stem(news.detailMore));
			indexWriter.addDocument(doc1);			
		}
		indexWriter.commit();
		indexWriter.forceMerge(100, true);
		indexWriter.close();
	}
	private String stem(String title) {
		if(title==null){
			return null;
		}
		TurkishStemmer stemmer=new TurkishStemmer();
		String[] split = title.split(" ");
		String returnString="";
		for(String term : split){
			stemmer.setCurrent(term);
			stemmer.stem();
			if(stemmer.getCurrent()!=null && stemmer.getCurrent().length()>2){
				returnString+=stemmer.getCurrent()+" ";
			}
		}
		return returnString;
	}
	private Document createDocument(String id, String content) {
		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true); //TermVectors are needed for MoreLikeThis

		Document doc = new Document();
		doc.add(new StringField("id", id, Store.YES));
		doc.add(new Field("content", content, type));
		return doc;
	}
	public void findSimilar(String searchForSimilar, String id) throws Exception {
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
//		MoreLikeThis mlt = new MoreLikeThis(reader);
//	    mlt.setMinTermFreq(0);
//	    mlt.setMinDocFreq(0);
//	    mlt.setFieldNames(new String[]{"title", "content"});
//	    mlt.setAnalyzer(analyzer);
//	    Reader sReader = new StringReader(searchForSimilar);
//	    Query query = mlt.like(sReader, null);
		
//		FuzzyQuery query = new FuzzyQuery(new Term("title",searchForSimilar), 2);
		
//		SpanQuery[] clauses = new SpanQuery[3];
//		clauses[0] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term("content", "Şans")));
//		clauses[1] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term("content", "ordusundan")));
//		clauses[2] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term("content", "çekilişi")));
//		SpanNearQuery query = new SpanNearQuery(clauses, 1000, true);
		String querystr = "";
		String[] split = searchForSimilar.split(" ");
		for(int i=0; i<split.length;i++){
			String key= split[i];
			if(key.indexOf("'")!=-1){
				key=key.substring(0,key.indexOf("'"));
			}
			if(!key.trim().equals("")){
				querystr+="content:"+key+"* ";
				if(i!=split.length-1){
					querystr+="OR ";
				}
			}
		}
		System.out.println(querystr);
		Query query = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer).parse(querystr.trim());

	    TopDocs topDocs = indexSearcher.search(query,10);
	    
	    List<News>similarNews=new ArrayList<News>();
	    Datastore datasourceLucene = DBConnectorLucene.getDatasource();
	    for ( ScoreDoc scoreDoc : topDocs.scoreDocs ) {
	        if(similarNews.size()>=5){
	        	break;
	        }
	    	Document aSimilar = indexSearcher.doc( scoreDoc.doc );
	        String similarTitle = aSimilar.get("title");
	        String similarContent = aSimilar.get("content");
	        
	        if(!aSimilar.get("id").toString().equals(id)){
	        	similarNews.add(datasourceLucene.get(News.class, new ObjectId(aSimilar.get("id"))));
	        }
	        System.out.println("====similar finded====");
	        System.out.println("title: "+ similarTitle);
	        System.out.println("content: "+ similarContent);
	    }
	    Datastore datasource = DBConnector.getDatasource();
	    News news =datasource.get(News.class, new ObjectId(id));
	    news.similarNews=similarNews;
	    datasource.save(news);

	}
}

