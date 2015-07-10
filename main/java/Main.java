import as.leap.code.*;
import as.leap.code.assist.AssistLASClassManager;
import as.leap.code.assist.classes.Coin;
import as.leap.code.assist.classes.ReceiptRegular;
import as.leap.code.assist.classes.Wallet;
import as.leap.code.impl.*;
import as.leap.las.sdk.*;
import bean._User;
import handler.NinjaHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * User：poplar
 * Date：14-11-5
 */
public class Main extends LoaderBase implements Loader {

	Logger logger = LoggerFactory.getLogger(Main.class);

	@Override
	public void main(GlobalConfig globalConfig) {

		defineFunction("hello", request -> {
			Response<Map> response = new LASResponse<>(Map.class);
			Map map = request.parameter(Map.class);
			System.out.println(map);
			response.setResult(map);
			return response;
		});

		defineFunction("helloUser", request1 -> {
			LASClassManager<_User> userZEntityManager = LASClassManagerFactory.getManager(_User.class);
			_User user = new _User();
			user.setUsername("jack2");
			user.setPassword("123456");
			SaveResult saveMsg = userZEntityManager.create(user);
			System.out.println(saveMsg.getSaveMessage());

			LASUpdate update = LASUpdate.getUpdate();
			update.set("password","123");
			UpdateMsg updateMsg = userZEntityManager.update(saveMsg.getSaveMessage().objectIdString(), update);
			System.out.println(updateMsg);

			DeleteResult deleteResult = userZEntityManager.delete(saveMsg.getSaveMessage().objectIdString());
			System.out.println(deleteResult);
			LASQuery lasQuery = LASQuery.instance();
			lasQuery.equalTo("name", "aa");
			FindMsg<_User> findMsg = userZEntityManager.find(lasQuery);
			System.out.println(findMsg);
			LASResponse LASResponse = new LASResponse(FindMsg.class);
			LASResponse.setResult(findMsg);
			return LASResponse;
		});

		defineFunction("helloUserDelete", request1 -> {
			LASClassManager<_User> userZEntityManager = LASClassManagerFactory.getManager(_User.class);
			_User user = new _User();
			user.setUsername("jack222");
			user.setPassword("123456");
			SaveResult saveMsg = userZEntityManager.create(user);
			System.out.println(saveMsg.getSaveMessage());

			_User user2 = new _User();
			user2.setUsername("jack333");
			user2.setPassword("123456");
			SaveResult saveMsg2 = userZEntityManager.create(user2);
			System.out.println(saveMsg2.getSaveMessage());

      _User user3 = new _User();
      user3.setUsername("jack444");
      user3.setPassword("123456");
      SaveResult saveMsg3 = userZEntityManager.create(user3);
      System.out.println(saveMsg3.getSaveMessage());

      LASQuery lasQuery = LASQuery.instance();
      lasQuery.notEqualTo("username", "");
      lasQuery.setLimit(10);
      lasQuery.sort(LASQuery.SORT_ASC,"updatedAt");
      FindMsg<_User> findMsg = userZEntityManager.find(lasQuery);
      System.out.println(findMsg);

			DeleteResult deleteResult = userZEntityManager.delete(new String[]{saveMsg.getSaveMessage().objectIdString(), saveMsg2.getSaveMessage().objectIdString(), saveMsg3.getSaveMessage().objectIdString()});
			System.out.println(deleteResult);
			LASResponse LASResponse = new LASResponse(FindMsg.class);
			LASResponse.setResult(findMsg);
			return LASResponse;
		});

		defineFunction("helloAssist", request -> {
			Response<String> response = new LASResponse<>(String.class);
			AssistLASClassManager<Coin> coinService = new AssistLASClassManagerImpl<>(Coin.class);

			//create
			Coin coin = new Coin();
			coin.setDesc("mytestDesc");
			coin.setName("mytest");
			SaveMsg saveMsg = coinService.create(coin);
			System.out.println(saveMsg);

			//findById
			Coin coin2 = coinService.findById(saveMsg.objectIdString());
			System.out.println(LASJsonParser.asJson(coin2));

			//update
			LASUpdate update = LASUpdate.getUpdate();
			update.set("name","mytest_new");
			update.set("desc", "mytestDesc_new");
			UpdateMsg updateMsg = coinService.update(saveMsg.objectIdString(), update);
			System.out.println(updateMsg);

			//find
			LASQuery lasQuery = LASQuery.instance();
			lasQuery.equalTo("name", "mytest_new");
			FindMsg<Coin> coinFindMsg = coinService.find(lasQuery);
			System.out.println(coinFindMsg);

			//delete
			DeleteMsg deleteMsg = coinService.delete(coinFindMsg.results().get(0).objectIdString());
			response.setResult("ok");
			return response;
		});

		defineJob("helloJob", request -> {
			Response response = new LASResponse(String.class);
			response.setResult("hello job");
			return response;
		});

		defineFunction("helloNinja", new NinjaHandler().helloNinja());

		defineFunction("receiptTransaction", request->{
			Response<String> response = new LASResponse<>(String.class);

      AssistLASClassManager<Coin> coinService = new AssistLASClassManagerImpl<>(Coin.class);
			ReceiptRegularLASClassManager receiptRegularService = new ReceiptRegularLASClassManager(ReceiptRegular.class);
      WalletLASClassManager walletService = new WalletLASClassManager(Wallet.class);

			// define a coin
			Coin coin = new Coin();
			coin.setName("slvtest");
			coin.setDesc("slvtest");
			SaveMsg saveMsg = coinService.create(coin);
			System.out.println(saveMsg);

			// define a receiptRegular
			ReceiptRegular receiptRegular = new ReceiptRegular();
			receiptRegular.setCount(100);
			receiptRegular.setAmount(7.7);
			receiptRegular.setName("slvtest");
			receiptRegular.setPlatform("GooglePlay");
			receiptRegular.setStoreProdId("com.appchannelmobi.farmsaga.handofcoins");
			receiptRegular.setCoinId(saveMsg.objectIdString());
			SaveMsg saveMsg1 = receiptRegularService.create(receiptRegular);
			System.out.println(saveMsg1);

			// transaction
			try {
				Map receiptInfo = new HashMap();
				receiptInfo.put("receipt", "eyJwYWNrYWdlTmFtZSI6ImNvbS5hcHBjaGFubmVsbW9iaS5mYXJtc2FnYSIsInRva2VuIjoiaGJjbGlrY21uYmtjYmhqZWpqcGZwbWNrLkFPLUoxT3pyS1VaWHZjQkZzTlBOT2xKdGpoYWRRTkFzeHVDU0E1N29zSGE0SzlMYU96a3BEZ3R6WUt4Ykx4b25XLTY2ZmJLOE81QnhrZ1AxakN4LUx6SEpOVk1wRlVDblNMYUlHc0JTbEZQYlNERTFHdlQzd2FMX1RJdllNeXFNVnVyT1EwSXppSWktTjZsR1BNVlZzSzA3MnlpR2NOQ0dMYmYtVVQ5cnBsRVZmNGxlUS0wT1g0VSIsIm9yZGVySWQiOiIxMjk5OTc2MzE2OTA1NDcwNTc1OC4xMzMwNTIwMjI3NTEwOTUwIiwicHVyY2hhc2VTdGF0ZSI6MCwicHJvZHVjdElkIjoiY29tLmFwcGNoYW5uZWxtb2JpLmZhcm1zYWdhLmhhbmRvZmNvaW5zIiwicHVyY2hhc2VUaW1lIjoxNDI2MDU4MzI3NzA0fQ==");
				receiptInfo.put("platform", "GooglePlay");
				Map result = receiptRegularService.transaction(saveMsg1.objectIdString(), receiptInfo);
				System.out.println(result);
			}catch (Exception e){
				System.out.println(e.getMessage());
			}

      // get wallet
			Map params = new HashMap();
      Wallet wallet = walletService.getWallet(params);
      System.out.println(wallet);

      // delete
      coinService.delete(saveMsg.objectIdString());

			response.setResult("ok");
			return response;
		});

    defineFunction("walletTransaction", request->{
      Response<String> response = new LASResponse<>(String.class);

      AssistLASClassManager<Coin> coinService = new AssistLASClassManagerImpl<>(Coin.class);
      ReceiptRegularLASClassManager receiptRegularService = new ReceiptRegularLASClassManager(ReceiptRegular.class);
      WalletLASClassManager walletService = new WalletLASClassManager(Wallet.class);

      // define a coin
      Coin coin = new Coin();
      coin.setName("slvtest");
      coin.setDesc("slvtest");
      SaveMsg saveMsg = coinService.create(coin);
      System.out.println(saveMsg);

      // define a receiptRegular
      ReceiptRegular receiptRegular = new ReceiptRegular();
      receiptRegular.setCount(100);
      receiptRegular.setAmount(7.7);
      receiptRegular.setName("slvtest");
      receiptRegular.setPlatform("GooglePlay");
      receiptRegular.setStoreProdId("com.appchannelmobi.farmsaga.handofcoins");
      receiptRegular.setCoinId(saveMsg.objectIdString());
      SaveMsg saveMsg1 = receiptRegularService.create(receiptRegular);
      System.out.println(saveMsg1);

      // create a wallet if not exist
			Wallet wallet = null;
      try {
        wallet = walletService.getWallet(new HashMap<>());
      }catch (Exception e){
      }
      if (wallet == null){
				wallet = new Wallet();
				SaveMsg saveMsg2 = walletService.create(wallet);
				System.out.println(saveMsg2);
				wallet.setObjectId(saveMsg2.objectId());
			}

      // transaction
      try {
        Map receiptInfo = new HashMap();
        receiptInfo.put("receipt", "eyJwYWNrYWdlTmFtZSI6ImNvbS5hcHBjaGFubmVsbW9iaS5mYXJtc2FnYSIsInRva2VuIjoiaGJjbGlrY21uYmtjYmhqZWpqcGZwbWNrLkFPLUoxT3pyS1VaWHZjQkZzTlBOT2xKdGpoYWRRTkFzeHVDU0E1N29zSGE0SzlMYU96a3BEZ3R6WUt4Ykx4b25XLTY2ZmJLOE81QnhrZ1AxakN4LUx6SEpOVk1wRlVDblNMYUlHc0JTbEZQYlNERTFHdlQzd2FMX1RJdllNeXFNVnVyT1EwSXppSWktTjZsR1BNVlZzSzA3MnlpR2NOQ0dMYmYtVVQ5cnBsRVZmNGxlUS0wT1g0VSIsIm9yZGVySWQiOiIxMjk5OTc2MzE2OTA1NDcwNTc1OC4xMzMwNTIwMjI3NTEwOTUwIiwicHVyY2hhc2VTdGF0ZSI6MCwicHJvZHVjdElkIjoiY29tLmFwcGNoYW5uZWxtb2JpLmZhcm1zYWdhLmhhbmRvZmNvaW5zIiwicHVyY2hhc2VUaW1lIjoxNDI2MDU4MzI3NzA0fQ==");
        receiptInfo.put("platform", "GooglePlay");
        receiptInfo.put("receiptRegularId", saveMsg1.objectIdString());
        Map result = walletService.transaction(wallet.objectIdString(), receiptInfo);
        System.out.println(result);
      }catch (Exception e){
        System.out.println(e.getMessage());
      }

      // get wallet
      Wallet wallet1 = walletService.findById(wallet.objectIdString());
      System.out.println(wallet1);

      // delete
      coinService.delete(saveMsg.objectIdString());

      response.setResult("ok");
      return response;
    });
	}

}