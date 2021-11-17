package com.example.mememeet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.graphics.BitmapFactory

import android.graphics.Bitmap




// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val memeList= mutableListOf<Meme>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_meme, container, false)
        val recyclerView: RecyclerView=view.findViewById(R.id.memeRecView)
        val searchText: EditText=view.findViewById(R.id.searchText)

        //Test Image and tags
        val image = BitmapFactory.decodeResource(requireContext().resources, R.drawable.irelia)
        val list= mutableListOf<String>()
        list.add("Irelia")
        list.add("League of Legends")
        memeList.add(Meme(image,list))

        val adapter=MemeAdapter(memeList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MemeFragment.
         */
        @JvmStatic
        fun newInstance() =
            MemeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}