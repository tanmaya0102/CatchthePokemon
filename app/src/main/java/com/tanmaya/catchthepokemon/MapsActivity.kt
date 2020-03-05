package com.tanmaya.catchthepokemon



import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
    }
    var ACCESSLOCATION=123
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23)
        {
            if(ActivityCompat.
                    checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }
        Getuserlocation()
        loadPokemon()
    }



    fun Getuserlocation(){
      Toast.makeText(this,"User Location is ON",Toast.LENGTH_LONG).show()

        var myLocation=MylocationListener()
            var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,3f,myLocation)
        var mythread=myThread()
        mythread.start()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            ACCESSLOCATION-> {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    Getuserlocation()
                } else {
                    Toast.makeText(this,"Permission is not granted",Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }
    var location:Location?=null
           inner class MylocationListener:LocationListener{

               constructor(){
                   location= Location("Start")
                   location!!.latitude=28.481216
                   location!!.longitude=77.019135
               }
               override fun onLocationChanged(p0: Location?) {
                  location=p0
               }

               override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

               override fun onProviderEnabled(p0: String?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

               override fun onProviderDisabled(p0: String?) {
                   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
               }

           }




    var oldLocation:Location?=null
    inner class myThread:Thread{
        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.latitude=0.0

        }

        override fun run() {
            while (true)
            {
                try{
                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }
                    oldLocation=location
                    runOnUiThread{
                        //Show User
                            mMap!!.clear()
                        val sydney = LatLng(location?.latitude!!, location?.longitude!!)
                        mMap.addMarker(MarkerOptions().position(sydney).title("Me").snippet("Here is my location").icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17f))
                        //Show Pokemon
                        var x=listPokemon.size-1
                        for(i in 0..x)
                        {
                            var newPokemon=listPokemon[i]
                            if(newPokemon.isCatch==false)
                            {
                                val pokeloc = LatLng(newPokemon.loc!!.latitude, newPokemon.loc!!.longitude)
                                mMap.addMarker(MarkerOptions().position(pokeloc).title(newPokemon.name!!).snippet(newPokemon.des!!+",Power:"+newPokemon!!.power).icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))


                                if(location!!.distanceTo(newPokemon.loc)<10)
                                {
                                    newPokemon.isCatch=true
                                    listPokemon[i]=newPokemon
                                    playerpower+=newPokemon.power!!
                                    Toast.makeText(applicationContext,"You caught a new Pokemon!.Your Power is "+playerpower!!,Toast.LENGTH_LONG).show()
                                }
                            }

                        }


                    }
                     Thread.sleep(1000)
                }
                catch (ex:Exception){}
            }
        }

    }
    var playerpower=0.0
    var listPokemon=ArrayList<Pokemon>()
    fun loadPokemon()
    {
        listPokemon.add(Pokemon("Bulbasaur","I am strong!",R.drawable.bulbasaur,23.0,28.4466932,77.0064982))
        listPokemon.add(Pokemon("Charmander","I am dangerous!",R.drawable.charmander,55.0,28.445985,77.005690))
        listPokemon.add(Pokemon("Squirtle","I am invincible!",R.drawable.squirtle,45.0,28.445999,77.006661))

    }
}
