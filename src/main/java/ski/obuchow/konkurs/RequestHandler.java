package ski.obuchow.konkurs;


import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.logging.Logger;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import ski.obuchow.konkurs.atm.ATMApp;
import ski.obuchow.konkurs.onlinegame.GameApp;
import ski.obuchow.konkurs.transactions.TransactionsApp;

public class RequestHandler implements HttpHandler {
	private static final Logger LOGGER = Logger.getLogger(RequestHandler.class);
	
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
		if (!exchange.getRequestMethod().equals(Methods.POST)) {
		    return;
		}
		// picking a high max value because we expect big input json files
		// 1GB should be more than enough
		exchange.setMaxEntitySize(1000000000);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.startBlocking();
        
        // endpoints with double slashes were generated from provided OpenAPI description
        // making sure server responds to both
		String path = exchange.getRelativePath().replace("//", "/");
		
		InputStream inputStream = exchange.getInputStream();
		OutputStream outputStream = exchange.getOutputStream();
		
		LOGGER.debug(path);
		
		switch (path) {
			case "/transactions/report":
				TransactionsApp.solve(inputStream, outputStream);
				break;
			case "/onlinegame/calculate":
				GameApp.solve(inputStream, outputStream);
				break;
			case "/atms/calculateOrder":
				ATMApp.solve(inputStream, outputStream);
				break;
			default:
			    ResponseCodeHandler.HANDLE_404.handleRequest(exchange);
				return;
		}
		exchange.getResponseSender().close();
	}

}
