package bean;

import com.maxleap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * 系统自建class实体：用户表
 * 需要继承LASObject类，通过jackson来实现序列化和反序列化
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class _User extends LASObject {

  private Boolean emailVerified;
  private String password;
  private Map<String, Map<String, Object>> authData;
  private String email;
  private String username;
  private Integer coin;

  public Boolean getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Boolean emailVerified) {
    this.emailVerified = emailVerified;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Map<String, Map<String, Object>> getAuthData() {
    return authData;
  }

  public void setAuthData(Map<String, Map<String, Object>> authData) {
    this.authData = authData;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getCoin() {
    return coin;
  }

  public void setCoin(Integer coin) {
    this.coin = coin;
  }
}
