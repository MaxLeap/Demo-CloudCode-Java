package handler;

import as.leap.code.*;
import as.leap.code.impl.LASResponse;
import as.leap.code.impl.PushMsg;
import as.leap.code.impl.ThemisImpl;

/**
 * User：poplar
 * Date：15/8/26
 */
public class ThemisHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(NinjaHandler.class);

  public LASHandler<Request, Response> helloPushMsg() {
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.withInstallationId("aaa").withMsg("hello").push();
        LASResponse response = new LASResponse(String.class);
        response.setResult("OK");
        return response;
      }
    };
  }

  public LASHandler<Request, Response> helloCounter() {
    return new LASHandler<Request, Response>() {
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
        LASResponse response = new LASResponse(String.class);
        response.setResult("OK");
        return response;
      }
    };
  }
}
