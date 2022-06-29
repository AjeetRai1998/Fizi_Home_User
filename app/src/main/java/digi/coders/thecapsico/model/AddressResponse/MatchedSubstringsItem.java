package digi.coders.thecapsico.model.AddressResponse;

import com.google.gson.annotations.SerializedName;

public class MatchedSubstringsItem{

	@SerializedName("offset")
	private int offset;

	@SerializedName("length")
	private int length;

	public int getOffset(){
		return offset;
	}

	public int getLength(){
		return length;
	}
}