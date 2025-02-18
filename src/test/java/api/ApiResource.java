package api;

public enum ApiResource{
	
	////enum is special class in java which has collection of constants or  methods
	AddPlaceAPI("/maps/api/place/add/json"),
	getPlaceAPI("/maps/api/place/get/json"),
	deletePlaceAPI("/maps/api/place/delete/json");
	
	private String resource;

	public String getResource() {
		return resource;
	}

	ApiResource(String resource) {
		this.resource = resource;
	}
	
	
	
}