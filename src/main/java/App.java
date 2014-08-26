import service.SimilarNewsService;


public class App {

	public static void main(String[] args) throws Exception {
		SimilarNewsService service= new SimilarNewsService();
		service.findSimilarNews("53fccfffe4b02b14a04c50c7");
//		System.out.println("asd");
		service.findSimilarNews("53fccf07e4b02b14a04c50c4");
	}

}
