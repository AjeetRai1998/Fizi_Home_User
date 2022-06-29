package digi.coders.thecapsico.model.LatLongRespons;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("utc_offset")
	private int utcOffset;

	@SerializedName("formatted_address")
	private String formattedAddress;

	@SerializedName("types")
	private List<String> types;

	@SerializedName("website")
	private String website;

	@SerializedName("icon")
	private String icon;

	@SerializedName("icon_background_color")
	private String iconBackgroundColor;

	@SerializedName("address_components")
	private List<AddressComponentsItem> addressComponents;

	@SerializedName("photos")
	private List<PhotosItem> photos;

	@SerializedName("url")
	private String url;

	@SerializedName("reference")
	private String reference;

	@SerializedName("name")
	private String name;

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("icon_mask_base_uri")
	private String iconMaskBaseUri;

	@SerializedName("vicinity")
	private String vicinity;

	@SerializedName("adr_address")
	private String adrAddress;

	@SerializedName("place_id")
	private String placeId;

	public int getUtcOffset(){
		return utcOffset;
	}

	public String getFormattedAddress(){
		return formattedAddress;
	}

	public List<String> getTypes(){
		return types;
	}

	public String getWebsite(){
		return website;
	}

	public String getIcon(){
		return icon;
	}

	public String getIconBackgroundColor(){
		return iconBackgroundColor;
	}

	public List<AddressComponentsItem> getAddressComponents(){
		return addressComponents;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public String getUrl(){
		return url;
	}

	public String getReference(){
		return reference;
	}

	public String getName(){
		return name;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public String getIconMaskBaseUri(){
		return iconMaskBaseUri;
	}

	public String getVicinity(){
		return vicinity;
	}

	public String getAdrAddress(){
		return adrAddress;
	}

	public String getPlaceId(){
		return placeId;
	}
}