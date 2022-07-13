package digi.coders.thecapsico.helper.models;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("is_status")
	private String isStatus;

	@SerializedName("name")
	private String name;

	@SerializedName("icon")
	private String icon;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("modified_at")
	private String modifiedAt;

	public String getIsStatus(){
		return isStatus;
	}

	public String getName(){
		return name;
	}

	public String getIcon(){
		return icon;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getModifiedAt(){
		return modifiedAt;
	}
}