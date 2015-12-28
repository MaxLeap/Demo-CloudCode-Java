import bean.Ninja;
import com.maxleap.code.*;
import com.maxleap.code.impl.*;
import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.MLQuery;
import com.maxleap.las.sdk.MLUpdate;
import handler.AssistHandler;
import handler.NinjaHandler;
import handler.ThemisHandler;
import handler._UserHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User：poplar
 * Date：14-11-5
 */
public class Main extends LoaderBase implements Loader {

  Logger logger = LoggerFactory.getLogger(Main.class);

  private void getFile() {
    Properties properties = new Properties();
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("public/test.properties");
    if (inputStream != null) {
      try {
        properties.load(inputStream);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    logger.info("property:" + properties.getProperty("a"));
  }

  MLClassManager<Ninja> ninjaMLClassManager = MLClassManagerFactory.getManager(Ninja.class);

  /**
   * 分页实现
   *
   * @param query 基础查询语句，预先定义好的
   * @param skip  动态忽略行数
   * @return 最终结果集，包含全部分页结果
   */
  private List<Ninja> paging(MLQuery query, AtomicInteger skip) {
    query.setLimit(100);//设置返回最大记录条数为100条
    query.setSkip(skip.get());//动态设置忽略前面N条记录
    FindMsg<Ninja> findMsg = ninjaMLClassManager.find(query);//执行查询
    if (findMsg.results() == null || findMsg.results().size() == 0) return new ArrayList<>();//结果集为空，退出
    if (findMsg.results().size() < 100) return findMsg.results();//没有下一页退出
    //有下一页
    skip.addAndGet(100);//忽略前面已经查询过的条数
    findMsg.results().addAll(paging(query, skip));//递归查询
    return findMsg.results();
  }

  @Override
  public void main(GlobalConfig globalConfig) {

    //定义一个简单的function:返回输入数据
    defineFunction("hello", new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        MLUpdate update = MLUpdate.getUpdate();
        logger.info(request.parameter(String.class));
        Response<String> response = new MLResponse<>(String.class);
        response.setResult(request.parameter(String.class));
        return response;
      }
    });

    //定义一个简单的job
    defineJob("helloJob", new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        Response response = new MLResponse(String.class);
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
    defineFunction("helloPushMsg", themisHandler.helloPushMsg());
    defineFunction("helloCounter", themisHandler.helloCounter());
  }

}