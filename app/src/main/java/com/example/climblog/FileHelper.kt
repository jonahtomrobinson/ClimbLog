package com.example.climblog

import android.content.Context
import android.util.Log
import android.widget.Switch
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * Created by Jonah Robinson on 04/03/2019.
 */
class LocationFileHelper {

    companion object {
        fun newInstance(): LocationFileHelper {
            return LocationFileHelper()
        }

        fun updateData(id: String, filePath: String, type: String, locMap: MutableMap<String, String>){
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            var updatedItem: Any? = null
            var uniqueId : String
            when (type){
                "location" -> uniqueId = "name"
                else -> return

            }

            for (i in 0 until jsonArray.length()-1){

                    if (jsonArray.getJSONObject(i).getString("name") == id){
                        updatedItem = jsonObjToKotObj(jsonArray.getJSONObject(i),type)
                        jsonArray.remove(i)
                    }

            }

            if (updatedItem != null){

                for ((k,v) in locMap){

                    if (updatedItem is Location){
                        when (k){
                            "name" -> updatedItem.name = v
                            "address" -> updatedItem.address = v
                            "lastVisited" -> updatedItem.lastVisited = v
                            "favourite" -> updatedItem.favourite = v.toBoolean()
                            "inOrOut" -> updatedItem.inOrOut = v
                        }
                    }
                }
                jsonArray.put(kotObjToJsonObj(updatedItem))
            }
            json.put(type, jsonArray)
            saveJSON(json.toString(), filePath)
        }

        fun deleteData(locationName: String, filePath: String, type: String){
            val storedList = parseJSON(filePath, type) as ArrayList<Location>

            for (location in storedList){
                if (location.name == locationName){
                    storedList.remove(location)
                }
            }

            //saveJSON

        }

        // Adds a Location object to a file.
        fun addData(obj: Any, type: String, filePath: String){
            val json = JSONObject()
            val locations = getJSONArray(type, filePath)

            locations.put(kotObjToJsonObj(obj))
            json.put(type, locations)

            saveJSON(json.toString(), filePath)
        }

        // Utility function, gets JSON array using a filePath and array name.
        private fun getJSONArray(name: String, filePath: String): JSONArray{
            return JSONObject(readFile((filePath))).getJSONArray(name)
        }


        private fun kotObjToJsonObj(obj: Any): JSONObject? {
            if (obj is Location) {
                return JSONObject()
                    .put("name", obj.name)
                    .put("address", obj.address)
                    .put("lastVisited", obj.lastVisited)
                    .put("favourite", obj.favourite)
                    .put("inOrOut", obj.inOrOut)
            }

            return null
        }

        private fun jsonObjToKotObj(jsonObject: JSONObject, type: String): Location? {
            if (type == "location") {
                return Location(
                    jsonObject.getString("name"),
                    jsonObject.getString("address"),
                    jsonObject.getString("lastVisited"),
                    jsonObject.getBoolean("favourite"),
                    jsonObject.getString("inOrOut")
                )
            }

            return null
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

        // Using a filepath and a
        fun parseJSON(filePath: String, name: String) : ArrayList<Any> {
            val file = readFile(filePath)
            val arrayList = ArrayList<Any>()

            if (file != "") {
                val jsonObject = JSONObject(readFile(filePath))
                for (i in 0 .. (jsonObject.getJSONArray(name).length() - 1)){
                    arrayList.add( jsonObjToKotObj(jsonObject.getJSONArray(name).getJSONObject(i),name)as Any)
                }
            }

            return arrayList
        }

    }

}