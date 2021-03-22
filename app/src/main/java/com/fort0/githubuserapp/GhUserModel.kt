import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GhUserModel (
        val fname: String? = "",
        val uname: String? = "",
        val userpic: Int? = 0,
        val location: String? = "",
        val company: String? = "",
        val following: String? = "",
        val followers: String? = "",
        val repository: String? = ""
) : Parcelable