package container;


import java.util.Date;
import java.util.List;

import models.News;
import mongo.DBConnectorLucene;

import com.google.code.morphia.Datastore;

public class NewsContainer {

		public static final int TWO_DAYS=24*60*60*1000*2;
		private static List<News> newsList;
		public static boolean scaling=false;
		
		public static void scale(){
			scaling=true;
			for(News news:newsList){
				if(news.createDate<new Date().getTime()-(NewsContainer.TWO_DAYS)){
					newsList.remove(news);
				}
				
			}
			scaling=false;
		}
		
		public static List<News> getNews() throws Exception{
			if(newsList==null){
				Datastore datasource = DBConnectorLucene.getDatasource();
				newsList=datasource.find(News.class).field("createDate").lessThan(new Date().getTime()+(NewsContainer.TWO_DAYS))
							.field("createDate").greaterThan(new Date().getTime()-(NewsContainer.TWO_DAYS)).asList();
			}
			return newsList;
		}
		
		public static void add(News news){
			newsList.add(news);
		}

	
}
