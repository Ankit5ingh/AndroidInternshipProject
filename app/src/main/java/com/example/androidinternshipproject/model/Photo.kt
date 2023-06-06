package com.example.androidinternshipproject.model

data class Photo(
    val farm: Int,
    val height_s: Int,
    val id: String,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String,
    val url_s: String,
    val width_s: Int
): java.io.Serializable {

    //    this code below is to remove a null id as id is null by default and it creates nullPointException
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url_s.isEmpty()){
            result = 31 * result + url_s.hashCode()
        }
        return result
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false
        if (owner != other.owner) return false
        if (title != other.title) return false
        if (url_s != other.url_s) return false
        if (server != other.server) return false
        if (secret != other.secret) return false
        if (isfamily != other.isfamily) return false
        if (isfriend != other.isfriend) return false
        if (ispublic != other.ispublic) return false

        return true
    }
}