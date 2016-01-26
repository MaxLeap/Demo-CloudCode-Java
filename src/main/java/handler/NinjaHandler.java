package handler;

import com.maxleap.code.*;
import com.maxleap.code.impl.MLJsonParser;
import com.maxleap.code.impl.MLResponse;
import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.MLQuery;
import com.maxleap.las.sdk.MLUpdate;
import com.maxleap.las.sdk.UpdateMsg;
import bean.Ninja;

import java.util.ArrayList;
import java.util.List;

/**
 * User：poplar
 * Date：15-5-14
 */
public class NinjaHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(NinjaHandler.class);

  /**
   * 测试自建表-忍者 的CRUD
   *
   * @return
   */
  public MLHandler<Request, Response> helloNinja() {
    final MLClassManager<Ninja> ninjaZEntityManager = MLClassManagerFactory.getManager(Ninja.class);
    return new MLHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
        UserPrincipal userPrincipal = request.getUserPrincipal();
        LOGGER.info(request.parameter(String.class));
        Ninja ninja = request.parameter(Ninja.class);
        LOGGER.info(ninja.toString());
        String name = ninja.getName();
        //产生本体
        SaveResult<Ninja> saveMsg = ninjaZEntityManager.create(ninja, userPrincipal);
        String ninjaObjectId = saveMsg.getSaveMessage().objectId().toString();
        LOGGER.info("生成本体，ID为：" + ninjaObjectId);
        //产生50个分身
        List<String> cloneObjectIds = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
          ninja.setName(name + "_" + i);
          SaveResult<Ninja> cloneSave = ninjaZEntityManager.create(ninja, userPrincipal);
          String cloneObjectId = cloneSave.getSaveMessage().objectId().toString();
          cloneObjectIds.add(cloneObjectId);
          LOGGER.info("多重影分身：" + cloneSave.getSaveMessage().objectId().toString());
        }
        //找出第50个分身
        MLQuery sunQuery = MLQuery.instance();
        sunQuery.equalTo("name", name + "_" + 50);
        FindMsg<Ninja> findMsg = ninjaZEntityManager.find(sunQuery, userPrincipal);
        Ninja ninja_50 = findMsg.results().get(0);
        cloneObjectIds.remove(ninja_50.objectIdString());
        LOGGER.info("找到第50个分身：" + MLJsonParser.asJson(ninja_50));
        //击杀其余49个分身
        DeleteResult deleteResult = ninjaZEntityManager.delete(cloneObjectIds.toArray(new String[cloneObjectIds.size()]), userPrincipal);
        LOGGER.info("完成分身击杀数目：" + deleteResult.getDeleteMessage().number());
        //击杀本体
        ninjaZEntityManager.delete(ninjaObjectId, userPrincipal);
        LOGGER.info("完成本体击杀");
        //让第50分身成为新的本体
        MLUpdate update = MLUpdate.getUpdate();
        update.set("name", name + "_new");
        UpdateMsg updateMsg = ninjaZEntityManager.update(ninja_50.objectIdString(), update, userPrincipal);
        LOGGER.info("第50个分身在" + updateMsg.updatedAtString() + "成为新的本体");
        //返回新的本体名称
        Ninja ninja_new = ninjaZEntityManager.findById(ninja_50.objectIdString(), userPrincipal);
        Response<String> response = new MLResponse<>(String.class);
        response.setResult(ninja_new.getName());

        ninjaZEntityManager.delete(ninja_50.objectIdString(), userPrincipal);
        return response;
      }
    };
  }

}
