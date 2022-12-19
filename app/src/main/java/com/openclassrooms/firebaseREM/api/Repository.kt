package com.openclassrooms.firebaseREM.api

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.openclassrooms.firebaseREM.Utils
import com.openclassrooms.firebaseREM.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//private const val COLLECTION_USER = "users"
private const val COLLECTION_PROPERTY = "propertys"
private const val COLLECTION_ELEMENT = "elements"

class Repository(private val remDao: REMDao, private val context: Context) {

    private suspend fun updatePropertyId(id: String, newId: String) {
        getPropertysCollection().document(id).update("id", newId)
        remDao.updatePropertyId(id, newId)
    }

    suspend fun updateAgentWhoSells(id: String, newAgentWhoSells: String) {
        getPropertysCollection().document(id).update("agentWhoSells", newAgentWhoSells)
        remDao.updateAgentWhoSells(id, newAgentWhoSells)
    }

    suspend fun updateSaleDate(id: String, newSaleDate: String) {
        getPropertysCollection().document(id).update("saleDate", newSaleDate)
        remDao.updateSaleDate(id, newSaleDate)
    }

    suspend fun updateType(id: String, newType: String) {
        getPropertysCollection().document(id).update("type", newType)
        remDao.updateType(id, newType)
    }

    suspend fun updatePrice(id: String, newPrice: Int) {
        getPropertysCollection().document(id).update("price", newPrice)
        remDao.updatePrice(id, newPrice)
    }

    suspend fun updatePropertyAvatar(id: String, newAvatar: String) {
        getPropertysCollection().document(id).update("avatar1", newAvatar)
        remDao.updatePropertyAvatar(id, newAvatar)
    }

    suspend fun updateDescription(id: String, newDescription: String) {
        getPropertysCollection().document(id).update("description", newDescription)
        remDao.updateDescription(id, newDescription)
    }

    suspend fun updateSurface(id: String, newSurface: Int) {
        getPropertysCollection().document(id).update("surface", newSurface)
        remDao.updateSurface(id, newSurface)
    }

    suspend fun updateCity(id: String, newCity: String) {
        getPropertysCollection().document(id).update("city", newCity)
        remDao.updateCity(id, newCity)
    }

    suspend fun updateAddress(id: String, newAddress: String) {
        getPropertysCollection().document(id).update("address", newAddress)
        remDao.updateAddress(id, newAddress)
    }

    suspend fun updateCloseToParc(id: String, newParcInt: Int) {
        getPropertysCollection().document(id).update("closeToParc", newParcInt)
        remDao.updateCloseToParc(id, newParcInt)
    }

    suspend fun updateCloseToSchools(id: String, newSchoolsInt: Int) {
        getPropertysCollection().document(id).update("closeToSchools", newSchoolsInt)
        remDao.updateCloseToSchools(id, newSchoolsInt)
    }

    suspend fun updateCloseToShops(id: String, newShopsInt: Int) {
        getPropertysCollection().document(id).update("closeToShops", newShopsInt)
        remDao.updateCloseToShops(id, newShopsInt)
    }

    suspend fun updateNumberOfRooms(id: String, newNumberOfRooms: Int) {
        getPropertysCollection().document(id).update("numberOfRooms", newNumberOfRooms)
        remDao.updateNumberOfRooms(id, newNumberOfRooms)
    }

    suspend fun updateNumberOfBedRooms(id: String, newNumberOfBedRooms: Int) {
        getPropertysCollection().document(id)
            .update("numberOfBedrooms", newNumberOfBedRooms)
        remDao.updateNumberOfBedrooms(id, newNumberOfBedRooms)
    }

    suspend fun updateNumberOfBathRooms(id: String, newNumberOfBathRooms: Int) {
        getPropertysCollection().document(id)
            .update("numberOfBathrooms", newNumberOfBathRooms)
        remDao.updateNumberOfBathrooms(id, newNumberOfBathRooms)
    }

    suspend fun updateNumberOfPhotos(id: String, newNumberOfPhotos: Int) {
        getPropertysCollection().document(id).update("numberOfPhotos", newNumberOfPhotos)
        remDao.updateNumberOfPhotos(id, newNumberOfPhotos)
    }

    private suspend fun updateElementId(id: String, newId: String) {
        getElementsCollection().document(id).update("elementId", newId)
        remDao.updateElementId(id, newId)
    }

