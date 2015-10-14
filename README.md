### LAS-Demo-CloudCode-Java

##如何使用LAS-CloudCode-SDK开发你的应用

# 添加LAS-CloudCode-SDK依赖

添加cloudcode的本地开发测试框架依赖，这样你就可以使用cloudcode提供的服务了

```xml
<dependency>
  <groupId>com.maxleap</groupId>
  <artifactId>cloud-code-test</artifactId>
  <version>2.4.0</version>
  <scope>provided</scope>
</dependency>
```

*这个依赖包含了基础的SDK客户端和本地单元测试框架*
    
# 使用LAS-CloudCode-SDK提供的服务

- 定义你的第一个function

      进入主程序入口(main函数)
      
      ```java
      defineFunction("hello", request -> {
          Response<String> response = new LASResponse<String>(String.class);
          response.setResult(request.parameter(String.class));
          return response;
      });
      ```
         
      *使用defineFunction来定义你的function*
  
- 定义你的第一个job

      进入主程序入口(main函数)
      
      ```java
      defineJob("helloJob", request -> {
          Response response = new LASResponse(String.class);
          response.setResult("hello job");
          return response;
      });
      ```
      
      *使用defineJob来定义你的job，job一般用于定时执行或者需要花费较长时间运行的程序*

# 使用LAS-CloudCode-SDK来完成你的业务
1. 新建Class(一个Class实体对应后端数据库中的一张表)
    + console中新建Class
    + cloudcode中新建Class
        我们新建一个忍者实体
        
        ```java
        public class Ninja extends LASObject {
            private String name;
        
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
        }
        ```
        
        *推荐继承LASObject对象，每张表初始化后都会自动产生几个默认的字段如objectId、createdAt、updatedAt、ACL*
        **所有自定义实体必须在同一个package下，并在global.json配置中标注该package选项，如下：**
        `"packageClasses" : "bean"`
2. 操作Class实现你的业务
    这里我们简单实现一个业务逻辑，提交一个忍者名称，生成一个忍者本体和它的50个影分身，找出其中第50个分身，击杀其余分身和本体，让它成为新的本体

    ```java
    public LASHandler<Request, Response> helloNinja() {
        final LASClassManager<Ninja> ninjaZEntityManager = LASClassManagerFactory.getManager(Ninja.class);
        return new LASHandler<Request, Response>() {
          @Override
          public Response handle(Request request) {
            Ninja ninja = request.parameter(Ninja.class);
            String name = ninja.getName();
            //产生本体
            SaveResult<Ninja> saveMsg = ninjaZEntityManager.create(ninja);
            String ninjaObjectId = saveMsg.getSaveMessage().objectId().toString();
            LOGGER.info("生成本体，ID为：" + ninjaObjectId);
            //产生50个分身
            List<String> cloneObjectIds = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
              ninja.setName(name + "_" + i);
              SaveResult<Ninja> cloneSave = ninjaZEntityManager.create(ninja);
              String cloneObjectId = cloneSave.getSaveMessage().objectId().toString();
              cloneObjectIds.add(cloneObjectId);
              LOGGER.info("多重影分身：" + cloneSave.getSaveMessage().objectId().toString());
            }
            //找出第50个分身
            LASQuery sunQuery = LASQuery.instance();
            sunQuery.equalTo("name", name + "_" + 50);
            FindMsg<Ninja> findMsg = ninjaZEntityManager.find(sunQuery);
            Ninja ninja_50 = findMsg.results().get(0);
            cloneObjectIds.remove(ninja_50.objectIdString());
            LOGGER.info("找到第50个分身：" + LASJsonParser.asJson(ninja_50));
            //击杀其余49个分身
            DeleteResult deleteResult = ninjaZEntityManager.delete(cloneObjectIds.toArray(new String[cloneObjectIds.size()]));
            LOGGER.info("完成分身击杀数目：" + deleteResult.getDeleteMessage().number());
            //击杀本体
            ninjaZEntityManager.delete(ninjaObjectId);
            LOGGER.info("完成本体击杀");
            //让第50分身成为新的本体
            LASUpdate update = LASUpdate.getUpdate();
            update.set("name", name + "_new");
            UpdateMsg updateMsg = ninjaZEntityManager.update(ninja_50.objectIdString(), update);
            LOGGER.info("第50个分身在" + updateMsg.updatedAtString() + "成为新的本体");
            //返回新的本体名称
            Ninja ninja_new = ninjaZEntityManager.findById(ninja_50.objectIdString());
            Response<String> response = new LASResponse<>(String.class);
            response.setResult(ninja_new.getName());
            ninjaZEntityManager.delete(ninja_50.objectIdString());
            return response;
          }
        };
    }
    ```

    ***通过实体工厂得到你要操作的实体对象管理者来完成相关操作
    `LASClassManager<Ninja> ninjaZEntityManager = LASClassManagerFactory.getManager(Ninja.class);`***
    
    ***整个过程中系统会自动捕获异常并返回给你***        
3. 注册你的服务接口
    进入主程序入口(main函数)

    ```java
    defineFunction("helloNinja", new NinjaHandler().helloNinja());
    ```
    
4. 本地单元测试你的服务

  ```
    public class MainTest extends TestCloudCode {
        @Test
        public void helloNinja(){
            String params = "{\"name\":\"鸣人\"}";
            Response response = runFunction("helloNinja",params);
            Assert.assertTrue(response.succeeded());
            Assert.assertEquals("鸣人_new",response.getResult().toString());
        }
    }
  ```

  ***新建单元测试，继承TestCloudCode***
  
  ***测试用例运行在本地，会自动代理产品环境的测试数据***
  
  ***请确保测试用例本地全部通过再进行发布，否则你的服务将可能无法发布成功***

# 使用SDK实现HOOK操作
***内建Collection和自定义Collection均支持Hook，内建Collection原有的限制（_User用户名和密码必填，_Installation的deviceToken和installationId二选一）依然有效。***
*支持beforeCreate，afterCreate，beforeDelete，afterDelete，afterUpdate*
我们实现一个简单的HOOK操作

```java
@ClassManager("Ninja")
public class NinjaHook extends LASClassManagerHookBase<Ninja> {
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
```
    
*Hook类需要实现LASClassManagerHook接口，建议直接继承LASClassManagerHookBase类，它默认为你做了实现，你想要hook操作直接重载对应的方法即可*

*Hook类上需要添加@ClassManager注解，以便服务器能够识别该HOOK是针对哪个实体的*

*所有Hook必须在同一个package下，并在global.json配置中标注该选项，如下：*
`"packageHook" : "hook"`
    
# 使用日志

*你可以在Main, Hook, Handler等任意地方中使用日志功能，只需使用com.maxleap.code包下的日志类即可，比如：`Logger logger = LoggerFactory.getLogger(NinjaHook.class);`，而正常的log4j或slf4j日志将不会被远程服务器记录，但可以在本地使用*

*目前有info，warn，error，debug四个级别，服务器上只会记录info、warn和error级别的日志*

*本地测试不会产生远程数据库日志记录，但发布后会产生记录，你可以在后端界面查看你的日志信息*

*如果你的function调用频率很高请在发布前尽量去掉调试测试日志以便不必要的日志存储*