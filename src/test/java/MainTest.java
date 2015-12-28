import com.maxleap.code.Response;
import com.maxleap.code.impl.MLJsonParser;
import com.maxleap.code.test.framework.TestCloudCode;
import com.maxleap.code.test.http.HTTPServerMock;
import org.junit.Assert;
import org.junit.Test;

/**
 * User：poplar
 * Date：14-11-5
 */
public class MainTest extends TestCloudCode {

	public MainTest() throws Exception {
		super("http://10.10.10.193:8080");
	}

	@Test
	public void httpTest() throws Exception {
		new HTTPServerMock(new MainTest());
	}

	@Test
	public void hello(){
		String json = "{\"name\":\"jack\",\"ids\":[\"aa\",\"bb\"]}";
		Response response = runFunction("hello", json);
		if (response.succeeded()){
			System.out.println(response.getResult());
		} else {
			Assert.fail(response.getError());
		}
	}

  @Test
  public void helloJob(){
		runJob("helloJob",null);
  }

  @Test
  public void helloNinja(){
    String params = "{\"name\":\"鸣人\"}";
    Response response = runFunction("helloNinja",params);
    Assert.assertTrue(response.succeeded());
    Assert.assertEquals("鸣人_new", response.getResult().toString());
  }

	@Test
	public void helloUser(){
		Response response = runFunction("helloUser", "");
		if (response.succeeded()){
			System.out.println(MLJsonParser.asJson(response.getResult()));
		} else {
			Assert.fail(response.getError());
		}
	}

	@Test
	public void helloUserDelete(){
		Response response = runFunction("helloUserDelete", "");
		if (response.succeeded()){
			System.out.println(MLJsonParser.asJson(response.getResult()));
		} else {
			Assert.fail(response.getError());
		}
	}

	@Test
	public void helloAssist(){
		Response response = runFunction("helloAssist", "");
		if (response.succeeded()){
			System.out.println(response.getResult().toString());
		} else {
			Assert.fail(response.getError());
		}
	}

	@Test
	public void receiptTrans(){
		Response response = runFunction("receiptTransaction", "");
		if (response.succeeded()){
			System.out.println(response.getResult().toString());
		} else {
			Assert.fail(response.getError());
		}
	}

	@Test
	public void walletTrans(){
		Response response = runFunction("walletTransaction", "");
		if (response.succeeded()){
			System.out.println(response.getResult().toString());
		} else {
			Assert.fail(response.getError());
		}
	}

  @Test
  public void helloCounter(){
    Response response = runFunction("helloCounter","");
    Assert.assertTrue(response.succeeded());
  }

  @Test
  public void helloPushMsg(){
    Response response = runFunction("helloPushMsg","");
    Assert.assertTrue(response.succeeded());
  }

}
