package com.xdl.jjg.model.co;

import com.shopx.common.util.StringUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 选器实体
 * @author waf
 * @version v2.0
 * @since v7.0.0
 */
@Data
public class SearchSelector implements Serializable {

	private String paramKey;
	private String paramValue;
	private String name;
	private String url;
	private boolean isSelected;
	private String value;
	private List<SearchSelector> otherOptions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		if(!StringUtil.isEmpty(url) && url.startsWith("/")){
			url= url.substring(1, url.length());
		}
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getIsSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<SearchSelector> getOtherOptions() {
		return otherOptions;
	}
	public void setOtherOptions(List<SearchSelector> otherOptions) {
		this.otherOptions = otherOptions;
	}

	@Override
	public String toString() {
		return "SearchSelector{" +
				"name='" + name + '\'' +
				", url='" + url + '\'' +
				", isSelected=" + isSelected +
				", value='" + value + '\'' +
				", otherOptions=" + otherOptions +
				'}';
	}
}