    suspend fun updateElementSelected(id: String, newSelected: Boolean) {
        getElementsCollection().document(id).update("selected", newSelected)
        remDao.updateElementSelected(id, newSelected)
    }

    suspend fun getFilter(sqLiteQuery: SimpleSQLiteQuery): MutableList<PropertyRoom> {
        return remDao.getFilterPropertys(sqLiteQuery)
    }

    /* PROPERTY Firebase*/

    private fun getPropertysCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY)
    }

    fun getPropertysCollection(
        listener: PropertysListener,
        coroutineScope: CoroutineScope
    ) {
        if (Utils.checkForInternet(context)) {
            val collectionReference =
                FirebaseFirestore.getInstance().collection(COLLECTION_PROPERTY)
            collectionReference.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onPropertysSuccess(mapPropertys(task, coroutineScope))
                } else {
                    Log.d("PropertysListener", "Error getting documents: ", task.exception)
                }
            }
        } else {
            coroutineScope.launch {
            listener.onPropertysSuccess(
                remDao.getAllPropertys().map { propertyRoom -> propertyRoom.toProperty() } )
            }
        }
    }

    private fun mapPropertys(
        task: Task<QuerySnapshot>,
        coroutineScope: CoroutineScope
    ): List<Property> {
        val propertys: MutableList<Property> = ArrayList()
        val propertysRoom: MutableList<PropertyRoom> = ArrayList()
        for (document in task.result) {
            val property: Property = document.toObject(Property::class.java)
            if (property.id != document.id) {
                property.id = document.id
                coroutineScope.launch { updatePropertyId(document.id, document.id) }
            }
            propertys.add(property)
        }
        for (property in propertys) {
            propertysRoom.add(property.toPropertyRoom())
        }
        coroutineScope.launch {
            remDao.deleteAllPropertys(remDao.getAllPropertys().toList())
            remDao.insertList(propertysRoom)
        }
        return propertys
    }

    interface PropertysListener {
        fun onPropertysSuccess(propertys: List<Property?>?)
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
        val propertyToCreate = Property(
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
        getPropertysCollection().add(propertyToCreate)
    }

/* Element */

    private fun getElementsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_ELEMENT)
    }

    fun getElementsCollection(listener: ElementsListener, coroutineScope: CoroutineScope) {
        if (Utils.checkForInternet(context)) {
            val collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_ELEMENT)
            collectionReference.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onElementsSuccess(mapElements(task, coroutineScope))
                } else {
                    Log.d("ElementListener", "Error getting documents: ", task.exception)
                }
            }
        } else {
            coroutineScope.launch { listener.onElementsSuccess(
                remDao.getAllElements().map { elementRoom -> elementRoom.toElement() }) }
        }
    }

    private fun mapElements(
        task: Task<QuerySnapshot>,
        coroutineScope: CoroutineScope
    ): List<Element> {
        val elements: MutableList<Element> = ArrayList()
        val elementsRoom: MutableList<ElementRoom> = ArrayList()
        for (document in task.result) {
            val element: Element = document.toObject(Element::class.java)
            if (element.elementId != document.id) {
                element.elementId = document.id
                coroutineScope.launch { updateElementId(element.elementId, document.id) }
                //remDao.updateElementId(element.elementId, document.id)
            }
            elements.add(element)
        }
        for (element in elements) {
            elementsRoom.add(element.toElementRoom())
        }
        coroutineScope.launch {
            remDao.insertListOfElement(elementsRoom)
        }
        return elements
    }

    fun createElement(
        elementId: String,
        photo: String,
        propertyId: String,
        isSelected: Boolean,
        typeOfElement: String,
    ) {
        val elementToCreate = Element(
            elementId,
            photo,
            propertyId,
            isSelected,
            typeOfElement
        )
        getElementsCollection().add(elementToCreate)
    }

    suspend fun deleteElement(id: String?, onDeleted: () -> Unit) {
        if (id != null) {
            getElementsCollection().document(id).delete()
                .addOnCompleteListener {
                    onDeleted()
                }
                .addOnFailureListener { onDeleted() }
            remDao.deleteByElementId(id)
        }
    }

    interface ElementsListener {
        fun onElementsSuccess(elements: List<Element?>?)
    }
}