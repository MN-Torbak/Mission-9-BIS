package com.openclassrooms.firebaseREM.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.openclassrooms.firebaseREM.api.Manager
import com.openclassrooms.firebaseREM.api.Repository
import com.openclassrooms.firebaseREM.model.Agent
import com.openclassrooms.firebaseREM.model.Property
import java.time.LocalDate

class MainViewModel : ViewModel() {
    var mManager: Manager
    val Propertys = MutableLiveData<List<Property?>>(listOf())
    fun getLiveLocation(): LiveData<Location?> { return liveLocation }
    private val liveLocation = MutableLiveData<Location>()
    fun updateLocation(location: Location?) { liveLocation.value = location }

    init {
        mManager = Manager()
        getPropertysCollection(object : Repository.PropertysListener {
            override fun onPropertysSuccess(propertys: List<Property?>?) {
                Propertys.value = propertys
            }
        })
    }

    fun getUsersCollection(listener: Repository.AgentsListener?) {
        mManager.getUsersCollection(listener)
    }

    fun getPropertysCollection(listener: Repository.PropertysListener?) {
        mManager.getPropertysCollection(listener)
    }

    fun getElementsCollection(listener: Repository.ElementsListener?) {
        mManager.getElementsCollection(listener)
    }

    fun createUser(id: String?, avatar: String?, name: String?): Task<Void?>? {
        return mManager.createUser(id, avatar, name)
    }

    fun getUser(id: String?, listener: Repository.OnUserSuccessListener?) {
        mManager.getUser(id, listener)
    }

    fun getCurrentUser(): Agent? {
        return mManager.getCurrentUser()
    }

    fun updateUserName(id: String?, name: String?): Task<Void?>? {
        return mManager.updateUserName(id, name)
    }

    fun updateUserAvatar(id: String?, avatar: String?): Task<Void?>? {
        return mManager.updateUserAvatar(id, avatar)
    }

    fun deleteUser(id: String?) {
        mManager.deleteUser(id)
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
        getPropertysCollection(object : Repository.PropertysListener {
            override fun onPropertysSuccess(propertys: List<Property?>?) {
                Propertys.value = propertys
            }
        })
        return mManager.createProperty(
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

    fun createElement(photo: String, propertyId: String?, isSelected: Boolean, typeOfElement: String?): Task<DocumentReference?>? {
        return mManager.createElement(photo, propertyId, isSelected, typeOfElement)
    }

    fun deleteElement(id: String?, onDeleted: () -> Unit) {
        mManager.deleteElement(id, onDeleted)
    }

    fun updateElementSelected (id: String?, newSelected: Boolean) {
        mManager.updateElementSelected(id, newSelected)
    }

    fun updateAgentWhoSells(id: String?, newAgentWhoSells: String?): Task<Void?>? {
        return mManager.updateAgentWhoSells(id, newAgentWhoSells)
    }

    fun updateSaleDate(saleDate: String?, newSaleDate: String?): Task<Void?>? {
        return mManager.updateSaleDate(saleDate, newSaleDate)
    }

    fun updateType(type: String?, newType: String?): Task<Void?>? {
        return mManager.updateType(type, newType)
    }

    fun updatePrice(price: String?, newPrice: Int?): Task<Void?>? {
        return mManager.updatePrice(price, newPrice)
    }

    fun updatePropertyAvatar(avatar: String?, newAvatar: String?): Task<Void?>? {
        return mManager.updatePropertyAvatar(avatar, newAvatar)
    }

    fun updateDescription(description: String?, newDescription: String?): Task<Void?>? {
        return mManager.updateDescription(description, newDescription)
    }

    fun updateSurface(surface: String?, newSurface: Int?): Task<Void?>? {
        return mManager.updateSurface(surface, newSurface)
    }

    fun updateCity(city: String?, newCity: String?): Task<Void?>? {
        return mManager.updateCity(city, newCity)
    }

    fun updateAddress(city: String?, newAddress: String?): Task<Void?>? {
        return mManager.updateAddress(city, newAddress)
    }

    fun updateCloseToParcBoolean(id: String?, newParcBoolean: Boolean?): Task<Void?>? {
        return mManager.updateCloseToParcBoolean(id, newParcBoolean)
    }

    fun updateCloseToSchoolsBoolean(id: String?, newSchoolsBoolean: Boolean?): Task<Void?>? {
        return mManager.updateCloseToSchoolsBoolean(id, newSchoolsBoolean)
    }

    fun updateCloseToShopsBoolean(id: String?, newShopsBoolean: Boolean?): Task<Void?>? {
        return mManager.updateCloseToShopsBoolean(id, newShopsBoolean)
    }

    fun updateNumberOfRooms(id: String?, newNumberOfRooms: Int?): Task<Void?>? {
        return mManager.updateNumberOfRooms(id, newNumberOfRooms)
    }

    fun updateNumberOfBedRooms(id: String?, newNumberOfBedRooms: Int?): Task<Void?>? {
        return mManager.updateNumberOfBedRooms(id, newNumberOfBedRooms)
    }

    fun updateNumberOfBathRooms(id: String?, newNumberOfBathRooms: Int?): Task<Void?>? {
        return mManager.updateNumberOfBathRooms(id, newNumberOfBathRooms)
    }

    fun updateNumberOfPhotos(id: String?, newNumberOfPhotos: Int?): Task<Void?>? {
        return mManager.updateNumberOfPhotos(id, newNumberOfPhotos)
    }
}