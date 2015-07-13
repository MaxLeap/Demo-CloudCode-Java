package handler;

import as.leap.code.*;
import as.leap.code.impl.LASResponse;
import as.leap.las.sdk.FindMsg;
import as.leap.las.sdk.LASQuery;
import as.leap.las.sdk.LASUpdate;
import as.leap.las.sdk.UpdateMsg;
import bean._User;

/**
 * User：poplar
 * Date：15/7/10
 */
public class _UserHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(NinjaHandler.class);

  /**
   * 测试内建表的CRUD
   * @return
   */
  public LASHandler<Request, Response> helloUser(){
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        LASClassManager<_User> userZEntityManager = LASClassManagerFactory.getManager(_User.class);
        //create
        _User user = new _User();
        user.setUsername("jack2");
        user.setPassword("123456");
        SaveResult saveMsg = userZEntityManager.create(user);
        System.out.println(saveMsg.getSaveMessage());

        //update
        LASUpdate update = LASUpdate.getUpdate();
        update.set("password","123");
        UpdateMsg updateMsg = userZEntityManager.update(saveMsg.getSaveMessage().objectIdString(), update);
        System.out.println(updateMsg);

        //delete
        DeleteResult deleteResult = userZEntityManager.delete(saveMsg.getSaveMessage().objectIdString());
        System.out.println(deleteResult);

        //find
        LASQuery lasQuery = LASQuery.instance();
        lasQuery.equalTo("name", "aa");
        FindMsg<_User> findMsg = userZEntityManager.find(lasQuery);
        System.out.println(findMsg);

        LASResponse LASResponse = new LASResponse(FindMsg.class);
        LASResponse.setResult(findMsg);
        return LASResponse;
      }
    };
  }

  /**
   * 测试内建表的CRUD
   * @return
   */
  public LASHandler<Request, Response> helloUserDelete(){
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        LASClassManager<_User> userZEntityManager = LASClassManagerFactory.getManager(_User.class);

        //create
        _User user = new _User();
        user.setUsername("jack222");
        user.setPassword("123456");
        SaveResult saveMsg = userZEntityManager.create(user);
        System.out.println(saveMsg.getSaveMessage());

        //create
        _User user2 = new _User();
        user2.setUsername("jack333");
        user2.setPassword("123456");
        SaveResult saveMsg2 = userZEntityManager.create(user2);
        System.out.println(saveMsg2.getSaveMessage());

        //create
        _User user3 = new _User();
        user3.setUsername("jack444");
        user3.setPassword("123456");
        SaveResult saveMsg3 = userZEntityManager.create(user3);
        System.out.println(saveMsg3.getSaveMessage());

        //find
        LASQuery lasQuery = LASQuery.instance();
        lasQuery.notEqualTo("username", "");
        lasQuery.setLimit(10);
        lasQuery.sort(LASQuery.SORT_ASC,"updatedAt");
        FindMsg<_User> findMsg = userZEntityManager.find(lasQuery);
        System.out.println(findMsg);

        //deleteBatch
        DeleteResult deleteResult = userZEntityManager.delete(new String[]{saveMsg.getSaveMessage().objectIdString(), saveMsg2.getSaveMessage().objectIdString(), saveMsg3.getSaveMessage().objectIdString()});
        System.out.println(deleteResult);
        LASResponse LASResponse = new LASResponse(FindMsg.class);
        LASResponse.setResult(findMsg);
        return LASResponse;
      }
    };
  }
}