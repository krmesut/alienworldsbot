package entities;

public class PlayerProfile {
	private String username;
	private String password;
	private String profilePath;

	public PlayerProfile(String username, String password, String profilePath) {
		super();
		this.username = username;
		this.password = password;
		this.profilePath = profilePath;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getProfilePath() {
		return profilePath;
	}

}
