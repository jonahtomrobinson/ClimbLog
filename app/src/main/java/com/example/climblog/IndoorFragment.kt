package com.example.climblog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_indoor.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.file.Files.createFile
import java.nio.file.Files.getOwner
import java.time.LocalDate
import java.util.*
import java.util.stream.Stream
import kotlin.collections.ArrayList

class IndoorFragment : Fragment() {
    companion object {
        fun newInstance(): IndoorFragment {
            return IndoorFragment()
        }

        fun addLocData(location: Location, filePath: String){
            val json = JSONObject()
            val file = readFile((filePath))

            val locations = JSONObject(file).getJSONArray("location")
            locations.put(locToJsonObj(location))
            json.put("location", locations)

            saveJSON(json.toString(), filePath)
        }

        private fun locToJsonObj(location: Location): JSONObject {
            return JSONObject()
                .put("name", location.name)
                .put("address", location.address)
                .put("lastVisited", location.lastVisited)
                .put("favourite", location.favourite)
                .put("inOrOut", location.inOrOut)
        }

        private fun jsonObjToLoc(jsonObject: JSONObject): Location {
            return Location(
                jsonObject.getString("name"),
                jsonObject.getString("address"),
                jsonObject.getString("lastVisited"),
                jsonObject.getBoolean("favourite"),
                jsonObject.getString("inOrOut")
            )
        }

        fun readFile(filePath: String): String {
            try {
                val stream = FileInputStream(filePath)

                var jsonString = ""
                stream.use { stream ->
                    val fileChannel = stream.channel
                    val mappedByteBuffer = fileChannel.map(
                        FileChannel.MapMode.READ_ONLY,
                        0,
                        fileChannel.size()
                    )
                    jsonString = Charset.defaultCharset().decode(mappedByteBuffer).toString()
                }
                return jsonString
            } catch (e: Exception) {
                return ""
            }
        }

        fun saveJSON(jsonString: String, filePath: String) {
            val output: Writer
            val file: File? = File(filePath)
            if (file != null) {
                output = BufferedWriter(FileWriter(file))
                output.write(jsonString)
                output.close()
            }
        }

        fun getLocationFilePath(context: Context) : String {
            val fileName = "Locations.json"
            val storageDir = context.filesDir
            val filePath = storageDir.absolutePath + "/" + fileName
            val file = File(filePath)
            if(!file .exists()){
                val json = JSONObject()
                json.put("location", JSONArray())
                saveJSON(json.toString(), filePath)
            }
            return filePath
        }

        fun parseJSON(filePath: String, name: String) : ArrayList<Any> {
            val file = readFile(filePath)
            val arrayList = ArrayList<Any>()

            if (file != "") {
                val jsonObject = JSONObject(readFile(filePath))
                for (i in 0 .. (jsonObject.getJSONArray(name).length() - 1)){
                    arrayList.add( jsonObjToLoc(jsonObject.getJSONArray(name).getJSONObject(i)))
                }
            }

            return arrayList
        }
    }

    val locations: ArrayList<Location> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_indoor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locations.clear()

        // Loads locations into the ArrayList

        val locationArray = parseJSON(getLocationFilePath(context!!), "location")
        if (!locationArray.isEmpty() && locationArray[0] is Location){
            addLocations(locationArray as ArrayList<Location>)
        }

        //Creates a vertical Layout Manager
        rv_location_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        // Access the RecyclerView Adapter and load the data into it
        rv_location_list.adapter = LocationAdapter(locations, context!!)

        float_add_location.setOnClickListener { view ->
            val intent = Intent(activity, AddLocationActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDetach() {
        super.onDetach()
        locations.clear()
    }

    private fun addLocations(parsedData: ArrayList<Location>?) {
        if (parsedData != null) {

            var sortedParsedData= parsedData.sortedWith(compareBy{it.name})
            sortedParsedData = sortedParsedData.sortedWith(compareByDescending {it.favourite})

            for (Location in sortedParsedData) {
                if (Location.inOrOut == "indoor"){
                    locations.add(Location)
                }
            }
        }
    }

    private fun addLocData(location: Location, filePath: String){
        val json = JSONObject()
        val file = readFile((filePath))

        val locations = JSONObject(file).getJSONArray("location")
        locations.put(locToJsonObj(location))
        json.put("location", locations)

        saveJSON(json.toString(), filePath)
    }

    /*private fun createJsonData() {
        var json = JSONObject()

        val locations = JSONArray()
        locations.put(addLocation(Location("The Valley", "34 Station Road", "2018-12-31", true)))
        locations.put(addLocation(Location("The ANOTHER SHITTY GYM", "34 Station Road", "2018-12-31", true)))
        locations.put(addLocation(Location("The 3rd", "34 Station Road", "2018-12-31", true)))

        json.put("location", locations)

        saveJSON(json.toString())
    }*/

}
