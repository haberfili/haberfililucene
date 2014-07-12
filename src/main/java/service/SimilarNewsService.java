package service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lucene.lucene.App;
import models.News;
import mongo.DBConnector;

import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.bson.types.ObjectId;
import org.tartarus.snowball.ext.TurkishStemmer;

import com.google.code.morphia.Datastore;

@Path("/SimilarNews")
public class SimilarNewsService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findSimilarNews/{id}")
	public String findSimilarNews(@PathParam("id") String id)throws Exception {
		Datastore datasource = DBConnector.getDatasource();
		News news =datasource.get(News.class, new ObjectId(id));
		List<News> newsList = datasource.find(News.class).field("createDate").lessThan(news.createDate+(24*60*60*1000*2))
				.field("createDate").greaterThan(news.createDate-(24*60*60*1000*2)).asList();
		App m = new App();
		m.start();
		m.writerEntries(newsList);
		
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
		m.findSilimar(queryString,id);
		return null;
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
}

