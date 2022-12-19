package com.openclassrooms.firebaseREM

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel


class ModifyPropertyFragment : Fragment() {

    private var mMainViewModel: MainViewModel? = null
    private var item: Property? = null
    lateinit var type: TextView
    lateinit var price: TextView
    private lateinit var avatar: TextView
    lateinit var description: TextView
    lateinit var surface: TextView
    lateinit var numberOfBathrooms: Spinner
    lateinit var numberOfBedrooms: Spinner
    lateinit var numberOfRooms: Spinner
    lateinit var city: TextView
    lateinit var address: TextView
    private lateinit var shops: CheckBox
    private lateinit var schools: CheckBox
    private lateinit var parc: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_ITEM_ID)) {
                @Suppress("DEPRECATION")
                item = bundle.getSerializable(ARG_ITEM_ID) as? Property
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_modify_property, container, false)
        activity?.findViewById<ImageButton>(R.id.delete_element)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.add_element)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.modify_property)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.sold_property)?.visibility = View.GONE

        type = rootView.findViewById(R.id.add_type_property)
        price = rootView.findViewById(R.id.add_price_property)
        avatar = rootView.findViewById(R.id.add_avatar_property)
        description = rootView.findViewById(R.id.add_description_property)
        surface = rootView.findViewById(R.id.add_surface_property)
        numberOfBathrooms = rootView.findViewById(R.id.add_numberOfBathrooms_property)
        numberOfBedrooms = rootView.findViewById(R.id.add_numberOfBedrooms_property)
        city = rootView.findViewById(R.id.add_city_property)
        address = rootView.findViewById(R.id.add_address_property)
        numberOfRooms = rootView.findViewById(R.id.add_numberOfRooms_property)
        shops = rootView.findViewById(R.id.checkBox_shops)
        schools = rootView.findViewById(R.id.checkBox_schools)
        parc = rootView.findViewById(R.id.checkBox_parc)
        val adapterForRooms = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.add_numberOfRooms_property, android.R.layout.simple_spinner_item
            )
        }
        adapterForRooms!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        numberOfRooms.adapter = adapterForRooms
        numberOfBathrooms.adapter = adapterForRooms
        numberOfBedrooms.adapter = adapterForRooms

        item?.let {
            type.text = item!!.type
            price.text = item!!.price.toString()
            avatar.text = item!!.avatar1
            description.text = item!!.description
            surface.text = item!!.surface.toString()
            city.text = item!!.city
            numberOfBathrooms.adapter = adapterForRooms.apply { item!!.numberOfBathrooms }
            numberOfBathrooms.setSelection(item!!.numberOfBathrooms - 1)
            numberOfBedrooms.adapter = adapterForRooms.apply { item!!.numberOfBedrooms }
            numberOfBedrooms.setSelection(item!!.numberOfBedrooms - 1)
            numberOfRooms.adapter = adapterForRooms.apply { item!!.numberOfRooms }
            numberOfRooms.setSelection(item!!.numberOfRooms - 1)
            address.text = item!!.address
            adapterForRooms.notifyDataSetChanged()
            if (item!!.closeToShops == 1) { shops.isChecked = true }
            if (item!!.closeToSchools == 1) { schools.isChecked = true }
            if (item!!.closeToParc == 1) { parc.isChecked = true }
        }

        rootView.findViewById<Button>(R.id.save_modification).setOnClickListener {
            item!!.id.let { it1 -> mMainViewModel?.updateType(it1, type.text.toString()) }
            item!!.id.let { it1 -> mMainViewModel?.updatePrice(it1, price.text.toString().toInt()) }
            item!!.id.let { it1 -> mMainViewModel?.updatePropertyAvatar(it1, avatar.text.toString()) }
            item!!.id.let { it1 -> mMainViewModel?.updateDescription(it1, description.text.toString()) }
            item!!.id.let { it1 -> mMainViewModel?.updateSurface(it1, surface.text.toString().toInt()) }
            item!!.id.let { it1 -> mMainViewModel?.updateCity(it1, city.text.toString()) }
            item!!.id.let { it1 -> mMainViewModel?.updateAddress(it1, address.text.toString()) }
            item!!.id.let { it1 -> mMainViewModel?.updateCloseToParc(it1, if(parc.isChecked) {1} else {0}) }
            item!!.id.let { it1 -> mMainViewModel?.updateCloseToSchools(it1, if(schools.isChecked) {1} else {0}) }
            item!!.id.let { it1 -> mMainViewModel?.updateCloseToShops(it1, if(shops.isChecked) {1} else {0}) }
            item!!.id.let { it1 -> mMainViewModel?.updateNumberOfRooms(it1, numberOfRooms.selectedItem.toString().toInt()) }
            item!!.id.let { it1 -> mMainViewModel?.updateNumberOfBedRooms(it1, numberOfBedrooms.selectedItem.toString().toInt()) }
            item!!.id.let { it1 -> mMainViewModel?.updateNumberOfBathRooms(it1, numberOfBathrooms.selectedItem.toString().toInt()) }
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ItemDetailFragment.ARG_ITEM_ID_BY_STRING, item?.address)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.item_detail_container, fragment).addToBackStack(this.toString())
                .commit()

        }
        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}


