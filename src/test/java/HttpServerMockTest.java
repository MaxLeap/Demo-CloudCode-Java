import com.maxleap.code.test.framework.TestCloudCode;
import com.maxleap.code.test.http.HTTPServerMock;

/**
 * User：David Young
 * Date：15/12/8
 */
public class HttpServerMockTest {

  public static void main(String[] args) throws Exception {
    HTTPServerMock httpServerMock = new HTTPServerMock(9090,new TestCloudCode("http://10.10.10.193:8080"));
  }
}
