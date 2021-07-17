package com.example.workshop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


class MainFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main
    private val TAG = "MainFragment"
    private val viewModel: DetailsViewModel by activityViewModels()
    private lateinit var workshopsRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var noWorkShopsTextView: TextView
    private val workshops = mutableListOf<Workshop>()

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // get all views
        workshopsRecyclerView = view.findViewById(R.id.recycleViewWorkshops)
        searchEditText = view.findViewById(R.id.editTextSearch)
        noWorkShopsTextView = view.findViewById(R.id.noWorkShopsTextView)

        // set workshop adapter
        var workshopAdapter = WorkshopAdapter(requireContext(), workshops)
        // set on workshop click
        workshopAdapter.onItemClick = { workshop, cardView ->
            // set the selected workshop on the view model to the clicked workshop
            viewModel.selectedWorkshop.value = workshop
            // set transition name to the card view
            ViewCompat.setTransitionName(cardView, "card")
            // replace the fragment on the fragment container
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("main")
                addSharedElement(cardView, "card")
                replace<DetailsFragment>(R.id.fragment_container_view)
            }
        }

        // set listener to changes on the search edit text
        RxTextView.textChanges(searchEditText)
            .debounce(300, TimeUnit.MILLISECONDS) // set debounce of 300 milliseconds
            .observeOn(AndroidSchedulers.mainThread()) // set where to handle subscribe when search edit text change.
            .subscribe(
                { r ->
                    // clear the workshops list
                    workshops.clear()
                    // add all the workshops from that contain the search term to the workshops list
                    for (workshop in viewModel.getWorkshops().value!!) {
                        if (workshop.name.toLowerCase().contains(r.toString().toLowerCase())) {
                            workshops.add(workshop)
                        }
                    }
                    // if there is not a workshop that contains the search term, show error
                    if (workshops.isEmpty()) {
                        noWorkShopsTextView.text = "Oops! we didn't found $r"
                        workshopsRecyclerView.visibility = View.GONE
                        searchEditText.visibility = View.GONE
                        noWorkShopsTextView.visibility = View.VISIBLE
                    } else {
                        noWorkShopsTextView.visibility = View.GONE
                        workshopsRecyclerView.visibility = View.VISIBLE
                        searchEditText.visibility = View.VISIBLE
                    }
                    // sort the results
                    workshops.sortBy { it.name }
                    // update data on the list
                    workshopAdapter.updateData(workshops)
                },
                { r -> Log.d(TAG, r.toString()) }
            )

        // observer to the workshops list on the view model
        viewModel.getWorkshops().observe(viewLifecycleOwner, { workshops ->
            // set the recycle view
            workshopsRecyclerView!!.apply {
                layoutManager = LinearLayoutManager(activity)
                //create new list to order the workshops
                val orderedWorkshops = mutableListOf<Workshop>()
                // add all workshops to the list
                orderedWorkshops.addAll(workshops)
                // sort the results
                orderedWorkshops.sortBy { it.name }
                // update data on the list
                workshopAdapter.updateData(orderedWorkshops)
                // set the adapter
                adapter = workshopAdapter
            }
        })

        return view
    }
}