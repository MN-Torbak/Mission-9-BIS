package com.openclassrooms.firebaseREM

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bumptech.glide.Glide
import com.google.android.material.slider.RangeSlider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.model.PropertyRoom
import com.openclassrooms.firebaseREM.model.toProperty
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class ItemListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    private val mMainViewModel: MainViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var checkBoxSold: CheckBox
    private lateinit var checkBoxRecentlyPutOnTheMarket: CheckBox
    private lateinit var checkBoxCloseToSchool: CheckBox
    private lateinit var checkBoxCloseToShops: CheckBox
    private lateinit var checkBoxCloseToParc: CheckBox
    private lateinit var checkBoxNumbersOfPhotos: CheckBox
    private lateinit var checkBoxSurface: CheckBox
    private lateinit var checkBoxPrice: CheckBox
    private lateinit var numberOfMonths: Spinner
    private lateinit var numberOfPhotos: Spinner
    private lateinit var rangeSliderPrice: RangeSlider
    private lateinit var rangeSliderSurface: RangeSlider
    private lateinit var buttonCancel: Button
    private lateinit var buttonSubmit: Button
    private lateinit var adapter: SimpleItemRecyclerViewAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        setupAutoCompleteTextView(propertyListAddress)
        createNotificationChannel()
        enablePersistence()
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
            checkBoxCloseToParc = dialog.findViewById(R.id.checkBox_close_to_parc)
            checkBoxNumbersOfPhotos = dialog.findViewById(R.id.checkBox_photo)
            checkBoxSurface = dialog.findViewById(R.id.checkBox_surface)
            checkBoxPrice = dialog.findViewById(R.id.checkBox_price)
            numberOfMonths = dialog.findViewById(R.id.month_spinner_dialog)
            buttonCancel = dialog.findViewById(R.id.cancel)
            buttonCancel.setOnClickListener {
                dialog.dismiss()
            }
            buttonSubmit = dialog.findViewById(R.id.submit)
            buttonSubmit.setOnClickListener {
                showFilteredListWithRoom()
                dialog.dismiss()
            }
            val adapterForMonths = this.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.months_since_sold, android.R.layout.simple_spinner_item
                )
            }
            adapterForMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            numberOfMonths.adapter = adapterForMonths
            numberOfPhotos = dialog.findViewById(R.id.photo_spinner_dialog)
            val adapterForPhotos = this.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.add_numberOfRooms_property, android.R.layout.simple_spinner_item
                )
            }
            adapterForPhotos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            numberOfPhotos.adapter = adapterForPhotos
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

        mMainViewModel.propertys.observe(this) {
            if (it != null) {
                propertyList.addAll(it)
                for (Property in it) {
                    propertyListAddress.add(Property?.address.toString())
                }
                setupRecyclerView(recyclerView, propertyList)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private val propertyList: MutableList<Property?> = ArrayList()
    private val propertyListAddress: MutableList<String> = ArrayList()

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

        fun updateList(list: MutableList<Property?>) {
            adapterPropertyList = list
        }

        var selected = -1

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = adapterPropertyList[position]
            Glide.with(holder.avatarView.context)
                .load(item?.avatar1)
                .centerCrop()
                .into(holder.avatarView)
            holder.typeView.text = item?.type
            holder.cityView.text = item?.city
            holder.priceView.text = buildString {
                append(item?.price.toString())
                append(" €")
            }
            if (item?.saleDate == "") {
                holder.freeOrSaleView.text = buildString { append("Available") }
                holder.freeOrSaleView.setTextColor(Color.parseColor("#32CD32"))
            } else {
                holder.freeOrSaleView.text = buildString { append("Sold") }
                holder.freeOrSaleView.setTextColor(Color.parseColor("#ff2d49"))
            }
            holder.freeOrSaleView
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
                val itemProperty = v.tag as Property
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable(ItemDetailFragment.ARG_ITEM_ID, itemProperty)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, itemProperty)
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
            val freeOrSaleView: TextView = view.findViewById(R.id.free_or_sale_property)
        }
    }

    private fun createProperty() {
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

    private fun goToMapFragment() {
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

    private fun goToListFragment() {
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
        autoTextView.dropDownWidth = 1000
        autoTextView.setOnItemClickListener { _, _, _, _ ->
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
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("k", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun enablePersistence() {
        Firebase.database.setPersistenceEnabled(true)
    }

    private fun convertChoiceMonthIntoDate(month: Int): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val c = Calendar.getInstance()
        c.add(Calendar.MONTH, -month)
        val localDate = LocalDateTime.ofInstant(c.toInstant(), c.timeZone.toZoneId()).toLocalDate()
        return localDate.format(df)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showFilteredListWithRoom() {
        var queryString = String()
        val args = mutableListOf<String>()
        var containsCondition = false
        queryString += "SELECT * FROM PropertyRoom"
        if (checkBoxSold.isChecked) {
            queryString += " WHERE"
            queryString += " saleDate IS NOT NULL"
            queryString += " AND"
            queryString += " saleDate > "
            queryString += "\"" + convertChoiceMonthIntoDate(numberOfMonths.selectedItem.toString().toInt()) + "\""
            args.add(checkBoxSold.toString())
            containsCondition = true
        }
        if (checkBoxRecentlyPutOnTheMarket.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " createDate > "
            queryString += "\"" + dateNowSubtract31() + "\""
            args.add(checkBoxCloseToShops.toString())
        }
        if (checkBoxCloseToShops.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " closeToShops = 1"
            args.add(checkBoxCloseToShops.toString())
        }
        if (checkBoxCloseToSchool.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " closeToSchools = 1"
            args.add(checkBoxCloseToSchool.toString())
        }
        if (checkBoxCloseToParc.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " closeToParc = 1"
            args.add(checkBoxCloseToParc.toString())
        }
        if (checkBoxNumbersOfPhotos.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " numberOfPhotos >= "
            queryString += numberOfPhotos.selectedItem.toString()
            args.add(checkBoxNumbersOfPhotos.toString())
        }
        if (checkBoxSurface.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " surface >= "
            queryString += rangeSliderSurface.values.minOrNull()
            queryString += " AND"
            queryString += " surface <= "
            queryString += rangeSliderSurface.values.maxOrNull()
            args.add(checkBoxSurface.toString())
        }
        if (checkBoxPrice.isChecked) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " price >= "
            queryString += rangeSliderPrice.values.minOrNull()!!
            queryString += " AND"
            queryString += " price <= "
            queryString += rangeSliderPrice.values.maxOrNull()
            args.add(checkBoxPrice.toString())
        }
        val sqLiteQuery = SimpleSQLiteQuery(queryString)
        mMainViewModel.getFilter(sqLiteQuery)
        val currentAdapter = recyclerView.adapter as SimpleItemRecyclerViewAdapter
        mMainViewModel.filterList.observe(this) {
            currentAdapter.updateList(swapTypeOfList(it))
            currentAdapter.notifyDataSetChanged()
        }
    }

    private fun swapTypeOfList(primaryList: MutableList<PropertyRoom>): MutableList<Property?> {
        val propertys: MutableList<Property?> = ArrayList()
        for (propertyRoom in primaryList) {
            propertys.add(propertyRoom.toProperty())
        }
        return propertys
    }

    private fun dateNowSubtract31(): String? {
        val cal = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.DATE, -31)
        val dateBefore31Days = cal.time
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return formatter.format(dateBefore31Days)

    }
}

