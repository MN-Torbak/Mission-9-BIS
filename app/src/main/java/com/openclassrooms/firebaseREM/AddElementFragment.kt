package com.openclassrooms.firebaseREM

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.firebaseREM.model.Property
import com.openclassrooms.firebaseREM.viewmodel.MainViewModel

class AddElementFragment : Fragment() {

    private var mMainViewModel: MainViewModel? = null
    private var item: Property? = null
    private lateinit var photoUrl1: TextView
    private lateinit var photoUrl2: TextView
    private lateinit var photoUrl3: TextView
    private lateinit var photoUrl4: TextView
    private lateinit var photoUrl5: TextView
    private lateinit var photoUrl6: TextView
    private lateinit var typeOfElement1: TextView
    private lateinit var typeOfElement2: TextView
    private lateinit var typeOfElement3: TextView
    private lateinit var typeOfElement4: TextView
    private lateinit var typeOfElement5: TextView
    private lateinit var typeOfElement6: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        arguments?.let { bundle ->
            if (bundle.containsKey(ItemDetailFragment.ARG_ITEM_ID)) {
                @Suppress("DEPRECATION")
                item = bundle.getSerializable(ItemDetailFragment.ARG_ITEM_ID) as? Property
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_element, container, false)
        activity?.findViewById<ImageButton>(R.id.delete_element)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.add_element)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.modify_property)?.visibility = View.GONE
        activity?.findViewById<ImageButton>(R.id.sold_property)?.visibility = View.GONE
        photoUrl1 = rootView.findViewById(R.id.add_photo1_element)
        photoUrl2 = rootView.findViewById(R.id.add_photo2_element)
        photoUrl3 = rootView.findViewById(R.id.add_photo3_element)
        photoUrl4 = rootView.findViewById(R.id.add_photo4_element)
        photoUrl5 = rootView.findViewById(R.id.add_photo5_element)
        photoUrl6 = rootView.findViewById(R.id.add_photo6_element)
        typeOfElement1 = rootView.findViewById(R.id.type_of_element_1)
        typeOfElement2 = rootView.findViewById(R.id.type_of_element_2)
        typeOfElement3 = rootView.findViewById(R.id.type_of_element_3)
        typeOfElement4 = rootView.findViewById(R.id.type_of_element_4)
        typeOfElement5 = rootView.findViewById(R.id.type_of_element_5)
        typeOfElement6 = rootView.findViewById(R.id.type_of_element_6)

        rootView.findViewById<Button>(R.id.create_elements).setOnClickListener {
            var intChangeNumberOfPhotos = 0
            if (photoUrl1.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl1.text.toString(),
                        it1, false, typeOfElement1.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }
            if (photoUrl2.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl2.text.toString(),
                        it1, false, typeOfElement2.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }
            if (photoUrl3.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl3.text.toString(),
                        it1, false, typeOfElement3.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }
            if (photoUrl4.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl4.text.toString(),
                        it1, false, typeOfElement4.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }
            if (photoUrl5.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl5.text.toString(),
                        it1, false, typeOfElement5.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }
            if (photoUrl6.text.toString() != "") {
                item?.id?.let { it1 ->
                    mMainViewModel?.createElement(
                        "", photoUrl6.text.toString(),
                        it1, false, typeOfElement6.text.toString()
                    )
                }
                intChangeNumberOfPhotos += 1
            }

            item?.id?.let { it1 ->
                mMainViewModel?.updateNumberOfPhotos(
                    it1,
                    (item!!.numberOfPhotos.plus(intChangeNumberOfPhotos))
                )
            }
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ItemDetailFragment.ARG_ITEM_ID, item)
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