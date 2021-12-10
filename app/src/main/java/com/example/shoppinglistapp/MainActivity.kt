package com.example.shoppinglistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.adapters.ProductAdapter
import com.example.shoppinglistapp.models.Product
import com.example.shoppinglistapp.data.Repository
import com.example.shoppinglistapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
    lateinit var adapter: ProductAdapter
    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI(it)
        })


        binding.content.addButton.setOnClickListener {
            val product = Product(
                name = binding.content.editName.text.toString(),
                amount = binding.content.editAmount.text.toString().toInt())
            Repository.addProduct(product)
            adapter.notifyDataSetChanged();
        }

        binding.content.deleteAll.setOnClickListener {
            Repository.deleteAllProducts()
            adapter.notifyDataSetChanged();
        }

        binding.content.sortNameBtn.setOnClickListener {
            Repository.products.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }

        binding.content.sortAmountBtn.setOnClickListener {
            Repository.products.sortByDescending { it.amount }
            adapter.notifyDataSetChanged()
        }
    }



    fun updateUI(products : MutableList<Product>) {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

        binding.content.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

       adapter = ProductAdapter(products)

      /*connecting the recyclerview to the adapter  */
        binding.content.recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_about -> {
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_delete -> {
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_help -> {
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_refresh -> {
                Toast.makeText(this, "Refresh item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
        }

        return false //we did not handle the event

    }

}