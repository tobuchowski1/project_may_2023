package ski.obuchow.konkurs;

import io.undertow.Undertow;

public class Main
{
	private static final int port = 8080;
	
	public static void main(final String[] args) {
		System.setProperty("org.jboss.logging.provider", "slf4j");
        Undertow server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setWorkerThreads(40)
                .setIoThreads(10)
                .setHandler(new RequestHandler())
                .build();
        server.start();
    }
}
