package Classes;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// When accessing the general firebase database, it must be accessed through this interface only.
public final class artistlyDB {

    private artistlyDB(){
    }
// add / remove methods

    // add a new media post to firebase and return it's mediaID
    public static String createMedia(String user, String caption, Uri photo, String description) {
        final String randomUrl = UUID.randomUUID().toString();
        Map newPost = new HashMap();
        newPost.put(randomUrl + "/user", user);
        newPost.put(randomUrl + "/caption", caption);
        newPost.put(randomUrl + "/photo", photo.toString());
        FirebaseDatabase.getInstance().getReference().child("Media").updateChildren(newPost);
        return randomUrl;
    }

    // add a new service post to firebase and return it's serviceID
    public static String createService(String user, String title, Uri photo, String fee, String description) {
        final String randomUrl = UUID.randomUUID().toString();
        Map newPost = new HashMap();
        newPost.put(randomUrl + "/user", user);
        newPost.put(randomUrl + "/title", title);
        newPost.put(randomUrl + "/photo", photo.toString());
        newPost.put(randomUrl + "/fee", fee);
        newPost.put(randomUrl + "/description", "" + description);
        FirebaseDatabase.getInstance().getReference().child("Services").updateChildren(newPost);
        return randomUrl;
    }

// query methods

    // if path has children equal to "equalTo" then it will return those children
    public static Query equalToQuery(String path, String orderBy, String equalTo){
        DatabaseReference dbRef =FirebaseDatabase.getInstance().getReference().child(path);
        return dbRef.orderByChild(orderBy).equalTo(equalTo);
    }

    // if path has children starting with "startingWith" then it will return those children
    public static Query startingWithQuery(String path, String orderBy, String startingWith){
        DatabaseReference dbRef =FirebaseDatabase.getInstance().getReference().child(path);
        return dbRef.orderByChild(orderBy).startAt(startingWith).endAt(startingWith + "\uf8ff");
    }
}