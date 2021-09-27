package com.example.collagemanagement.models

import com.google.firebase.database.IgnoreExtraProperties

// [START rtdb_user_class]
@IgnoreExtraProperties
data class User( val Username: String ="Username",val CollageID: String ="CollageId"
                 ,val Email: String="",val Organisation : String="Organisation" ,
                 val Profilepic : String="") {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
// [END rtdb_user_class]