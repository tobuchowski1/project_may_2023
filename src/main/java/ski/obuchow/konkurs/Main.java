package ski.obuchow.konkurs;


public class Main
{
	private static final int port = 8080;
	public static void main(String[] args) {
		new Server().listen(port);
	}
}
