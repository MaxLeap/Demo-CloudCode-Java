package hook;

import com.maxleap.code.*;
import com.maxleap.code.impl.MLClassManagerHookBase;
import com.maxleap.code.impl.MLJsonParser;
import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.MLQuery;
import com.maxleap.las.sdk.SaveMsg;
import bean.Ninja;

@ClassManager("Ninja")
public class NinjaHook extends MLClassManagerHookBase<Ninja> {

  Logger logger = LoggerFactory.getLogger(NinjaHook.class);

  @Override
  public BeforeResult<Ninja> beforeCreate(Ninja ninja, UserPrincipal userPrincipal) {
    logger.info("hook before:"+MLJsonParser.asJson(userPrincipal));
    MLClassManager<Ninja> ninjaZEntityManager = MLClassManagerFactory.getManager(Ninja.class);
    //创建忍者前验证是否重名了
    MLQuery sunQuery = MLQuery.instance();
    sunQuery.equalTo("name", ninja.getName());
    FindMsg<Ninja> findMsg = ninjaZEntityManager.find(sunQuery,userPrincipal);
    if (findMsg.results() != null && findMsg.results().size() > 0) return new BeforeResult<>(ninja,false,"ninja name repeated");
    return new BeforeResult<>(ninja, true);
  }

  @Override
  public AfterResult afterCreate(BeforeResult<Ninja> beforeResult, SaveMsg saveMessage, UserPrincipal userPrincipal) {
    //创建完忍者后在服务器上记录日志，这条日志可以通过console后台查看到
    logger.info("create Ninja complete use " + MLJsonParser.asJson(userPrincipal) + ",saveMsg:"+MLJsonParser.asJson(saveMessage));
		return new AfterResult(saveMessage);
  }

}
