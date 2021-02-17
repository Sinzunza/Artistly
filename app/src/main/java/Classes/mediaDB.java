package Classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// When accessing a media post, it must be accessed through this interface only.
public class mediaDB {

    final private String mediaID;

    public mediaDB(String mediaID){
        this.mediaID = mediaID;
    }

//setters

//getters

    public String getMediaID(){
        return mediaID;
    }

    public String getUser(DataSnapshot snapshot){
        return snapshot.child("user").getValue().toString();
    }

    public String getCaption(DataSnapshot snapshot) {
        return snapshot.child("caption").getValue().toString();
    }

    public String getPhoto(DataSnapshot snapshot){
        return snapshot.child("photo").getValue().toString();
    }


// other methods

    public DatabaseReference getDBRef(){
        return FirebaseDatabase.getInstance().getReference().child("Media/" + mediaID);
    }

}
