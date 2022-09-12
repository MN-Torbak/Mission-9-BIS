package com.openclassrooms.firebaseREM

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.firebaseREM.api.Repository
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel
import java.io.IOException
import java.text.NumberFormat
import java.util.*


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
@Suppress("DEPRECATION")
class ItemDetailFragment : Fragment(), OnMapReadyCallback {

    private var twoPane: Boolean = false
    private var item: Property? = null
    var mRecyclerView: RecyclerView? = null
    var mMainViewModel: MainViewModel? = null
    private var mMap: GoogleMap? = null
    private var itemLatLng: LatLng? = null
    private lateinit var closeToCommodity: String
    private lateinit var itemDetailAdapter: ItemDetailAdapter
    lateinit var buttonCancel: Button
    lateinit var buttonSubmit: Button
    lateinit var datePickerSaleDate: DatePicker
    lateinit var nameOfSalesman: TextView


    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_ITEM_ID)) {
                item = bundle.getSerializable(ARG_ITEM_ID) as? Property
                activity?.findViewById<TextView>(R.id.appBarDetailTextView)?.text = item?.type
            } else if (bundle.containsKey(ARG_ITEM_ID_BY_STRING)) {
                val arguments = bundle.getString(ARG_ITEM_ID_BY_STRING).toString()
                mMainViewModel?.Propertys?.observe(this) {
                    if (it != null) {
                        for (property in it) {
                            if (property?.address.toString().equals(arguments)) {
                                item = property
                                activity?.findViewById<TextView>(R.id.appBarDetailTextView)?.text =
                                    item?.type
                                activity?.findViewById<TextView>(R.id.detail_description)?.text =
                                    item?.description
                                activity?.findViewById<TextView>(R.id.detail_surface)?.text =
                                    item?.surface.toString()
                                activity?.findViewById<TextView>(R.id.detail_numberofrooms)?.text =
                                    item?.numberOfRooms.toString()
                                activity?.findViewById<TextView>(R.id.detail_numberofbathrooms)?.text =
                                    item?.numberOfBathrooms.toString()
                                activity?.findViewById<TextView>(R.id.detail_numberofbedrooms)?.text =
                                    item?.numberOfBedrooms.toString()
                                activity?.findViewById<TextView>(R.id.detail_address)?.text =
                                    item?.address
                                activity?.findViewById<TextView>(R.id.detail_city)?.text =
                                    item?.city
                                if (item?.closeToShops == true && item?.closeToSchools == false && item?.closeToParc == false) {
                                    closeToCommodity = "Shops"
                                } else if (item?.closeToShops == false && item?.closeToSchools == true && item?.closeToSchools == false) {
                                    closeToCommodity = "Schools"
                                } else if (item?.closeToShops == false && item?.closeToSchools == false && item?.closeToParc == true) {
                                    closeToCommodity = "Parc"
                                } else if (item?.closeToShops == true && item?.closeToSchools == true && item?.closeToParc == false) {
                                    closeToCommodity = "Shops and Schools"
                                } else if (item?.closeToShops == true && item?.closeToSchools == false && item?.closeToParc == true) {
                                    closeToCommodity = "Shops and Parc"
                                } else if (item?.closeToShops == false && item?.closeToSchools == true && item?.closeToParc == true) {
                                    closeToCommodity = "Schools and Parc"
                                } else if (item?.closeToShops == true && item?.closeToSchools == true && item?.closeToParc == true) {
                                    closeToCommodity = "Shops, Schools and Parc"
                                } else {
                                    activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.visibility =
                                        View.GONE
                                }
                                activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text =
                                    closeToCommodity
                                activity?.findViewById<TextView>(R.id.detail_address)?.text = item?.address
                                activity?.findViewById<TextView>(R.id.detail_city)?.text = item?.city
                                activity?.findViewById<TextView>(R.id.detail_add_by_title)?.text = "Added by "
                                activity?.findViewById<TextView>(R.id.detail_name_of_agent)?.text =
                                    item?.agentWhoAdd + ", "
                                activity?.findViewById<TextView>(R.id.detail_create_date)?.text =
                                    item?.createDate
                                if (item?.saleDate == "") {
                                    activity?.findViewById<TextView>(R.id.detail_sold_by_title)?.visibility =
                                        View.GONE
                                    activity?.findViewById<TextView>(R.id.detail_name_of_agent_who_sold)?.visibility =
                                        View.GONE
                                    activity?.findViewById<TextView>(R.id.detail_sale_date)?.visibility = View.GONE
                                } else {
                                    activity?.findViewById<TextView>(R.id.detail_sold_by_title)?.text = "Sold by "
                                    activity?.findViewById<TextView>(R.id.detail_name_of_agent_who_sold)?.text =
                                        item?.agentWhoSells + ", "
                                    activity?.findViewById<TextView>(R.id.detail_sale_date)?.text =
                                        item?.saleDate
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)
        mRecyclerView = rootView.findViewById(R.id.fragmentItemDetailRecyclerView)
        if (rootView.findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }
        activity?.findViewById<ImageButton>(R.id.add_element)?.setOnClickListener {
            addElement()
        }

        activity?.findViewById<ImageButton>(R.id.delete_element)?.setOnClickListener {
            deleteElement(item?.let { createPhotoListElement(it, itemDetailAdapter.mElement) })
        }

        activity?.findViewById<ImageButton>(R.id.modify_property)?.setOnClickListener {
            modifyProperty()
        }

        activity?.findViewById<ImageButton>(R.id.sold_property)?.setOnClickListener {
            val dialog = context?.let { it1 -> Dialog(it1) }
            if (dialog != null) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_detail_sold)
                datePickerSaleDate = dialog.findViewById(R.id.datePickerSaleDate)
                nameOfSalesman = dialog.findViewById(R.id.real_estate_salesman)
                buttonCancel = dialog.findViewById(R.id.cancel)
                buttonCancel.setOnClickListener {
                    dialog.dismiss()
                }
                buttonSubmit = dialog.findViewById(R.id.submit)
                buttonSubmit.setOnClickListener {
                    mMainViewModel?.updateAgentWhoSells(item?.id, nameOfSalesman.text.toString())
                    mMainViewModel?.updateSaleDate(item?.id, "" + datePickerSaleDate.getDayOfMonth() + "/" + (datePickerSaleDate.getMonth()+1) + "/" + datePickerSaleDate.getYear())
                    dialog.dismiss()
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).text = "Sold by "
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                        nameOfSalesman.text.toString() + ", "
                    rootView.findViewById<TextView>(R.id.detail_sale_date).text =
                        "" + datePickerSaleDate.getDayOfMonth() + "/" + (datePickerSaleDate.getMonth()+1) + "/" + datePickerSaleDate.getYear()
                }
                dialog.show()
            }
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.detail_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        getElements()
        item.let { currentItem ->
            rootView.findViewById<TextView>(R.id.detail_title).text = "Media"
            rootView.findViewById<TextView>(R.id.detail_description_title).text = "Description"
            rootView.findViewById<TextView>(R.id.detail_surface_title).text = "Surface"
            rootView.findViewById<TextView>(R.id.detail_numberofrooms_title).text =
                "Number of rooms"
            rootView.findViewById<TextView>(R.id.detail_numberofbathrooms_title).text =
                "Number of bathrooms"
            rootView.findViewById<TextView>(R.id.detail_numberofbedrooms_title).text =
                "Number of bedrooms"
            rootView.findViewById<TextView>(R.id.detail_close_to_title).text =
                "Close to: "
            rootView.findViewById<TextView>(R.id.detail_address_title).text = "Location"
            if (currentItem != null) {
                rootView.findViewById<TextView>(R.id.detail_description).text =
                    currentItem.description
                rootView.findViewById<TextView>(R.id.detail_surface).text =
                    currentItem.surface.toString() + " m²"
                rootView.findViewById<TextView>(R.id.detail_numberofrooms).text =
                    currentItem.numberOfRooms.toString()
                rootView.findViewById<TextView>(R.id.detail_numberofbathrooms).text =
                    currentItem.numberOfBathrooms.toString()
                rootView.findViewById<TextView>(R.id.detail_numberofbedrooms).text =
                    currentItem.numberOfBedrooms.toString()
                if (currentItem.closeToShops == true && currentItem.closeToSchools == false && currentItem.closeToParc == false) {
                    closeToCommodity = "Shops"
                } else if (currentItem.closeToShops == false && currentItem.closeToSchools == true && currentItem.closeToSchools == false) {
                    closeToCommodity = "Schools"
                } else if (currentItem.closeToShops == false && currentItem.closeToSchools == false && currentItem.closeToParc == true) {
                    closeToCommodity = "Parc"
                } else if (currentItem.closeToShops == true && currentItem.closeToSchools == true && currentItem.closeToParc == false) {
                    closeToCommodity = "Shops and Schools"
                } else if (currentItem.closeToShops == true && currentItem.closeToSchools == false && currentItem.closeToParc == true) {
                    closeToCommodity = "Shops and Parc"
                } else if (currentItem.closeToShops == false && currentItem.closeToSchools == true && currentItem.closeToParc == true) {
                    closeToCommodity = "Schools and Parc"
                } else if (currentItem.closeToShops == true && currentItem.closeToSchools == true && currentItem.closeToParc == true) {
                    closeToCommodity = "Shops, Schools and Parc"
                } else {
                    rootView.findViewById<TextView>(R.id.detail_close_to_commodity).visibility =
                        View.GONE
                }
                rootView.findViewById<TextView>(R.id.detail_close_to_commodity).text =
                    closeToCommodity
                rootView.findViewById<TextView>(R.id.detail_address).text = currentItem.address
                rootView.findViewById<TextView>(R.id.detail_city).text = currentItem.city
                rootView.findViewById<TextView>(R.id.detail_add_by_title).text = "Added by "
                rootView.findViewById<TextView>(R.id.detail_name_of_agent).text =
                    currentItem.agentWhoAdd + ", "
                rootView.findViewById<TextView>(R.id.detail_create_date).text =
                    currentItem.createDate
                if (currentItem.saleDate == "") {
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).visibility =
                        View.GONE
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).visibility =
                        View.GONE
                    rootView.findViewById<TextView>(R.id.detail_sale_date).visibility = View.GONE
                } else {
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).text = "Sold by "
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                        currentItem.agentWhoSells + ", "
                    rootView.findViewById<TextView>(R.id.detail_sale_date).text =
                        currentItem.saleDate
                }
                rootView.findViewById<ImageButton>(R.id.modify_property)?.setOnClickListener {
                    modifyProperty()
                }

                rootView.findViewById<ImageButton>(R.id.sold_property)?.setOnClickListener {
                    val dialog = context?.let { it1 -> Dialog(it1) }
                    if (dialog != null) {
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.dialog_detail_sold)
                        datePickerSaleDate = dialog.findViewById(R.id.datePickerSaleDate)
                        nameOfSalesman = dialog.findViewById(R.id.real_estate_salesman)
                        buttonCancel = dialog.findViewById(R.id.cancel)
                        buttonCancel.setOnClickListener {
                            dialog.dismiss()
                        }
                        buttonSubmit = dialog.findViewById(R.id.submit)
                        buttonSubmit.setOnClickListener {
                            mMainViewModel?.updateAgentWhoSells(item?.id, nameOfSalesman.text.toString())
                            mMainViewModel?.updateSaleDate(item?.id, "" + datePickerSaleDate.getDayOfMonth() + "/" + (datePickerSaleDate.getMonth()+1) + "/" + datePickerSaleDate.getYear())
                            dialog.dismiss()
                            rootView.findViewById<TextView>(R.id.detail_sold_by_title).text = "Sold by "
                            rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                                nameOfSalesman.text.toString() + ", "
                            rootView.findViewById<TextView>(R.id.detail_sale_date).text =
                                "" + datePickerSaleDate.getDayOfMonth() + "/" + (datePickerSaleDate.getMonth()+1) + "/" + datePickerSaleDate.getYear()
                        }
                        dialog.show()
                    }
                }
                rootView.findViewById<ImageButton>(R.id.add_element)?.setOnClickListener {
                    addElement()
                }

                rootView.findViewById<ImageButton>(R.id.delete_element)?.setOnClickListener {
                    deleteElement(item?.let { createPhotoListElement(it, itemDetailAdapter.mElement) })
                }
            }
        }

        return rootView
    }

    private fun getElements() {
        mMainViewModel?.getElementsCollection(object : Repository.ElementsListener {
            override fun onElementsSuccess(elements: List<Element?>?) {
                if (elements != null) {
                    displayPhotoProperty(item?.let { createPhotoListElement(it, elements) })
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayPhotoProperty(photoElementList: ArrayList<Element>?) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        mRecyclerView?.setLayoutManager(layoutManager)
        itemDetailAdapter = ItemDetailAdapter(photoElementList)
        itemDetailAdapter.notifyDataSetChanged()
        mRecyclerView?.setAdapter(itemDetailAdapter)
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private fun createPhotoListElement(
        property: Property,
        ElementList: List<Element?>?
    ): ArrayList<Element> {
        val mPhotoElement = ArrayList<Element>()

        if (ElementList != null) {
            for (element in ElementList) {
                if (element?.propertyId == property.id) {
                    if (element != null) {
                        mPhotoElement.add(element)
                    }
                }
            }
        }
        return mPhotoElement
    }

    private fun addElement() {
        val fragment = AddElementFragment().apply {
            arguments = Bundle().apply {
                putSerializable(AddElementFragment.ARG_ITEM_ID, item)
            }
        }

        if (twoPane) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutDetail, fragment)
                .commit()
        } else {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.item_detail_container, fragment).addToBackStack(this.toString())
                .commit()
        }
    }

    private fun modifyProperty() {
        val fragment = ModifyPropertyFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ITEM_ID, item)
            }
        }

        if (twoPane) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutDetail, fragment)
                .commit()
        } else {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.item_detail_container, fragment).addToBackStack(this.toString())
                .commit()
        }
    }

    fun deleteElement(elementList: ArrayList<Element>?) {
        var intChangeNumberOfPhotos = 0
        if (elementList != null) {
            for (element in elementList) {
                if (element.isSelected == true) {
                    mMainViewModel?.deleteElement(element.elementId, onElementDeleted)
                    intChangeNumberOfPhotos += 1
                }
            }
        }
        var updatingNumberOfPhotos = item?.numberOfPhotos?.minus(intChangeNumberOfPhotos)
        mMainViewModel?.updateNumberOfPhotos(item?.id, (updatingNumberOfPhotos))
        if (updatingNumberOfPhotos != null) {
            item?.numberOfPhotos = updatingNumberOfPhotos
        }
    }

    var onElementDeleted: () -> Unit = {
        getElements()
    }

    private var simpleCallback = object :
        ItemTouchHelper.SimpleCallback((ItemTouchHelper.START).or(ItemTouchHelper.END), 0) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition
            recyclerView.adapter?.notifyItemMoved(startPosition, endPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
        itemLatLng = getLatLangFromAddress(item?.address + item?.city)
        googleMap.addMarker(MarkerOptions().position(itemLatLng!!))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(itemLatLng!!))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
    }

    fun getLatLangFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(requireContext(), Locale.getDefault())
        val address: List<Address>?
        return try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null || address.size < 1) {
                return LatLng(-10000.0, -10000.0)
            }
            val location: Address = address[0]
            LatLng(location.getLatitude(), location.getLongitude())

        } catch (e: IOException) {
            LatLng(-10000.0, -10000.0)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Map.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    mMap!!.isMyLocationEnabled = true
                    mMap!!.uiSettings.isMyLocationButtonEnabled = true
                } catch (e: SecurityException) {
                    Log.e("Exception: %s", Objects.requireNonNull(e.message).toString())
                }
            }
        }
    }

    companion object {
        const val ARG_ITEM_ID_BY_STRING = "item_id_by_string"

        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
