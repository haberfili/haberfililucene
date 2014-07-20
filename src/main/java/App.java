import service.SimilarNewsService;


public class App {

	public static void main(String[] args) throws Exception {
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("53cc3e5ce4b0bec0004be868");
		System.out.println("asd");
		service.findSimilarNews("53cc3b8ee4b0bec0004be866");
	}

}
