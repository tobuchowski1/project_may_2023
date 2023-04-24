package ski.obuchow.konkurs;


import java.io.IOException;

import org.rapidoid.buffer.Buf;
import org.rapidoid.http.AbstractHttpServer;
import org.rapidoid.http.HttpStatus;
import org.rapidoid.http.MediaType;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.impl.RapidoidHelper;

import ski.obuchow.konkurs.transactions.TransactionsApp;


public class Server extends AbstractHttpServer
{
 private static final byte HOME[] = "/".getBytes();
 private static final byte TRANSACTIONS[] = "/transactions/report".getBytes();
 private static final byte GAME[] = "/".getBytes();
 private static final byte ATM[] = "/".getBytes();

 @Override
 protected HttpStatus handle(Channel ctx, Buf buf, RapidoidHelper req) {
	 System.out.println(req.body.toString());
	 System.out.println(req.body.start);
	 System.out.println(req.body.last());
	 System.out.println(buf.get(req.path));
	 
	 if (!req.isGet.value) {
		 try {
			 String endpoint = buf.get(req.path).replace("//", "/");
			 buf.deleteBefore(req.body.start);
			 switch (endpoint) {
			 	case "/transactions/report":
				
					return ok(ctx, req.isKeepAlive.value, TransactionsApp.solve(buf), MediaType.JSON);
			 	default:
			 		return HttpStatus.NOT_FOUND;
			 }
		 } catch (IOException e) {
			return HttpStatus.ERROR;
		}
	 }
	
	 
	 //return ok(ctx, req.isKeepAlive.value, buf.asText().getBytes(), MediaType.JSON);
  
	 return HttpStatus.NOT_FOUND;
 }

}