package com.example.shoppinglistapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistapp.models.Product
import com.example.shoppinglistapp.data.Repository

class MainViewModel: ViewModel() {

    fun getData(): MutableLiveData<MutableList<Product>> {
        return Repository.getData()
    }

}