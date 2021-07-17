package com.example.workshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel : ViewModel() {
    private val workshops = MutableLiveData<List<Workshop>>()
    val selectedWorkshop = MutableLiveData<Workshop>()

    fun getWorkshops(): LiveData<List<Workshop>> {
        return workshops
    }

    fun setWorkshops(workshops: List<Workshop>) {
        this.workshops.value = workshops
    }
}
