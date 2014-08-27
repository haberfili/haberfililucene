package container;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import models.News;
import mongo.DBConnectorLucene;

import com.google.code.morphia.Datastore;

public class NewsContainer {

		public static final int TWO_DAYS=24*60*60*1000*2;
		public static boolean running=false;
		public static Queue<String> queue=new LinkedList<String>();
		
		
		
		public static List<News> getNews() throws Exception{
				Datastore datasource = DBConnectorLucene.getDatasource();
				return datasource.find(News.class).field("createDate").lessThan(new Date().getTime()+(NewsContainer.TWO_DAYS))
							.field("createDate").greaterThan(new Date().getTime()-(NewsContainer.TWO_DAYS)).asList();
		}
}
