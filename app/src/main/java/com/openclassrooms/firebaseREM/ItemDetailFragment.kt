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
import androidx.fragment.app.FragmentTransaction
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
import com.openclassrooms.firebaseREM.model.Element
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class ItemDetailFragment : Fragment(), OnMapReadyCallback {

    private var twoPane: Boolean = false
    private var item: Property? = null
    private var mRecyclerView: RecyclerView? = null
    private var mMainViewModel: MainViewModel? = null
    private var mMap: GoogleMap? = null
    private var itemLatLng: LatLng? = null
    private lateinit var closeToCommodity: String
    private lateinit var itemDetailAdapter: ItemDetailAdapter
    private lateinit var buttonCancel: Button
    private lateinit var buttonSubmit: Button
    private lateinit var datePickerSaleDate: DatePicker
    private lateinit var nameOfSalesman: TextView

    override fun onResume() {
        super.onResume()
        mMainViewModel?.refreshPropertysCollection()
        mMainViewModel?.refreshElementsCollection()
        mMainViewModel?.propertys?.observe(this) { propertys ->
            if (propertys != null) {
                arguments?.let { bundle ->
                    if (bundle.containsKey(ARG_ITEM_ID)) {
                        item = bundle.getSerializable(ARG_ITEM_ID) as? Property
                        activity?.findViewById<TextView>(R.id.appBarDetailTextView)?.text = item?.type
                        displayPropertyInformations(item)
                    } else if (bundle.containsKey(ARG_ITEM_ID_BY_STRING)) {
                        val arguments = bundle.getString(ARG_ITEM_ID_BY_STRING).toString()
                        for (property in propertys) {
                            if (property?.address.toString() == arguments) {
                                item = property
                                displayPropertyInformations(item)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    fun displayPropertyInformations(item: Property?) {
        activity?.findViewById<TextView>(R.id.appBarDetailTextView)?.text = item?.type
        activity?.findViewById<TextView>(R.id.detail_description)?.text = item?.description
        activity?.findViewById<TextView>(R.id.detail_surface)?.text =
            buildString {
                append(item?.surface.toString())
                append(getString(R.string.squareMeter))
            }
        activity?.findViewById<TextView>(R.id.detail_numberofrooms)?.text = item?.numberOfRooms.toString()
        activity?.findViewById<TextView>(R.id.detail_numberofbathrooms)?.text = item?.numberOfBathrooms.toString()
        activity?.findViewById<TextView>(R.id.detail_numberofbedrooms)?.text = item?.numberOfBedrooms.toString()
        activity?.findViewById<TextView>(R.id.detail_address)?.text = item?.address
        activity?.findViewById<TextView>(R.id.detail_city)?.text = item?.city
        if (item?.closeToShops == 1 && item.closeToSchools == 0 && item.closeToParc == 0) { closeToCommodity = "Shops"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else if (item?.closeToShops == 0 && item.closeToSchools == 1 && item.closeToParc == 0) { closeToCommodity = "Schools"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else if (item?.closeToShops == 0 && item.closeToSchools == 0 && item.closeToParc == 1) { closeToCommodity = "Parc"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else if (item?.closeToShops == 1 && item.closeToSchools == 1 && item.closeToParc == 0) { closeToCommodity = "Shops and Schools"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else if (item?.closeToShops == 1 && item.closeToSchools == 0 && item.closeToParc == 1) { closeToCommodity = "Shops and Parc"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity}
        else if (item?.closeToShops == 0 && item.closeToSchools == 1 && item.closeToParc == 1) { closeToCommodity = "Schools and Parc"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else if (item?.closeToShops == 1 && item.closeToSchools == 1 && item.closeToParc == 1) { closeToCommodity = "Shops, Schools and Parc"
            activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.text = closeToCommodity }
        else { activity?.findViewById<TextView>(R.id.detail_close_to_commodity)?.visibility = View.GONE }
        activity?.findViewById<TextView>(R.id.detail_address)?.text = item?.address
        activity?.findViewById<TextView>(R.id.detail_city)?.text = item?.city
        activity?.findViewById<TextView>(R.id.detail_add_by_title)?.text = getString(R.string.AddedBy)
        activity?.findViewById<TextView>(R.id.detail_name_of_agent)?.text =
            buildString {
                append(item?.agentWhoAdd)
                append(", ")
            }
        activity?.findViewById<TextView>(R.id.detail_create_date)?.text = item?.createDate
        if (item?.saleDate == "") {
            activity?.findViewById<TextView>(R.id.detail_sold_by_title)!!.visibility = View.GONE
            activity?.findViewById<TextView>(R.id.detail_name_of_agent_who_sold)!!.visibility = View.GONE
            activity?.findViewById<TextView>(R.id.detail_sale_date)!!.visibility = View.GONE
        } else {
            activity?.findViewById<TextView>(R.id.detail_sold_by_title)?.text = getString(R.string.SoldBy)
            activity?.findViewById<TextView>(R.id.detail_name_of_agent_who_sold)?.text =
                buildString {
                    append(item?.agentWhoSells)
                    append(", ")
                }
            activity?.findViewById<TextView>(R.id.detail_sale_date)?.text = item?.saleDate
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    @SuppressLint("CutPasteId", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)
        activity?.findViewById<TextView>(R.id.appBarDetailTextView)?.text = item?.type
        mRecyclerView = rootView.findViewById(R.id.fragmentItemDetailRecyclerView)
        if (rootView.findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
        }
        activity?.findViewById<ImageButton>(R.id.add_element)?.visibility = View.VISIBLE
        activity?.findViewById<ImageButton>(R.id.add_element)?.setOnClickListener {
            addElement()
        }
        //TODO: faire les 3 autres boutons comme add element tablet
        rootView?.findViewById<ImageButton>(R.id.add_element_tablet)?.setOnClickListener {
            addElement()
        }
        activity?.findViewById<ImageButton>(R.id.delete_element)?.visibility = View.VISIBLE
        activity?.findViewById<ImageButton>(R.id.delete_element)?.setOnClickListener {
            deleteElement(item?.let { createPhotoListElement(it, itemDetailAdapter.mElement) })
        }
        activity?.findViewById<ImageButton>(R.id.modify_property)?.visibility = View.VISIBLE
        activity?.findViewById<ImageButton>(R.id.modify_property)?.setOnClickListener {
            modifyProperty()
        }
        activity?.findViewById<ImageButton>(R.id.sold_property)?.visibility = View.VISIBLE
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
                    item?.id?.let { it1 ->
                        mMainViewModel?.updateAgentWhoSells(
                            it1,
                            nameOfSalesman.text.toString()
                        )
                    }
                    item?.id?.let { it1 ->
                        mMainViewModel?.updateSaleDate(
                            it1,
                            "" + datePickerSaleDate.dayOfMonth + "/" + (datePickerSaleDate.month + 1) + "/" + datePickerSaleDate.year
                        )
                    }
                    dialog.dismiss()
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).text =
                        getString(R.string.SoldBy)
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                        buildString {
                            append(nameOfSalesman.text.toString())
                            append(", ")
                        }
                    rootView.findViewById<TextView>(R.id.detail_sale_date).text =
                        buildString {
                            append("")
                            append(datePickerSaleDate.dayOfMonth)
                            append("/")
                            append((datePickerSaleDate.month + 1))
                            append("/")
                            append(datePickerSaleDate.year)
                        }
                    val currentFragment =
                        requireFragmentManager().findFragmentByTag("YourFragmentTag")
                    val fragmentTransaction: FragmentTransaction =
                        requireFragmentManager().beginTransaction()
                    if (currentFragment != null) {
                        fragmentTransaction.detach(currentFragment)
                        fragmentTransaction.attach(currentFragment)
                    }
                    fragmentTransaction.commit()
                }
                dialog.show()
            }
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.detail_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        observeElements()
        item.let { currentItem ->
            rootView.findViewById<TextView>(R.id.detail_title).text = getString(R.string.Media)
            rootView.findViewById<TextView>(R.id.detail_description_title).text =
                getString(R.string.Description)
            rootView.findViewById<TextView>(R.id.detail_surface_title).text =
                getString(R.string.Surface)
            rootView.findViewById<TextView>(R.id.detail_numberofrooms_title).text =
                getString(R.string.NumberOfRooms)
            rootView.findViewById<TextView>(R.id.detail_numberofbathrooms_title).text =
                getString(R.string.NumberOfBathrooms)
            rootView.findViewById<TextView>(R.id.detail_numberofbedrooms_title).text =
                getString(R.string.NumberOfBedrooms)
            rootView.findViewById<TextView>(R.id.detail_close_to_title).text =
                getString(R.string.CloseTo)
            rootView.findViewById<TextView>(R.id.detail_address_title).text =
                getString(R.string.Location)
            if (currentItem != null) {
                rootView.findViewById<TextView>(R.id.detail_description).text =
                    currentItem.description
                rootView.findViewById<TextView>(R.id.detail_surface).text =
                    buildString {
                        append(currentItem.surface.toString())
                        append(getString(R.string.squareMeter))
                    }
                rootView.findViewById<TextView>(R.id.detail_numberofrooms).text =
                    currentItem.numberOfRooms.toString()
                rootView.findViewById<TextView>(R.id.detail_numberofbathrooms).text =
                    currentItem.numberOfBathrooms.toString()
                rootView.findViewById<TextView>(R.id.detail_numberofbedrooms).text =
                    currentItem.numberOfBedrooms.toString()
                if (currentItem.closeToShops == 1 && currentItem.closeToSchools == 0 && currentItem.closeToParc == 0) {
                    closeToCommodity = "Shops"
                } else if (currentItem.closeToShops == 0 && currentItem.closeToSchools == 1 && currentItem.closeToParc == 0) {
                    closeToCommodity = "Schools"
                } else if (currentItem.closeToShops == 0 && currentItem.closeToSchools == 0 && currentItem.closeToParc == 1) {
                    closeToCommodity = "Parc"
                } else if (currentItem.closeToShops == 1 && currentItem.closeToSchools == 1 && currentItem.closeToParc == 0) {
                    closeToCommodity = "Shops and Schools"
                } else if (currentItem.closeToShops == 1 && currentItem.closeToSchools == 0 && currentItem.closeToParc == 1) {
                    closeToCommodity = "Shops and Parc"
                } else if (currentItem.closeToShops == 0 && currentItem.closeToSchools == 1 && currentItem.closeToParc == 1) {
                    closeToCommodity = "Schools and Parc"
                } else if (currentItem.closeToShops == 1 && currentItem.closeToSchools == 1 && currentItem.closeToParc == 1) {
                    closeToCommodity = "Shops, Schools and Parc"
                } else {
                    rootView.findViewById<TextView>(R.id.detail_close_to_commodity).visibility =
                        View.GONE
                }
                rootView.findViewById<TextView>(R.id.detail_close_to_commodity).text =
                    closeToCommodity
                rootView.findViewById<TextView>(R.id.detail_address).text = currentItem.address
                rootView.findViewById<TextView>(R.id.detail_city).text = currentItem.city
                rootView.findViewById<TextView>(R.id.detail_add_by_title).text =
                    getString(R.string.AddedBy)
                rootView.findViewById<TextView>(R.id.detail_name_of_agent).text =
                    buildString {
                        append(currentItem.agentWhoAdd)
                        append(", ")
                    }
                rootView.findViewById<TextView>(R.id.detail_create_date).text =
                    currentItem.createDate
                if (currentItem.saleDate == "") {
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).visibility =
                        View.GONE
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).visibility =
                        View.GONE
                    rootView.findViewById<TextView>(R.id.detail_sale_date).visibility = View.GONE
                } else {
                    rootView.findViewById<TextView>(R.id.detail_sold_by_title).text =
                        getString(R.string.SoldBy)
                    rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                        buildString {
                            append(currentItem.agentWhoSells)
                            append(", ")
                        }
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
                            item?.id?.let { it1 ->
                                mMainViewModel?.updateAgentWhoSells(
                                    it1,
                                    nameOfSalesman.text.toString()
                                )
                            }
                            item?.id?.let { it1 ->
                                mMainViewModel?.updateSaleDate(
                                    it1,
                                    "" + datePickerSaleDate.year + "/" + (datePickerSaleDate.month + 1) + "/" + datePickerSaleDate.dayOfMonth
                                )
                            }
                            dialog.dismiss()
                            rootView.findViewById<TextView>(R.id.detail_sold_by_title).text =
                                getString(R.string.SoldBy)
                            rootView.findViewById<TextView>(R.id.detail_name_of_agent_who_sold).text =
                                buildString {
                                    append(nameOfSalesman.text.toString())
                                    append(", ")
                                }
                            rootView.findViewById<TextView>(R.id.detail_sale_date).text =
                                buildString {
                                    append("")
                                    append(datePickerSaleDate.year)
                                    append("/")
                                    append((datePickerSaleDate.month + 1))
                                    append("/")
                                    append(datePickerSaleDate.dayOfMonth)
                                }
                            val currentFragment =
                                requireFragmentManager().findFragmentByTag("YourFragmentTag")
                            val fragmentTransaction: FragmentTransaction =
                                requireFragmentManager().beginTransaction()
                            if (currentFragment != null) {
                                fragmentTransaction.detach(currentFragment)
                                fragmentTransaction.attach(currentFragment)
                            }
                            fragmentTransaction.commit()
                        }
                        dialog.show()
                    }
                }
                rootView.findViewById<ImageButton>(R.id.add_element)?.setOnClickListener {
                    addElement()
                }

                rootView.findViewById<ImageButton>(R.id.delete_element)?.setOnClickListener {
                    deleteElement(item?.let {
                        createPhotoListElement(
                            it,
                            itemDetailAdapter.mElement
                        )
                    })
                }
            }
        }

        return rootView
    }

    private fun observeElements() {
        mMainViewModel?.elements?.observe(viewLifecycleOwner) { testElement ->
            if (testElement != null) {
                displayPhotoProperty(item?.let { createPhotoListElement(it, testElement) })
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayPhotoProperty(photoElementList: ArrayList<Element>?) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        mRecyclerView?.layoutManager = layoutManager
        itemDetailAdapter = ItemDetailAdapter(photoElementList)
        mRecyclerView?.adapter = itemDetailAdapter
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
        itemDetailAdapter.notifyDataSetChanged()
    }

    private fun createPhotoListElement(
        property: Property,
        ElementList: List<Element?>?
    ): ArrayList<Element> {
        val mPhotoElement = ArrayList<Element>()
        if (ElementList != null) {
            for (element in ElementList) {
                if (element?.propertyId == property.id) {
                    mPhotoElement.add(element)
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

    private fun deleteElement(elementList: ArrayList<Element>?) {
        var intChangeNumberOfPhotos = 0
        if (elementList != null) {
            for (element in elementList) {
                if (element.isSelected) {
                    mMainViewModel?.deleteElement(element.elementId, onElementDeleted)
                    intChangeNumberOfPhotos += 1
                }
            }
        }
        val updatingNumberOfPhotos = item!!.numberOfPhotos.minus(intChangeNumberOfPhotos)
        item?.id?.let { mMainViewModel?.updateNumberOfPhotos(it, (updatingNumberOfPhotos)) }
        item?.numberOfPhotos = updatingNumberOfPhotos
    }

    private var onElementDeleted: () -> Unit = {
        mMainViewModel?.refreshElementsCollection()
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

    private fun getLatLangFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(requireContext(), Locale.getDefault())
        val address: List<Address>?
        return try {
            address = strAddress!!.let { coder.getFromLocationName(it, 5) }
            if ((address == null) || address.isEmpty()) {
                return LatLng(-10000.0, -10000.0)
            }
            val location: Address = address[0]
            LatLng(location.latitude, location.longitude)

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
            if (grantResults.isNotEmpty()
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
