package com.openclassrooms.firebaseREM.api

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.firebaseREM.model.ElementRoom
import com.openclassrooms.firebaseREM.model.PropertyRoom
import java.util.*

@Dao
interface REMDao {

    @Insert
    suspend fun createProperty(vararg propertyRoom: PropertyRoom)

    @Insert
    suspend fun createElement(vararg elementRoom: ElementRoom)

    @Delete
    suspend fun deleteElement(elementRoom: ElementRoom)

    @Delete
    suspend fun deleteAllPropertys(propertys: List<PropertyRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(propertys: List<PropertyRoom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfElement(elements: List<ElementRoom>)

    @Query("DELETE FROM ElementRoom WHERE elementId = :id")
    suspend fun deleteByElementId(id: String)

    @Query("SELECT * FROM PropertyRoom")
    suspend fun getAllPropertys(): Array<PropertyRoom>

    @RawQuery
    suspend fun getFilterPropertys(query: SimpleSQLiteQuery): MutableList<PropertyRoom>

    @Query("SELECT * FROM PropertyRoom")
    fun getAllPropertysCursor(): Cursor

    @Query("SELECT * FROM ElementRoom")
    suspend fun getAllElements(): Array<ElementRoom>

    @Query("UPDATE PropertyRoom SET id=:newId WHERE id = :propertyId")
    suspend fun updatePropertyId(propertyId: String, newId: String)

    @Query("UPDATE PropertyRoom SET agentWhoSells=:newAgentWhoSells WHERE id = :propertyId")
    suspend fun updateAgentWhoSells(propertyId: String, newAgentWhoSells: String)

    @Query("UPDATE PropertyRoom SET saleDate=:newSaleDate WHERE id = :propertyId")
    suspend fun updateSaleDate(propertyId: String, newSaleDate: String)

    @Query("UPDATE PropertyRoom SET type=:newType WHERE id = :propertyId")
    suspend fun updateType(propertyId: String, newType: String)

    @Query("UPDATE PropertyRoom SET price=:newPrice WHERE id = :propertyId")
    suspend fun updatePrice(propertyId: String, newPrice: Int)

    @Query("UPDATE PropertyRoom SET avatar1=:newPropertyAvatar WHERE id = :propertyId")
    suspend fun updatePropertyAvatar(propertyId: String, newPropertyAvatar: String)

    @Query("UPDATE PropertyRoom SET description=:newDescription WHERE id = :propertyId")
    suspend fun updateDescription(propertyId: String, newDescription: String)

    @Query("UPDATE PropertyRoom SET surface=:newSurface WHERE id = :propertyId")
    suspend fun updateSurface(propertyId: String, newSurface: Int)

    @Query("UPDATE PropertyRoom SET city=:newCity WHERE id = :propertyId")
    suspend fun updateCity(propertyId: String, newCity: String)

    @Query("UPDATE PropertyRoom SET address=:newAddress WHERE id = :propertyId")
    suspend fun updateAddress(propertyId: String, newAddress: String)

    @Query("UPDATE PropertyRoom SET closeToParc=:newCloseToParc WHERE id = :propertyId")
    suspend fun updateCloseToParc(propertyId: String, newCloseToParc: Int)

    @Query("UPDATE PropertyRoom SET closeToSchools=:newCloseToSchools WHERE id = :propertyId")
    suspend fun updateCloseToSchools(propertyId: String, newCloseToSchools: Int)

    @Query("UPDATE PropertyRoom SET closeToShops=:newCloseToShops WHERE id = :propertyId")
    suspend fun updateCloseToShops(propertyId: String, newCloseToShops: Int)

    @Query("UPDATE PropertyRoom SET numberOfRooms=:newNumberOfRooms WHERE id = :propertyId")
    suspend fun updateNumberOfRooms(propertyId: String, newNumberOfRooms: Int)

    @Query("UPDATE PropertyRoom SET numberOfBedrooms=:newNumberOfBedrooms WHERE id = :propertyId")
    suspend fun updateNumberOfBedrooms(propertyId: String, newNumberOfBedrooms: Int)

    @Query("UPDATE PropertyRoom SET numberOfBathrooms=:newNumberOfBathrooms WHERE id = :propertyId")
    suspend fun updateNumberOfBathrooms(propertyId: String, newNumberOfBathrooms: Int)

    @Query("UPDATE PropertyRoom SET numberOfPhotos=:newNumberOfPhotos WHERE id = :propertyId")
    suspend fun updateNumberOfPhotos(propertyId: String, newNumberOfPhotos: Int)

    @Query("UPDATE ElementRoom SET elementId=:newId WHERE elementId = :elementId")
    suspend fun updateElementId(elementId: String, newId: String)

    @Query("UPDATE ElementRoom SET isSelected=:newElementSelected WHERE elementId = :elementId")
    suspend fun updateElementSelected(elementId: String, newElementSelected: Boolean)

    /* @Query("SELECT * FROM PropertyRoom WHERE id = :id")
    fun getProperty(id: String?): Property

    @Query("SELECT * FROM ElementRoom WHERE elementId = :id")
    fun getElement(id: String?): Element */
}