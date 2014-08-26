package container;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.News;
import mongo.DBConnectorLucene;

import com.google.code.morphia.Datastore;

public class NewsContainer {

		public static final int TWO_DAYS=24*60*60*1000*2;
		private static List<News> newsList;
		public static boolean running=false;
		public static Queue<String> queue=new LinkedList<String>();
		
		public static void scale(){
			System.out.println("news count before remove:"+newsList.size());
			List<News>newsToRemove=new ArrayList<News>();
			for(News news:newsList){
				if(news.createDate<new Date().getTime()-(NewsContainer.TWO_DAYS)){
					newsToRemove.add(news);
				}
				
			}
			for(News news :newsToRemove){
				newsList.remove(news);
			}
			System.out.println("news count after:"+newsList.size());
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
