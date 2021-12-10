package com.example.shoppinglistapp.models

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize


//The exclude thing on product is because we want to have an id number in the product
//class, but do not want to save it to firebase as firebase will automatically
//generate the IDs (in this application, maybe in other applications you do it
//differently.
@Parcelize

data class Product(var name:String = "",  var amount: Int = 0, @get:Exclude var id: String = "") : Parcelable
