package entities.http;

public enum HTTPContentTypes {
	FORM_URL_ENCODED("application/x-www-form-urlencoded");

	public final String label;

	private HTTPContentTypes(String label) {
		// TODO Auto-generated constructor stub
		this.label = label;
	}
}
