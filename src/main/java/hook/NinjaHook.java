package hook;

import com.maxleap.code.*;
import com.maxleap.code.impl.LASClassManagerHookBase;
import com.maxleap.code.impl.LASJsonParser;
import com.maxleap.las.sdk.FindMsg;
import com.maxleap.las.sdk.LASQuery;
import com.maxleap.las.sdk.SaveMsg;
import bean.Ninja;

@ClassManager("Ninja")
public class NinjaHook extends LASClassManagerHookBase<Ninja> {

  Logger logger = LoggerFactory.getLogger(NinjaHook.class);

  @Override
  public BeforeResult<Ninja> beforeCreate(Ninja ninja, UserPrincipal userPrincipal) {
    LASClassManager<Ninja> ninjaZEntityManager = LASClassManagerFactory.getManager(Ninja.class);
    //创建忍者前验证是否重名了
    LASQuery sunQuery = LASQuery.instance();
    sunQuery.equalTo("name", ninja.getName());
    FindMsg<Ninja> findMsg = ninjaZEntityManager.find(sunQuery,userPrincipal);
    if (findMsg.results() != null && findMsg.results().size() > 0) return new BeforeResult<>(ninja,false,"ninja name repeated");
    return new BeforeResult<>(ninja, true);
  }

  @Override
  public AfterResult afterCreate(BeforeResult<Ninja> beforeResult, SaveMsg saveMessage, UserPrincipal userPrincipal) {
    //创建完忍者后在服务器上记录日志，这条日志可以通过console后台查看到
    logger.info("create Ninja complete use " + LASJsonParser.asJson(userPrincipal) + ",saveMsg:"+LASJsonParser.asJson(saveMessage));
		return new AfterResult(saveMessage);
  }

}
