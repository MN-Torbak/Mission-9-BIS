package com.openclassrooms.firebaseREM

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.openclassrooms.firebaseREM.notifications.NotificationsWorker
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel


class AddPropertyFragment : Fragment() {

    private var mMainViewModel: MainViewModel? = null
    lateinit var type: TextView
    private lateinit var agentWhoAdd: TextView
    lateinit var price: TextView
    private lateinit var avatar: TextView
    lateinit var description: TextView
    lateinit var surface: TextView
    lateinit var numberOfBathrooms: Spinner
    lateinit var numberOfBedrooms: Spinner
    lateinit var numberOfRooms: Spinner
    lateinit var city: TextView
    lateinit var address: TextView
    private lateinit var createDate: DatePicker
    private lateinit var shops: CheckBox
    private lateinit var schools: CheckBox
    private lateinit var parc: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_property, container, false)

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
        createDate = rootView.findViewById(R.id.datePickerCreateDate)
        shops = rootView.findViewById(R.id.checkBox_shops)
        schools = rootView.findViewById(R.id.checkBox_schools)
        parc = rootView.findViewById(R.id.checkBox_parc)
        agentWhoAdd = rootView.findViewById(R.id.add_name_of_agent)
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

        rootView.findViewById<Button>(R.id.create).setOnClickListener {

            if (type.text.toString() != "" && price.text.toString() != "" && description.text.toString() != "" && surface.text.toString() != "" && city.text.toString() != ""
            ) {
                mMainViewModel?.createProperty(
                    type.text.toString(),
                    Integer.parseInt(price.text.toString()),
                    avatar.text.toString(),
                    description.text.toString(),
                    Integer.parseInt(surface.text.toString()),
                    Integer.parseInt(numberOfRooms.selectedItem.toString()),
                    Integer.parseInt(numberOfBathrooms.selectedItem.toString()),
                    Integer.parseInt(numberOfBedrooms.selectedItem.toString()),
                    city.text.toString(),
                    address.text.toString(),
                    "" + createDate.year + "/" + (createDate.month +1) + "/" + createDate.dayOfMonth,
                    "",
                    if (shops.isChecked) {1} else {0},
                    if (schools.isChecked) {1} else {0},
                    if (parc.isChecked) {1} else {0},
                    agentWhoAdd.text.toString(),
                    "",
                    0
                    )
                val intent = Intent(context, ItemListActivity::class.java).apply {
                }
                createNotification()
                context?.startActivity(intent)
            } else {
                val text = "Please complete all the required fields"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }
        return rootView
    }

    private fun createNotification() {
        val oneTimeWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(NotificationsWorker::class.java)
                .addTag("Notify")
                .build()
        @Suppress("DEPRECATION")
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
    }

}