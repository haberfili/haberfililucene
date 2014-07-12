package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

@Entity("news")
public class News  {
	 @Id public ObjectId id;
	 public String title,detail,link,image,source,detailMore,category;
	 @Reference(lazy = true)  
	 public List<News> similarNews = new ArrayList<News>();
	 public long createDate;
 

}