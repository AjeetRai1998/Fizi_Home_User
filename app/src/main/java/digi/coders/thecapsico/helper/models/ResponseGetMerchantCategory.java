package digi.coders.thecapsico.helper.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetMerchantCategory{

	@SerializedName("res")
	private String res;

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("message")
	private String message;

	public String getRes(){
		return res;
	}

	public List<DataItem> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}
}