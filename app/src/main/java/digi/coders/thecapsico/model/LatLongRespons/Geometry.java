package digi.coders.thecapsico.model.LatLongRespons;

import com.google.gson.annotations.SerializedName;

public class Geometry{

	@SerializedName("viewport")
	private Viewport viewport;

	@SerializedName("location")
	private Location location;

	public Viewport getViewport(){
		return viewport;
	}

	public Location getLocation(){
		return location;
	}
}