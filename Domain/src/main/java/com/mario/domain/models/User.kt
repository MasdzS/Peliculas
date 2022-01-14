package mx.com.satoritech.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User (

    @PrimaryKey
    var id: Long? = null,

    var name:String? = null,

    @SerializedName("last_name")
    var lastName:String? = null,

    @SerializedName("second_last_name")
    var secondLastName:String? = null,

    var phone:String? = null,

    var email:String? = null,

    @Ignore
    var password:String? = null,

    @SerializedName("photo_url")
    var photoUrl:String? = null,

    @SerializedName("api_token")
    var apiToken : String? = null,


)