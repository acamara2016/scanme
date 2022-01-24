package com.acamara.scanme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.acamara.scanme.api.InitialSearchAPI
import com.acamara.scanme.api.FindLocalProductAPI
import com.acamara.scanme.api.PriceComparisonAPI
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.database.FirebaseHelper
import com.acamara.scanme.ui.home.HomeViewModel
import com.acamara.scanme.ui.map.MapsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.net.MalformedURLException
import java.net.URL


class Homepage : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var homeViewModel: HomeViewModel
  var navController : NavController? = null
  val history : TextView? = null
  internal var qrScanIntegrator: IntentIntegrator? = null
  var fb  = FirebaseHelper()
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_homepage)
    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    qrScanIntegrator = IntentIntegrator(this)
    qrScanIntegrator?.setOrientationLocked(false)
    qrScanIntegrator?.setBarcodeImageEnabled(true)
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

    val fab: FloatingActionButton = findViewById(R.id.fab)
    fab.setOnClickListener { view ->
      performAction()
    }
    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
    val navView: NavigationView = findViewById(R.id.nav_view)
    navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.nav_home, R.id.nav_history, R.id.nav_cart, R.id.nav_account, R.id.nav_help
      ), drawerLayout
    )

    setupActionBarWithNavController(navController!!, appBarConfiguration)
    navView.setupWithNavController(navController!!)
  }


  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_account -> {
        navController?.navigate(R.id.nav_account)

        true
      }

      R.id.action_home -> {
        navController?.navigate(R.id.nav_home)
        true
      }
      R.id.action_history -> {
        navController?.navigate(R.id.nav_history)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.homepage, menu)
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }

  override fun onResume() {
    super.onResume()
    searchItemNearMe()
  }

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    searchItemNearMe()
  }
  private fun performAction() {
    qrScanIntegrator?.initiateScan()
  }

  fun searchItem(title:String,barcode: String){
    val key = "AIzaSyATDLWAxlCo6dfAPrF14yTrIQtjqTwtHl8"
    val cx = "eb2c817c349db0ea1"
    val urlString =
      "https://www.googleapis.com/customsearch/v1?q=$barcode&key=$key&cx=$cx&alt=json"
    var url: URL? = null
    try {
      url = URL(urlString)
    } catch (e: MalformedURLException) {
      System.out.println("ERROR converting String to URL $e")
    }
    val searchTask = InitialSearchAPI(barcode)
    searchTask.execute(url)
  }
  fun searchItemNearMeByTitle(title:String,barcode: String){
    val key = "AIzaSyATDLWAxlCo6dfAPrF14yTrIQtjqTwtHl8"
    val cx = "d0dbadea52411d04a"
    val urlString =
            "https://www.googleapis.com/customsearch/v1?q=$title&key=$key&cx=$cx&alt=json"
    var url: URL? = null
    try {
      url = URL(urlString)
    } catch (e: MalformedURLException) {
      System.out.println("ERROR converting String to URL $e")
    }
    val searchTask = FindLocalProductAPI(barcode, title, "")
    searchTask.execute(url)
  }

  fun searchItemNearMe(){

    val list : List<Product_model>? = homeViewModel.productList.value
    if (list != null) {
      for (x in 0 until list.size){
        if (list[x].foundNear.equals("false")) {
          System.out.println("Searching "+x)
          searchItemNearMeByTitle(list.get(x).title.substringBeforeLast(','), list.get(x).barcode.toString())
        }
        if (list[x].second_parsed.equals("false")){
          GlobalScope.async {
            //System.out.println("Second parsing "+list.get(x).title)
            PriceComparisonAPI().getProduct(fab, list.get(x).barcode.toString())
          }
        }
      }
    }
  }


  @RequiresApi(Build.VERSION_CODES.O)
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    if (result != null) {
      GlobalScope.async {
        searchItem("",result.contents)
      }
      if (result.contents == null) {
        Toast.makeText(this, "Code not found", Toast.LENGTH_LONG).show()
      }
    } else {
      super.onActivityResult(requestCode, resultCode, data)
    }
  }
}
