package handler;

import com.maxleap.code.*;
import com.maxleap.code.assist.classes.PushMsgBuilder;
import com.maxleap.code.impl.MLResponse;
import com.maxleap.code.impl.PushMsg;
import com.maxleap.code.impl.ThemisImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User：poplar
 * Date：15/8/26
 */
public class ThemisHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(NinjaHandler.class);

  /**
   * 测试消息推送API
   * @return
   */
  public MLHandler<Request, Response> helloPushMsg() {
    return new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.withInstallationId("aaa")
            .withMsg("hello")
            .push();
        MLResponse response = new MLResponse(String.class);
        response.setResult("OK");
        return response;
      }
    };
  }

  /**
   * 测试分布式计数器API
   * @return
   */
  public MLHandler<Request, Response> helloCounter() {
    return new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        ThemisImpl themis = new ThemisImpl();
        String counterEntity = "myCount";
        themis.generateCounter(counterEntity);
        Long count = themis.get(counterEntity);
        System.out.println("get="+count);
        Long count1 = themis.incrementAndGet(counterEntity);
        System.out.println("incrementAndGet="+count1);
        Long count2 = themis.get(counterEntity);
        System.out.println("get="+count2);
        Long count3 = themis.getAndIncrement(counterEntity);
        System.out.println("getAndIncrement=" + count3);
        Long count4 = themis.get(counterEntity);
        System.out.println("get=" + count4);
        Long count5 = themis.decrementAndGet(counterEntity);
        System.out.println("decrementAndGet="+count5);
        Long count6 = themis.get(counterEntity);
        System.out.println("get=" + count6);
        Long count7 = themis.addAndGet(counterEntity, 1);
        System.out.println("addAndGet="+count7);
        Long count8 = themis.get(counterEntity);
        System.out.println("get=" + count8);
        Long count9 = themis.getAndAdd(counterEntity, 1);
        System.out.println("getAndAdd="+count9);
        Long count10 = themis.get(counterEntity);
        System.out.println("get=" + count10);
        MLResponse response = new MLResponse(String.class);
        response.setResult("OK");
        return response;
      }
    };
  }

  /**
   * 测试分布式锁API
   * @return
   */
  public MLHandler<Request, Response> helloLock() {
    return new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        ThemisImpl themis = new ThemisImpl();


        ExecutorService exec = Executors.newFixedThreadPool(30);
        for (int i = 0; i <= 99; i++)  {
          Runnable runnable = new Runnable(){
            @Override
            public void run() {
              System.out.println("##");
            }
          };
          exec.execute(runnable);
        }


        themis.getLock("myLock");
        MLResponse response = new MLResponse(String.class);
        response.setResult("OK");
        return response;
      }
    };
  }
}
