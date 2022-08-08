package com.openclassrooms.firebaseREM

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.slider.RangeSlider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class ItemListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false
    var mMainViewModel: MainViewModel? = null
    lateinit var recyclerView: RecyclerView
    lateinit var checkBoxSold: CheckBox
    lateinit var checkBoxRecentlyPutOnTheMarket: CheckBox
    lateinit var checkBoxCloseToSchool: CheckBox
    lateinit var checkBoxCloseToShops: CheckBox
    lateinit var checkBoxNumbersOfPhotos: CheckBox
    lateinit var checkBoxSurface: CheckBox
    lateinit var checkBoxPrice: CheckBox
    lateinit var numberOfMonths: Spinner
    lateinit var numberOfPhotos: Spinner
    lateinit var rangeSliderPrice: RangeSlider
    lateinit var rangeSliderSurface: RangeSlider
    lateinit var buttonCancel: Button
    lateinit var adapter: SimpleItemRecyclerViewAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        setupAutoCompleteTextView(propertyListAddress)
        createNotificationChannel()
        enablePersistence()
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recyclerView = findViewById(R.id.item_list)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter = SimpleItemRecyclerViewAdapter(this, listOf(), twoPane)
        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.add_property_button).setOnClickListener {
            createProperty()
        }

        findViewById<ImageButton>(R.id.go_to_map).setOnClickListener {
            goToMapFragment()
        }

        findViewById<ImageButton>(R.id.go_to_list).setOnClickListener {
            goToListFragment()
        }

        findViewById<ImageButton>(R.id.filter).setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_filter)
            checkBoxSold = dialog.findViewById(R.id.checkBox_sold)
            checkBoxRecentlyPutOnTheMarket = dialog.findViewById(R.id.checkBox_new)
            checkBoxCloseToSchool = dialog.findViewById(R.id.checkBox_close_to_school)
            checkBoxCloseToShops = dialog.findViewById(R.id.checkBox_close_to_shops)
            checkBoxNumbersOfPhotos = dialog.findViewById(R.id.checkBox_photo)
            checkBoxSurface = dialog.findViewById(R.id.checkBox_surface)
            checkBoxPrice = dialog.findViewById(R.id.checkBox_price)
            numberOfMonths = dialog.findViewById(R.id.month_spinner_dialog)
            buttonCancel = dialog.findViewById(R.id.cancel)
            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            val adapterForMonths = this.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.months_since_sold, android.R.layout.simple_spinner_item
                )
            }
            adapterForMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            numberOfMonths.setAdapter(adapterForMonths)
            numberOfPhotos = dialog.findViewById(R.id.photo_spinner_dialog)
            val adapterForPhotos = this.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.add_numberOfRooms_property, android.R.layout.simple_spinner_item
                )
            }
            adapterForPhotos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            numberOfPhotos.setAdapter(adapterForPhotos)
            rangeSliderPrice = dialog.findViewById(R.id.rangerSlider_price)
            rangeSliderPrice.setLabelFormatter { value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("USD")
                format.format(value.toDouble())
            }

            rangeSliderSurface = dialog.findViewById(R.id.rangerSlider_surface)
            rangeSliderSurface.setLabelFormatter { value: Float ->
                "$value m²"
            }
            dialog.show()


        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            twoPane = true
            findViewById<ImageButton>(R.id.go_to_list).visibility = View.GONE
        }

        mMainViewModel?.Propertys?.observe(this) {
            if (it != null) {
                propertyList.addAll(it)
                propertyList.addAll(it)
                for (Property in it) {
                    propertyListAddress.add(Property?.address.toString())
                }
                setupRecyclerView(recyclerView, propertyList)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    val propertyList: MutableList<Property?> = ArrayList()
    val propertyListAddress: MutableList<String> = ArrayList()

    private fun setupRecyclerView(recyclerView: RecyclerView, list: MutableList<Property?>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, list, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private var adapterPropertyList: List<Property?>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        fun updateList (list : MutableList<Property?>) {
          adapterPropertyList = list
        }

        var selected = -1

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = adapterPropertyList[position]
            Glide.with(holder.avatarView.getContext())
                .load(item?.avatar1)
                .centerCrop()
                .into(holder.avatarView)
            holder.typeView.text = item?.type
            holder.cityView.text = item?.city
            holder.priceView.text = item?.price.toString() + " €"
            holder.itemView.setBackgroundColor(
                if (position == selected) {
                    Color.parseColor("#C6E5F3")
                } else {
                    Color.parseColor("#FFFFFF")
                }
            )
            holder.itemView.setOnClickListener { v ->
                notifyItemChanged(selected)
                selected = position
                notifyItemChanged(selected)
                val item = v.tag as Property
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable(ItemDetailFragment.ARG_ITEM_ID, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item)
                    }
                    v.context.startActivity(intent)
                }
            }


            with(holder.itemView) {
                tag = item
                //holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }

        override fun getItemCount() = adapterPropertyList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val avatarView: ImageView = view.findViewById(R.id.avatar_property)
            val typeView: TextView = view.findViewById(R.id.type_property)
            val cityView: TextView = view.findViewById(R.id.city_property)
            val priceView: TextView = view.findViewById(R.id.price_property)
        }
    }

    fun createProperty() {
        val fragment = AddPropertyFragment().apply {
            arguments = Bundle().apply {
            }
        }

        if (twoPane) {
            supportFragmentManager.beginTransaction().replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
                .addToBackStack(this.toString())
                .commit()
        }
    }

    fun goToMapFragment() {
        val fragment = Map().apply {
            arguments = Bundle().apply {
            }
        }

        if (twoPane) {
            supportFragmentManager.beginTransaction().replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
                .addToBackStack(this.toString())
                .commit()
        }
    }

    fun goToListFragment() {
        val intent = Intent(this, ItemListActivity::class.java)
        startActivity(intent)
    }

    private fun setupAutoCompleteTextView(listOfAddress: MutableList<String>) {
        val autoTextView: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_item, listOfAddress
        )
        autoTextView.threshold = 1
        autoTextView.setAdapter(adapter)
        autoTextView.setDropDownBackgroundResource(R.color.colorWhite)
        autoTextView.setDropDownWidth(1000)
        autoTextView.setOnItemClickListener { parent, view, position, id ->
            val item = autoTextView.text
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID_BY_STRING, item.toString())
                    }
                }
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(this, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID_BY_STRING, item.toString())
                }
                startActivity(intent)
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("k", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun enablePersistence() {
        // [START rtdb_enable_persistence]
        Firebase.database.setPersistenceEnabled(true)
        // [END rtdb_enable_persistence]
    }

    fun showFilteredList(primaryList: MutableList<Property?>){
        val filteredList: MutableList<Property?> = ArrayList()
        for (property in primaryList) {
            if ((checkBoxSold.isChecked && property.saleDate <= numberOfMonths.selectedItem) &&
                (checkBoxRecentlyPutOnTheMarket.isChecked && property.createDate <= 31) &&
                (checkBoxCloseToSchool.isChecked && property.nearbyPointsOfInterest.contains("School")) &&
                (checkBoxCloseToShops.isChecked && property.nearbyPointsOfInterest.contains("Shops") ) &&
                (checkBoxNumbersOfPhotos.isChecked && /*TODO: faire une methode pour savoir combien d'Element possède une Property*/ 2 >= numberOfPhotos.selectedItem.toString().toInt()) &&
                (checkBoxSurface.isChecked && ((property?.surface!! >= rangeSliderSurface.values.minOrNull()!!) && (property.surface <= rangeSliderSurface.values.maxOrNull()!!))) &&
                (checkBoxPrice.isChecked && ((property.price >= rangeSliderPrice.values.minOrNull()!!) && (property.price <= rangeSliderPrice.values.maxOrNull()!!)))
                    ) {
                filteredList.add(property)
            }
        }
        adapter.updateList(filteredList)
        adapter.notifyDataSetChanged()
    }

}
