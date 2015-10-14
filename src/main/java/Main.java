import com.maxleap.code.*;
import com.maxleap.code.impl.*;
import handler.AssistHandler;
import handler.NinjaHandler;
import handler.ThemisHandler;
import handler._UserHandler;

/**
 * User：poplar
 * Date：14-11-5
 */
public class Main extends LoaderBase implements Loader {

	Logger logger = LoggerFactory.getLogger(Main.class);

	@Override
	public void main(GlobalConfig globalConfig) {

    //定义一个简单的function:返回输入数据
    defineFunction("hello", new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        logger.info(LASJsonParser.asJson(request.getUserPrincipal()));
        Response<String> response = new LASResponse<>(String.class);
        response.setResult(request.parameter(String.class));
        return response;
      }
    });

    //定义一个简单的job
    defineJob("helloJob", new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        Response response = new LASResponse(String.class);
        response.setResult("hello job");
        return response;
      }
    });

    defineFunction("helloNinja", new NinjaHandler().helloNinja());

    _UserHandler userHandler = new _UserHandler();
    defineFunction("helloUser", userHandler.helloUser());
    defineFunction("helloUserDelete", userHandler.helloUserDelete());

    AssistHandler assistHandler = new AssistHandler();
    defineFunction("helloAssist", assistHandler.helloAssist());
		defineFunction("receiptTransaction", assistHandler.receiptTransaction());
    defineFunction("walletTransaction", assistHandler.walletTransaction());

    ThemisHandler themisHandler = new ThemisHandler();
    defineFunction("helloPushMsg",themisHandler.helloPushMsg());
    defineFunction("helloCounter",themisHandler.helloCounter());
	}

}