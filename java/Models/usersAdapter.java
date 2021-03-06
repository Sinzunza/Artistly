package Models;

public class usersAdapter {

    public String username, name, profilePhoto;

    public usersAdapter() {}

    public usersAdapter(String username, String name, String profilePhoto) {
        this.username = username;
        this.name = name;
        this.profilePhoto = profilePhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePhoto() {return profilePhoto; }

    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

}
