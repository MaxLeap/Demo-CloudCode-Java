package handler;

import com.maxleap.code.LASHandler;
import com.maxleap.code.Request;
import com.maxleap.code.Response;
import com.maxleap.code.assist.AssistLASClassManager;
import com.maxleap.code.assist.classes.Coin;
import com.maxleap.code.assist.classes.ReceiptRegular;
import com.maxleap.code.assist.classes.Wallet;
import com.maxleap.code.impl.*;
import com.maxleap.las.sdk.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User：poplar
 * Date：15/7/10
 */
public class AssistHandler {

  /**
   * 测试基础金币系统CRUD
   * @return
   */
  public LASHandler<Request, Response> helloAssist(){
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
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
        update.set("name", "mytest_new");
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
      }
    };
  }

  /**
   * 测试收据相关API
   * @return
   */
  public LASHandler<Request, Response> receiptTransaction(){
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
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
        params.put("uId", "testuid");
        Wallet wallet = walletService.getWallet(params);
        System.out.println(wallet);

        // delete
        coinService.delete(saveMsg.objectIdString());

        response.setResult("ok");
        return response;
      }
    };
  }

  /**
   * 测试钱包相关API
   * @return
   */
  public LASHandler<Request, Response> walletTransaction(){
    return new LASHandler<Request, Response>() {
      @Override
      public Response handle(Request request) {
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
          wallet.setuId("testuid");
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
      }
    };
  }
}
