package digi.coders.thecapsico.model.AddressResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponsePlace{

	@SerializedName("predictions")
	private List<PredictionsItem> predictions;

	@SerializedName("status")
	private String status;

	public List<PredictionsItem> getPredictions(){
		return predictions;
	}

	public String getStatus(){
		return status;
	}
}