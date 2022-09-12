package com.openclassrooms.firebaseREM.api

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.openclassrooms.firebaseREM.model.Agent
import java.time.LocalDate

class Manager() {

    var mRepository: Repository? = null

    init {
        mRepository = Repository()
    }

    // --- COLLECTION REFERENCE ---

    fun getUsersCollection(listener: Repository.AgentsListener?) {
        if (listener != null) {
            mRepository?.getUsersCollection(listener)
        }
    }

    fun getPropertysCollection(listener: Repository.PropertysListener?) {
        if (listener != null) {
            mRepository?.getPropertysCollection(listener)
        }
    }

    fun getElementsCollection(listener: Repository.ElementsListener?) {
        if (listener != null) {
            mRepository?.getElementsCollection(listener)
        }
    }

    // --- CREATE FIREBASE ---

    fun createUser(id: String?, avatar: String?, name: String?): Task<Void?>? {
        return mRepository?.createUser(id, avatar, name)
    }

    fun createProperty(
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
        closeToShops: Boolean,
        closeToSchools: Boolean,
        closeToParc: Boolean,
        agentWhoAdd: String,
        agentWhoSells: String,
        numberOfPhotos: Int
    ): Task<DocumentReference?>? {
        return mRepository?.createProperty(
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

    fun createElement(
        photo: String,
        propertyId: String?,
        isSelected: Boolean,
        typeOfElement: String?
    ): Task<DocumentReference?>? {
        return mRepository?.createElement(photo, propertyId, isSelected, typeOfElement)
    }

    // --- GET FIREBASE ---

    fun getUser(id: String?, listener: Repository.OnUserSuccessListener?) {
        if (listener != null) {
            mRepository?.getUser(id, listener)
        }
    }

    fun getCurrentUser(): Agent? {
        return mRepository?.getCurrentUser()
    }

    // --- UPDATE FIREBASE ---

    fun updateUserName(id: String?, name: String?): Task<Void?>? {
        return mRepository?.updateUserName(id, name)
    }

    fun updateUserAvatar(id: String?, avatar: String?): Task<Void?>? {
        return mRepository?.updateUserAvatar(id, avatar)
    }

    fun updateElementSelected(id: String?, newSelected: Boolean): Task<Void?>? {
        return mRepository?.updateElementSelected(id, newSelected)
    }

    fun updateAgentWhoSells(id: String?, newAgentWhoSells: String?): Task<Void?>? {
        return mRepository?.updateAgentWhoSells(id, newAgentWhoSells)
    }

    fun updateSaleDate(id: String?, newSaleDate: String?): Task<Void?>? {
        return mRepository?.updateSaleDate(id, newSaleDate)
    }

    fun updateType(id: String?, newType: String?): Task<Void?>? {
        return mRepository?.updateType(id, newType)
    }

    fun updatePrice(id: String?, newPrice: Int?): Task<Void?>? {
        return mRepository?.updatePrice(id, newPrice)
    }

    fun updatePropertyAvatar(id: String?, newAvatar: String?): Task<Void?>? {
        return mRepository?.updatePropertyAvatar(id, newAvatar)
    }

    fun updateDescription(id: String?, newDescription: String?): Task<Void?>? {
        return mRepository?.updateDescription(id, newDescription)
    }

    fun updateSurface(id: String?, newSurface: Int?): Task<Void?>? {
        return mRepository?.updateSurface(id, newSurface)
    }

    fun updateCity(id: String?, newCity: String?): Task<Void?>? {
        return mRepository?.updateCity(id, newCity)
    }

    fun updateAddress(id: String?, newAddress: String?): Task<Void?>? {
        return mRepository?.updateAddress(id, newAddress)
    }

    fun updateCloseToParcBoolean(id: String?, newParcBoolean: Boolean?): Task<Void?>? {
        return mRepository?.updateCloseToParcBoolean(id, newParcBoolean)
    }

    fun updateCloseToSchoolsBoolean(id: String?, newSchoolsBoolean: Boolean?): Task<Void?>? {
        return mRepository?.updateCloseToSchoolsBoolean(id, newSchoolsBoolean)
    }

    fun updateCloseToShopsBoolean(id: String?, newShopsBoolean: Boolean?): Task<Void?>? {
        return mRepository?.updateCloseToShopsBoolean(id, newShopsBoolean)
    }

    fun updateNumberOfRooms(id: String?, newNumberOfRooms: Int?): Task<Void?>? {
        return mRepository?.updateNumberOfRooms(id, newNumberOfRooms)
    }

    fun updateNumberOfBedRooms(id: String?, newNumberOfBedRooms: Int?): Task<Void?>? {
        return mRepository?.updateNumberOfBedRooms(id, newNumberOfBedRooms)
    }

    fun updateNumberOfBathRooms(id: String?, newNumberOfBathRooms: Int?): Task<Void?>? {
        return mRepository?.updateNumberOfBathRooms(id, newNumberOfBathRooms)
    }

    fun updateNumberOfPhotos(id: String?, newNumberOfPhotos: Int?): Task<Void?>? {
        return mRepository?.updateNumberOfPhotos(id, newNumberOfPhotos)
    }

    // --- DELETE FIREBASE ---

    fun deleteUser(id: String?) {
        mRepository?.deleteUser(id)
    }

    fun deleteElement(id: String?, onDeleted: () -> Unit) {
        mRepository?.deleteElement(id, onDeleted)
    }
}