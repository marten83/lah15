package se.martenolsson.lah15;

public class WorldPopulation {

	private String title;
	private String musik;
	private String place;
	private String text;
	private String mp3;
	private String image;

	public WorldPopulation(String title, String musik, String text, String place, String mp3, String image) {
		this.title = title;
		this.musik = musik;
		this.place = place;
		this.text = text;
		this.mp3 = mp3;
		this.image = image;
	}

	public String getTitle() {return this.title;}

	public String getMusik() {return this.musik;}

	public String getPlace() { return this.place;}

	public String getText() {
		return this.text;
	}

	public String getMp3() {return this.mp3;}

	public String getImage() { return this.image;}
}
