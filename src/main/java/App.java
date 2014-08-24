import service.SimilarNewsService;


public class App {

	public static void main(String[] args) throws Exception {
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("53fa38d2e4b0dec253c89261");
//		System.out.println("asd");
//		service.findSimilarNews("53cc3b8ee4b0bec0004be866");
	}

}
