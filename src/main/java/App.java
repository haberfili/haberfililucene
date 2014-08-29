import service.SimilarNewsService;


public class App {

	public static void main(String[] args) throws Exception {
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("5400e135e4b0620db8621679");
//		System.out.println("asd");
		service.findSimilarNews("5400e0d3e4b0620db8621676");
	}

}
