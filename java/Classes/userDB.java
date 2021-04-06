package Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static Classes.timeParser.timeInvert;
import static java.lang.Integer.parseInt;

// When accessing the user's database, it must be accessed through this interface only.
// A user object can only be the user. Whereas, a visitingUser object can be both a visitingUser and the user.
public class userDB {

    final private String userID;

    public userDB(){
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    // gets reference to location of Users/userID (note: is not a snapshot of the data, only location reference)
    public DatabaseReference getDBRef(){
        return FirebaseDatabase.getInstance().getReference().child("Users/" + userID);
    }

    // sets new values for the user so it overrides everything. use addVals to not override
    public void newVals(Map newPost){
        getDBRef().setValue(newPost);
    }

    // add or update a value of the user
    public void addVal(String keyRef, String value){
        Map newPost = new HashMap();
        newPost.put(keyRef, value);
        getDBRef().updateChildren(newPost);
    }

    // update multiple values at a time, better than calling updateValue several times because DB is only updated once here.
    public void addVals(Map newPost){
        getDBRef().updateChildren(newPost);
    }

    public void removeVal(String value){
        getDBRef().child(value).removeValue();
    }

    // add visitingUser to user's following and add user to visiting user's followers
    public void addFollowing(DataSnapshot snapshot, otherUserDB theOtherUserDB, String visitingUserUsername){
        Map newPostUser = new HashMap();
        newPostUser.put(theOtherUserDB.getUserID(), getUsernameLowerCase(snapshot));
        getDBRef().child("following").updateChildren(newPostUser);

        Map newPostVisitingUser = new HashMap();
        newPostVisitingUser.put(userID, visitingUserUsername);
        theOtherUserDB.getDBRef().child("followers").updateChildren(newPostVisitingUser);
    }

    // remove visitingUser from user's following and remove user from visiting user's followers
    public void removeFollowing(otherUserDB theOtherUserDB){
        getDBRef().child("following").child(theOtherUserDB.getUserID()).removeValue();
        theOtherUserDB.getDBRef().child("followers").child(userID).removeValue();
    }

    // create a new chat with the message for both theUser and theOtherUser as well as update the lastMessage
    public void createChat(otherUserDB theOtherUserDB, String message, String timeStamp) {
        final String messageUrl = UUID.randomUUID().toString();
        Map newPost = new HashMap();
        newPost.put("messageText", message);
        newPost.put("sender", getUserID());;
        newPost.put("timeStamp", timeStamp);
        getDBRef().child("chats/" + theOtherUserDB.getUserID() + "/" + messageUrl).updateChildren(newPost);
        theOtherUserDB.getDBRef().child("chats/" + getUserID() + "/" + messageUrl).updateChildren(newPost);

        newPost.remove("sender");
        newPost.put("timeStamp", timeInvert(timeStamp));
        newPost.put("user", theOtherUserDB.getUserID());
        getDBRef().child("chats/lastMessages/" + theOtherUserDB.getUserID()).updateChildren(newPost);

        newPost.put("user", getUserID());
        theOtherUserDB.getDBRef().child("chats/lastMessages/" + getUserID()).updateChildren(newPost);
    }

    // add a message to an existing for both theUser and theOtherUser as well as update the lastMessage
    public void newMessage(otherUserDB theOtherUserDB, String message, String timeStamp) {
        final String messageUrl = UUID.randomUUID().toString();
        Map newPost = new HashMap();
        newPost.put("messageText", message);
        newPost.put("sender", getUserID());
        newPost.put("timeStamp", timeStamp);
        getDBRef().child("chats/" + theOtherUserDB.getUserID() + "/" + messageUrl).updateChildren(newPost);
        theOtherUserDB.getDBRef().child("chats/" + getUserID() + "/" + messageUrl).updateChildren(newPost);

        newPost.remove("sender");
        newPost.put("timeStamp", timeInvert(timeStamp));
        newPost.put("user", theOtherUserDB.getUserID());
        getDBRef().child("chats/lastMessages/" + theOtherUserDB.getUserID()).updateChildren(newPost);

        newPost.put("user", getUserID());
        theOtherUserDB.getDBRef().child("chats/lastMessages/" + getUserID()).updateChildren(newPost);
    }

}
