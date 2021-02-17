package Classes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// When accessing a service post, it must be accessed through this interface only.
public class serviceDB {

    final private String serviceID;

    public serviceDB(String serviceID){
        this.serviceID = serviceID;
    }

//setters

//getters

    public String getServiceID(){
        return serviceID;
    }

    public String getUser(DataSnapshot snapshot){
        return snapshot.child("user").getValue().toString();
    }

    public String getTitle(DataSnapshot snapshot) {
        return snapshot.child("title").getValue().toString();
    }

    public String getPhoto(DataSnapshot snapshot){
        return snapshot.child("photo").getValue().toString();
    }

    public String getFee(DataSnapshot snapshot){
        return snapshot.child("fee").getValue().toString();
    }

    public String getDescription(DataSnapshot snapshot){
        return snapshot.child("description").getValue().toString();
    }


// other methods

    public DatabaseReference getDBRef(){
        return FirebaseDatabase.getInstance().getReference().child("Services/" + serviceID);
    }

}
