package com.example.shoppinglistapp.data

import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglistapp.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object Repository {
    var products = mutableListOf<Product>()

    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()

    private lateinit var adapter: ArrayAdapter<Product>

    private lateinit var db: FirebaseFirestore


    fun getData(): MutableLiveData<MutableList<Product>> {
        db = Firebase.firestore
        val docRef = db.collection("products")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {  //any errors
                Log.d("Repository", "Listen failed.", e)
                return@addSnapshotListener
            }
            products.clear() //to avoid duplicates.
            for (document in snapshot?.documents!!) { //add all products to the list
                Log.d("Repository_snapshotlist", "${document.id} => ${document.data}")
                val product = document.toObject<Product>()!!
                product.id = document.id
                products.add(product)
            }

            productListener.value = products
        }
        return productListener
    }


    // Create
    fun addProduct(product : Product){
        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w("Error", "Error adding document", e)
            } //end failure listener
    } //end method


    // Delete
    fun deleteProduct(index : Int) {
        val product = products[index]
        db.collection("products").document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot","DocumentSnapshot with id: ${product.id} successfully deleted!")
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
        products.removeAt(index) //removes it from the list
        productListener.value = products
    }


    fun deleteAllProducts(){
        val batch = db.batch()
        for (product in products) {
            val ref = db.collection("products").document(product.id)
            batch.delete(ref)
        }

        // Commit the batch
        batch.commit().addOnCompleteListener {
            products.clear()
            adapter.notifyDataSetChanged()
        }
    }
}