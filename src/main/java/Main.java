import as.leap.code.*;
import as.leap.code.impl.*;
import handler.AssistHandler;
import handler.NinjaHandler;
import handler._UserHandler;

import java.util.Map;

/**
 * User：poplar
 * Date：14-11-5
 */
public class Main extends LoaderBase implements Loader {

	Logger logger = LoggerFactory.getLogger(Main.class);

	@Override
	public void main(GlobalConfig globalConfig) {

    defineFunction("hello", new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        Response<Map> response = new LASResponse<>(Map.class);
        Map map = request.parameter(Map.class);
        System.out.println(map);
        response.setResult(map);
        return response;
      }
    });

    _UserHandler userHandler = new _UserHandler();
		defineFunction("helloUser", userHandler.helloUser());

		defineFunction("helloUserDelete", userHandler.helloUserDelete());

    defineFunction("helloNinja", new NinjaHandler().helloNinja());


    AssistHandler assistHandler = new AssistHandler();
    defineFunction("helloAssist", assistHandler.helloAssist());

    defineJob("helloJob", new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        Response response = new LASResponse(String.class);
        response.setResult("hello job");
        return response;
      }
    });

		defineFunction("receiptTransaction", assistHandler.receiptTransaction());

    defineFunction("walletTransaction", assistHandler.walletTransaction());
	}

}