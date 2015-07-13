package bean;


import as.leap.las.sdk.LASObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * User：poplar
 * Date：15-5-14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ninja extends LASObject {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
