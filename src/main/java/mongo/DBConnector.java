package mongo;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class DBConnector {
	
	static String host="ds061298.mongolab.com";
	static Integer port = 61298;
	static String database="heroku_app20395923";
	static String username="mokarakaya";
	static String password="passw0rd33";
	
	private static Datastore ds;
	
	public static Datastore getDatasource() throws Exception {
		if (ds == null) {
			Mongo mongo;
			Morphia morphia;
			mongo = new MongoClient(new ServerAddress(host, port));
			morphia = new Morphia();
			ds = morphia.createDatastore(mongo, database);
			if (username != null) {
				ds.getDB().authenticate(username, password.toCharArray());
			}
		}
		return ds;
	}
}

