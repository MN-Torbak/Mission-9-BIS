package com.openclassrooms.firebaseREM

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.firebaseREM.ItemDetailFragment.Companion.ARG_ITEM_ID
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel


class ModifyPropertyFragment : Fragment() {

    var mMainViewModel: MainViewModel? = null
    private var item: Property? = null
    lateinit var type: TextView
    lateinit var price: TextView
    lateinit var avatar: TextView
    lateinit var description: TextView
    lateinit var surface: TextView
    lateinit var numberOfBathrooms: Spinner
    lateinit var numberOfBedrooms: Spinner
    lateinit var numberOfRooms: Spinner
    lateinit var city: TextView
    lateinit var address: TextView
    lateinit var shops: CheckBox
    lateinit var schools: CheckBox
    lateinit var parc: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        arguments?.let { bundle ->
            if (bundle.containsKey(ARG_ITEM_ID)) {
                item = bundle.getSerializable(ARG_ITEM_ID) as? Property
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_modify_property, container, false)

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
        numberOfRooms.setAdapter(adapterForRooms)
        numberOfBathrooms.setAdapter(adapterForRooms)
        numberOfBedrooms.setAdapter(adapterForRooms)

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

            if (item!!.closeToShops) {
                shops.isChecked = true
            }
            if (item!!.closeToSchools) {
                schools.isChecked = true
            }
            if (item!!.closeToParc) {
                parc.isChecked = true
            }
        }


        rootView.findViewById<Button>(R.id.save_modification).setOnClickListener {

            mMainViewModel?.updateType(item!!.id, type.text.toString())
            mMainViewModel?.updatePrice(item!!.id, price.text.toString().toInt())
            mMainViewModel?.updatePropertyAvatar(item!!.id, avatar.text.toString())
            mMainViewModel?.updateDescription(item!!.id, description.text.toString())
            mMainViewModel?.updateSurface(item!!.id, surface.text.toString().toInt())
            mMainViewModel?.updateCity(item!!.id, city.text.toString())
            mMainViewModel?.updateAddress(item!!.id, address.text.toString())
            mMainViewModel?.updateCloseToParcBoolean(item!!.id, if(parc.isChecked) { true } else { false })
            mMainViewModel?.updateCloseToSchoolsBoolean(item!!.id, if(schools.isChecked) { true } else { false })
            mMainViewModel?.updateCloseToShopsBoolean(item!!.id, if(shops.isChecked) { true } else { false })
            mMainViewModel?.updateNumberOfRooms(item!!.id, numberOfRooms.selectedItem.toString().toInt())
            mMainViewModel?.updateNumberOfBedRooms(item!!.id, numberOfBedrooms.selectedItem.toString().toInt())
            mMainViewModel?.updateNumberOfBathRooms(item!!.id, numberOfBathrooms.selectedItem.toString().toInt())
            val intent = Intent(context, ItemListActivity::class.java).apply {
            }
            context?.startActivity(intent)

        }
        return rootView
    }


}


