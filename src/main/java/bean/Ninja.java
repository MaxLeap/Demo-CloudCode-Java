package bean;


import com.maxleap.las.sdk.MLObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 自定义class实体：忍者表
 * 需要继承MLObject类，通过jackson来实现序列化和反序列化
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ninja extends MLObject {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
