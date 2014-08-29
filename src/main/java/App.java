import service.SimilarNewsService;


public class App {

	public static void main(String[] args) throws Exception {
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("5400cfade4b0620db8621662");
//		System.out.println("asd");
		service.findSimilarNews("5400c895e4b0620db8621654");
	}

}
