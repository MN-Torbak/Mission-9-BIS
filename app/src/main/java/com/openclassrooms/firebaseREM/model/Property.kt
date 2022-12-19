package com.openclassrooms.firebaseREM.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.annotations.NotNull
import java.io.Serializable

data class Property(
    var type: String = "",
    var price: Int = 0,
    var avatar1: String = "",
    var description: String = "",
    var surface: Int = 0,
    var numberOfRooms: Int = 0,
    var numberOfBathrooms: Int = 0,
    var numberOfBedrooms: Int = 0,
    var city: String = "",
    var address: String = "",
    val createDate: String = "",
    var saleDate: String = "",
    var closeToShops: Int = 0,
    var closeToSchools: Int = 0,
    var closeToParc: Int = 0,
    val agentWhoAdd: String = "",
    var agentWhoSells: String = "",
    var numberOfPhotos: Int = 0,
    var id: String = "",
) : Serializable

@Entity
data class PropertyRoom(
    @PrimaryKey @NotNull var id: String = "",
    @ColumnInfo(name = "type") val type: String = "",
    @ColumnInfo(name = "price") val price: Int = 0,
    @ColumnInfo(name = "avatar1") val avatar1: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "surface") val surface: Int = 0,
    @ColumnInfo(name = "numberOfRooms") val numberOfRooms: Int = 0,
    @ColumnInfo(name = "numberOfBathrooms") val numberOfBathrooms: Int = 0,
    @ColumnInfo(name = "numberOfBedrooms") val numberOfBedrooms: Int = 0,
    @ColumnInfo(name = "city") val city: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "createDate") val createDate: String = "",
    @ColumnInfo(name = "saleDate") val saleDate: String = "",
    @ColumnInfo(name = "closeToShops") val closeToShops: Int = 0,
    @ColumnInfo(name = "closeToSchools") val closeToSchools: Int = 0,
    @ColumnInfo(name = "closeToParc") val closeToParc: Int = 0,
    @ColumnInfo(name = "agentWhoAdd") val agentWhoAdd: String = "",
    @ColumnInfo(name = "agentWhoSells") val agentWhoSells: String = "",
    @ColumnInfo(name = "numberOfPhotos") var numberOfPhotos: Int = 0,
)

fun PropertyRoom.toProperty() = Property(
    type,
    price,
    avatar1,
    description,
    surface,
    numberOfRooms,
    numberOfBathrooms,
    numberOfBedrooms,
    city,
    address,
    createDate,
    saleDate,
    closeToShops,
    closeToSchools,
    closeToParc,
    agentWhoAdd,
    agentWhoSells,
    numberOfPhotos,
    id
)

fun Property.toPropertyRoom() = PropertyRoom(
    id,
    type,
    price,
    avatar1,
    description,
    surface,
    numberOfRooms,
    numberOfBathrooms,
    numberOfBedrooms,
    city,
    address,
    createDate,
    saleDate,
    closeToShops,
    closeToSchools,
    closeToParc,
    agentWhoAdd,
    agentWhoSells,
    numberOfPhotos,
)


data class Element(
    var elementId: String = "",
    val photo: String = "",
    val propertyId: String = "",
    var isSelected: Boolean,
    var typeOfElement: String = ""
) {
    @Suppress("unused")
    constructor() : this("", "", "",false, "")
}

@Entity
data class ElementRoom(
    @PrimaryKey @NotNull var elementId: String = "",
    @ColumnInfo(name = "photo") val photo: String = "",
    @ColumnInfo(name = "propertyId") val propertyId: String = "",
    @ColumnInfo(name = "isSelected") val isSelected: Boolean,
    @ColumnInfo(name = "typeOfElement") val typeOfElement: String = "",
)

fun ElementRoom.toElement() = Element(
    elementId,
    photo,
    propertyId,
    isSelected,
    typeOfElement
)

fun Element.toElementRoom() = ElementRoom(
    elementId,
    photo,
    propertyId,
    isSelected,
    typeOfElement
)



