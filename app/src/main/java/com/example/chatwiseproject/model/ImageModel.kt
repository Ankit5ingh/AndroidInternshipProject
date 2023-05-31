package com.example.chatwiseproject.model

data class ImageModel(
    val id: Int,
    val albumId : Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : java.io.Serializable {

    //    this code below is to remove a null id as id is null by default and it creates nullPointException
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (id != other.id) return false
        if (albumId != other.albumId) return false
        if (title != other.title) return false
        if (url != other.url) return false
        if (thumbnailUrl != other.thumbnailUrl) return false

        return true
    }
}