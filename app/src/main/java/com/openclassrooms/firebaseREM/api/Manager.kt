package com.openclassrooms.firebaseREM.api

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.firebaseREM.model.PropertyRoom
import kotlinx.coroutines.CoroutineScope

class Manager(database: REMRoomDatabase, context: Context) {

    private var mRepository: Repository? = null

    init {

        mRepository = Repository(database.remDao(), context)
    }

    // --- COLLECTION REFERENCE ---

    suspend fun getPropertysCollection(listener: Repository.PropertysListener?, coroutineScope: CoroutineScope) {
        if (listener != null) {
            mRepository?.getPropertysCollection(listener, coroutineScope)
        }
    }

    suspend fun getElementsCollection(listener: Repository.ElementsListener?, coroutineScope: CoroutineScope) {
        if (listener != null) {
            mRepository?.getElementsCollection(listener, coroutineScope)
        }
    }

    // --- CREATE FIREBASE ---

    suspend fun createProperty(
        type: String,
        price: Int,
        avatar1: String,
        description: String,
        surface: Int,
        numberOfRooms: Int,
        numberOfBathrooms: Int,
        numberOfBedrooms: Int,
        city: String,
        address: String,
        createDate: String,
        saleDate: String,
        closeToShops: Int,
        closeToSchools: Int,
        closeToParc: Int,
        agentWhoAdd: String,
        agentWhoSells: String,
        numberOfPhotos: Int
    ) {
        mRepository?.createProperty(
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
            numberOfPhotos
        )
    }

    suspend fun createElement(
        elementId: String,
        photo: String,
        propertyId: String,
        isSelected: Boolean,
        typeOfElement: String,
    ) {
        mRepository?.createElement(elementId, photo, propertyId, isSelected, typeOfElement)
    }

    /* fun updateElementSelected(id: String, newSelected: Boolean) {
        mRepository?.updateElementSelected(id, newSelected)
    }*/

    suspend fun updateAgentWhoSells(id: String, newAgentWhoSells: String) {
        mRepository?.updateAgentWhoSells(id, newAgentWhoSells)
    }

    suspend fun updateSaleDate(id: String, newSaleDate: String) {
        mRepository?.updateSaleDate(id, newSaleDate)
    }

    suspend fun updateType(id: String, newType: String) {
        mRepository?.updateType(id, newType)
    }

    suspend fun updatePrice(id: String, newPrice: Int) {
        mRepository?.updatePrice(id, newPrice)
    }

    suspend fun updatePropertyAvatar(id: String, newAvatar: String) {
        mRepository?.updatePropertyAvatar(id, newAvatar)
    }

    suspend fun updateDescription(id: String, newDescription: String) {
        mRepository?.updateDescription(id, newDescription)
    }

    suspend fun updateSurface(id: String, newSurface: Int) {
        mRepository?.updateSurface(id, newSurface)
    }

    suspend fun updateCity(id: String, newCity: String) {
        mRepository?.updateCity(id, newCity)
    }

    suspend fun updateAddress(id: String, newAddress: String) {
        mRepository?.updateAddress(id, newAddress)
    }

    suspend fun updateCloseToParc(id: String, newParcBoolean: Int) {
        mRepository?.updateCloseToParc(id, newParcBoolean)
    }

    suspend fun updateCloseToSchools(id: String, newSchoolsBoolean: Int) {
        mRepository?.updateCloseToSchools(id, newSchoolsBoolean)
    }

    suspend fun updateCloseToShops(id: String, newShopsBoolean: Int) {
        mRepository?.updateCloseToShops(id, newShopsBoolean)
    }

    suspend fun updateNumberOfRooms(id: String, newNumberOfRooms: Int) {
        mRepository?.updateNumberOfRooms(id, newNumberOfRooms)
    }

    suspend fun updateNumberOfBedRooms(id: String, newNumberOfBedRooms: Int) {
        mRepository?.updateNumberOfBedRooms(id, newNumberOfBedRooms)
    }

    suspend fun updateNumberOfBathRooms(id: String, newNumberOfBathRooms: Int) {
        mRepository?.updateNumberOfBathRooms(id, newNumberOfBathRooms)
    }

    suspend fun updateNumberOfPhotos(id: String, newNumberOfPhotos: Int) {
        mRepository?.updateNumberOfPhotos(id, newNumberOfPhotos)
    }

    suspend fun getFilter(sqLiteQuery: SimpleSQLiteQuery): MutableList<PropertyRoom> {
        return mRepository!!.getFilter(sqLiteQuery)
    }

    // --- DELETE FIREBASE ---

    suspend fun deleteElement(id: String?, onDeleted: () -> Unit) {
        mRepository?.deleteElement(id, onDeleted)
    }
}