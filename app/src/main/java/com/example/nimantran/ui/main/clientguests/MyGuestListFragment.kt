package com.example.nimantran.ui.main.clientguests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.nimantran.R
import com.example.nimantran.adapters.MyGuestListAdapter
import com.example.nimantran.databinding.FragmentMyGuestListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyGuestListFragment : Fragment() {
    private var _binding: FragmentMyGuestListBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val myGuestListViewModel: MyGuestListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyGuestListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myGuestListViewModel.getGuests(db) // fetch data only
        myGuestListViewModel.guests.observe(viewLifecycleOwner) { guests ->
            if (guests.isNotEmpty()) {
                binding.recyclerViewMyGuestList.adapter =
                    MyGuestListAdapter(requireActivity(), {
                        myGuestListViewModel.selectGuest(it)
                    })

                (binding.recyclerViewMyGuestList.adapter as MyGuestListAdapter).submitList(
                    guests
                )
            } else {
                binding.recyclerViewMyGuestList.visibility = View.GONE
            }
            if (binding.swipeRefreshLayoutMyGuestList.isRefreshing) {
                binding.swipeRefreshLayoutMyGuestList.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    companion object {
        const val COLL_GUESTS = "guests"
    }
}