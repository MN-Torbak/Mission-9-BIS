package com.openclassrooms.firebaseREM.viewmodel

import com.openclassrooms.firebaseREM.REMApplication
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.firebaseREM.api.Manager
import com.openclassrooms.firebaseREM.api.Repository
import com.openclassrooms.firebaseREM.model.Element
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.model.PropertyRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var mManager: Manager = Manager(
        (getApplication() as REMApplication).database,
        getApplication<Application>().applicationContext
    )
    val propertys = MutableLiveData<List<Property?>?>(listOf())
    val elements = MutableLiveData<List<Element?>?>(listOf())
    private val liveLocation = MutableLiveData<Location?>()
    fun updateLocation(location: Location?) {
        liveLocation.value = location
    }


    init {
        getPropertysCollection(object : Repository.PropertysListener {
            override fun onPropertysSuccess(propertys: List<Property?>?) {
                this@MainViewModel.propertys.value = propertys
            }
        }, viewModelScope)
        getElementsCollection(object : Repository.ElementsListener {
            override fun onElementsSuccess(elements: List<Element?>?) {
                this@MainViewModel.elements.value = elements
            }
        }, viewModelScope)
    }

    private fun getPropertysCollection(listener: Repository.PropertysListener?, coroutineScope: CoroutineScope) {
        viewModelScope.launch {
            mManager.getPropertysCollection(listener, coroutineScope)
        }
    }

    private fun getElementsCollection(listener: Repository.ElementsListener?, coroutineScope: CoroutineScope) {
        viewModelScope.launch {
            mManager.getElementsCollection(listener, coroutineScope)
        }
    }

    fun refreshPropertysCollection() {
        getPropertysCollection(object : Repository.PropertysListener {
            override fun onPropertysSuccess(propertys: List<Property?>?) {
                this@MainViewModel.propertys.value = propertys
            }
        }, viewModelScope)
    }

    fun refreshElementsCollection() {
        getElementsCollection(object : Repository.ElementsListener {
            override fun onElementsSuccess(elements: List<Element?>?) {
                this@MainViewModel.elements.value = elements
            }
        }, viewModelScope)
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
        closeToShops: Int,
        closeToSchools: Int,
        closeToParc: Int,
        agentWhoAdd: String,
        agentWhoSells: String,
        numberOfPhotos: Int
    ) {
        viewModelScope.launch {
            getPropertysCollection(object : Repository.PropertysListener {
                override fun onPropertysSuccess(propertys: List<Property?>?) {
                    this@MainViewModel.propertys.value = propertys
                }
            }, viewModelScope)
            mManager.createProperty(
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
    }

    fun createElement(
        elementId: String,
        photo: String,
        propertyId: String,
        isSelected: Boolean,
        typeOfElement: String
    ) {
        viewModelScope.launch {
            getElementsCollection(object  : Repository.ElementsListener {
                override fun onElementsSuccess(elements: List<Element?>?) {
                    this@MainViewModel.elements.value = elements
                }
            }, viewModelScope)
            mManager.createElement(elementId, photo, propertyId, isSelected, typeOfElement)
        }
    }

    fun deleteElement(id: String?, onDeleted: () -> Unit) {
        viewModelScope.launch {
            mManager.deleteElement(id, onDeleted)
        }
    }

    /* fun updateElementSelected (id: String, newSelected: Boolean) {
        mManager.updateElementSelected(id, newSelected)
    }*/

    fun updateAgentWhoSells(id: String, newAgentWhoSells: String) {
        viewModelScope.launch {
            mManager.updateAgentWhoSells(id, newAgentWhoSells)
        }
    }

    fun updateSaleDate(saleDate: String, newSaleDate: String) {
        viewModelScope.launch {
            mManager.updateSaleDate(saleDate, newSaleDate)
        }
    }

    fun updateType(type: String, newType: String) {
        viewModelScope.launch {
            mManager.updateType(type, newType)
        }
    }

    fun updatePrice(price: String, newPrice: Int) {
        viewModelScope.launch {
            mManager.updatePrice(price, newPrice)
        }
    }

    fun updatePropertyAvatar(avatar: String, newAvatar: String) {
        viewModelScope.launch {
            mManager.updatePropertyAvatar(avatar, newAvatar)
        }
    }

    fun updateDescription(description: String, newDescription: String) {
        viewModelScope.launch {
            mManager.updateDescription(description, newDescription)
        }
    }

    fun updateSurface(surface: String, newSurface: Int) {
        viewModelScope.launch {
            mManager.updateSurface(surface, newSurface)
        }
    }

    fun updateCity(city: String, newCity: String) {
        viewModelScope.launch {
            mManager.updateCity(city, newCity)
        }
    }

    fun updateAddress(city: String, newAddress: String) {
        viewModelScope.launch {
            mManager.updateAddress(city, newAddress)
        }
    }

    fun updateCloseToParc(id: String, newParcBoolean: Int) {
        viewModelScope.launch {
            mManager.updateCloseToParc(id, newParcBoolean)
        }
    }

    fun updateCloseToSchools(id: String, newSchoolsBoolean: Int) {
        viewModelScope.launch {
            mManager.updateCloseToSchools(id, newSchoolsBoolean)
        }
    }

    fun updateCloseToShops(id: String, newShopsBoolean: Int) {
        viewModelScope.launch {
            mManager.updateCloseToShops(id, newShopsBoolean)
        }
    }

    fun updateNumberOfRooms(id: String, newNumberOfRooms: Int) {
        viewModelScope.launch {
            mManager.updateNumberOfRooms(id, newNumberOfRooms)
        }
    }

    fun updateNumberOfBedRooms(id: String, newNumberOfBedRooms: Int) {
        viewModelScope.launch {
            mManager.updateNumberOfBedRooms(id, newNumberOfBedRooms)
        }
    }

    fun updateNumberOfBathRooms(id: String, newNumberOfBathRooms: Int) {
        viewModelScope.launch {
            mManager.updateNumberOfBathRooms(id, newNumberOfBathRooms)
        }
    }

    fun updateNumberOfPhotos(id: String, newNumberOfPhotos: Int) {
        viewModelScope.launch {
            mManager.updateNumberOfPhotos(id, newNumberOfPhotos)
        }
    }

    val filterList = MutableLiveData<MutableList<PropertyRoom>>()
    fun getFilter(sqLiteQuery: SimpleSQLiteQuery) {
        viewModelScope.launch {
            filterList.value = mManager.getFilter(sqLiteQuery)
        }
    }
}