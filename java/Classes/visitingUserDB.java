package Classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// When accessing a visitingUser's database, it must be accessed through this interface only.
// A visitingUser object can be any visitingUser and can also be the user. However, a user object can only be the user.
public class visitingUserDB {

    final private String userID;

    public visitingUserDB(String userID){
        this.userID = userID;
    }

//setters

//getters

    public String getUserID(){
        return userID;
    }

    public String getUsername(DataSnapshot snapshot){
        return snapshot.child("username").getValue().toString();
    }

    public String getUsernameLowerCase(DataSnapshot snapshot){
        return snapshot.child("usernameLowerCase").getValue().toString();
    }

    public String getName(DataSnapshot snapshot) {
        return snapshot.child("name").getValue().toString();
    }

    public String getTypeArtist(DataSnapshot snapshot) {
        return snapshot.child("typeArtist").getValue().toString();
    }

    public String getBio(DataSnapshot snapshot){
        return snapshot.child("bio").getValue().toString();
    }

    public String getProfilePhoto(DataSnapshot snapshot){
        return snapshot.child("profilePhoto").getValue().toString();
    }

    public long getFollowersCount(DataSnapshot snapshot){
        return snapshot.child("followers").getChildrenCount();
    }

    public long getFollowingCount(DataSnapshot snapshot){
        return snapshot.child("following").getChildrenCount();
    }

// other methods

    public DatabaseReference getDBRef(){
        return FirebaseDatabase.getInstance().getReference().child("Users/" + userID);
    }

}
