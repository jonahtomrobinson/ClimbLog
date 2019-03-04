package com.example.climblog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
    }

    val locations: ArrayList<Location> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_indoor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locations.clear()

        //addLocData()
        //addJsonData(Location("A new test", "a", "2018", true))

        // Loads locations into the ArrayList

        val locationArray = parseJSON(getLocationFilePath(), "location")
        if (locationArray[0] is Location){
            addLocations(locationArray as ArrayList<Location>)
        }

        //Creates a vertical Layout Manager
        rv_location_list.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        // Access the RecyclerView Adapter and load the data into it
        rv_location_list.adapter = LocationAdapter(locations, context!!)
    }

    override fun onDetach() {
        super.onDetach()
        locations.clear()
    }

    private fun addLocations(parsedData: ArrayList<Location>?) {
        if (parsedData != null) {
            for (Location in parsedData) {
                locations.add(Location)
            }
        }
    }

    private fun addLocData(location: Location){
        val json = JSONObject()
        val file = readFile(getLocationFilePath())

        val locations = JSONObject(file).getJSONArray("location")
        locations.put(locToJsonObj(location))
        json.put("location", locations)

        saveJSON(json.toString(), getLocationFilePath())
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

    private fun saveJSON(jsonString: String, filePath: String) {
        val output: Writer
        val file: File? = File(filePath)
        if (file != null) {
            output = BufferedWriter(FileWriter(file))
            output.write(jsonString)
            output.close()
        }
    }

    private fun parseJSON(filePath: String, name: String) : ArrayList<Any> {
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

    private fun readFile(filePath: String): String {
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

    private fun getLocationFilePath() : String {
        val fileName = "IndoorLocations.json"
        val storageDir = context!!.filesDir
        return storageDir.absolutePath + "/" + fileName
    }

    private fun locToJsonObj(location: Location): JSONObject {
        return JSONObject()
            .put("name", location.name)
            .put("address", location.address)
            .put("lastVisited", location.lastVisited)
            .put("Favourite", location.favourite)
    }

    private fun jsonObjToLoc(jsonObject: JSONObject): Location {
        return Location(
            jsonObject.getString("name"),
            jsonObject.getString("address"),
            jsonObject.getString("lastVisited"),
            jsonObject.getBoolean("Favourite")
        )
    }

}
