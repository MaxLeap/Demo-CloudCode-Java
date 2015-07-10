package hook;

import as.leap.code.*;
import as.leap.code.impl.LASClassManagerHookBase;
import as.leap.las.sdk.FindMsg;
import as.leap.las.sdk.LASQuery;
import as.leap.las.sdk.LASUpdate;
import as.leap.las.sdk.SaveMsg;
import bean.Ninja;

import java.util.HashMap;
import java.util.Map;


@ClassManager("Ninja")
public class NinjaHook extends LASClassManagerHookBase<Ninja> {

  @Override
  public BeforeResult<Ninja> beforeCreate(Ninja ninja) {
    LASClassManager<Ninja> ninjaZEntityManager = LASClassManagerFactory.getManager(Ninja.class);
    //创建忍者前验证是否重名了
    LASQuery sunQuery = LASQuery.instance();
    sunQuery.equalTo("name", ninja.getName());
    FindMsg<Ninja> findMsg = ninjaZEntityManager.find(sunQuery);
    if (findMsg.results() != null && findMsg.results().size() > 0) return new BeforeResult<>(ninja,false,"ninja name repeated");
    return new BeforeResult<>(ninja, true);
  }

  @Override
  public AfterResult afterCreate(BeforeResult<Ninja> beforeResult, SaveMsg saveMessage) {
    LASClassManager<Ninja> ninjaZEntityManager = LASClassManagerFactory.getManager(Ninja.class);
    //创建完忍者后修改这个忍者的ACL权限
		Map<String,Map<String,Boolean>> acl = new HashMap<>();
		Map<String,Boolean> value = new HashMap<>();
		value.put("read", true);
		value.put("write", true);
		acl.put(saveMessage.objectId().toString(), value);
    LASUpdate update = new LASUpdate().set("ACL", acl);
    ninjaZEntityManager.update(saveMessage.objectId().toString(), update);
    AfterResult afterResult = new AfterResult(saveMessage);
		return afterResult;
  }

}
