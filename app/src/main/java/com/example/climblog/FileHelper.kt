package com.example.climblog

import android.app.Application
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * Created by Jonah Robinson on 04/03/2019.
 */
class FileHelper {

    companion object {
        fun newInstance(): FileHelper {
            return FileHelper()
        }

        /** Update JSON data.
         * Pass in. id: unique object id, filepath, type: Object type, locMap.
         */
        fun updateData(id: String, filePath: String, type: String, locMap: MutableMap<String, String>){
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            var updatedItem: Any? = null
            var uniqueId : String

            /** Decide unique id based on object type*/
            when (type){
                "location" -> uniqueId = "name"
                else -> return

            }

            /** Loop Json Array and remove case where id == passed id.*/
            for (i in 0 until jsonArray.length()-1){
                if (jsonArray.getJSONObject(i).getString(uniqueId) == id){
                    updatedItem = jsonObjToKotObj(jsonArray.getJSONObject(i),type)
                    jsonArray.remove(i)
                }

            }

            /** If case found add it to the bottom of the JSON file.*/
            if (updatedItem != null){
                for ((k,v) in locMap){

                    /** Case: item is a Location.*/
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

        /**
         * Delete Json item
         */
        fun deleteData(locationName: String, filePath: String, type: String){
            val storedList = parseJSON(filePath, type) as ArrayList<Location>

            for (location in storedList){
                if (location.name == locationName){
                    storedList.remove(location)
                }
            }

        }

        /**
         * Add an item to the correct Json file.
         */
        fun addData(obj: Any, type: String, filePath: String){
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            jsonArray.put(kotObjToJsonObj(obj))
            json.put(type, jsonArray)

            saveJSON(json.toString(), filePath)
        }

        /** Utility function, gets JSON array using a filePath and array name.*/
        private fun getJSONArray(name: String, filePath: String): JSONArray{
            return JSONObject(readFile((filePath))).getJSONArray(name)
        }

        /** Convert a kotlin object to a JSON object.*/
        private fun kotObjToJsonObj(obj: Any): JSONObject? {
            if (obj is Location) {
                return JSONObject()
                    .put("name", obj.name)
                    .put("address", obj.address)
                    .put("lastVisited", obj.lastVisited)
                    .put("favourite", obj.favourite)
                    .put("inOrOut", obj.inOrOut)
            }
            else if (obj is Set){
                return JSONObject()
                    .put("id", obj.id)
                    .put("locationName", obj.locationName)
                    .put("difficulty", obj.difficulty)
                    .put("colour", obj.colour)
                    .put("identifier", obj.identifier)
                    .put("date", obj.date)
            }

            return null
        }

        /** Convert a JSON object to a kotlin object.*/
        private fun jsonObjToKotObj(jsonObject: JSONObject, type: String): Any? {
            if (type == "location") {
                return Location(
                    jsonObject.getString("name"),
                    jsonObject.getString("address"),
                    jsonObject.getString("lastVisited"),
                    jsonObject.getBoolean("favourite"),
                    jsonObject.getString("inOrOut")
                )
            }
            else if (type == "set"){
                return Set(
                    jsonObject.getInt("id"),
                    jsonObject.getString("locationName"),
                    jsonObject.getString("difficulty"),
                    jsonObject.getString("colour"),
                    jsonObject.getString("identifier"),
                    jsonObject.getString("date")
                )
            }

            return null
        }

        /** Read a file as a stream. Returns JSON string.*/
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

        /** Save a JSON string into a file.*/
        private fun saveJSON(jsonString: String, filePath: String) {
            val output: Writer
            val file: File? = File(filePath)
            if (file != null) {
                output = BufferedWriter(FileWriter(file))
                output.write(jsonString)
                output.close()
            }
        }

        /** Get the file path for Locations.json.*/
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

        fun getSetFilePath(context: Context) : String {
            val fileName = "Sets.json"
            val storageDir = context.filesDir
            val filePath = storageDir.absolutePath + "/" + fileName
            val file = File(filePath)
            if(!file .exists()){
                val json = JSONObject()
                json.put("set", JSONArray())
                saveJSON(json.toString(), filePath)
            }
            return filePath
        }

        /** Parses a JSON file and returns an ArrayList containing the values.*/
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

        fun nextId(filePath: String, name: String): Int{
            val array = FileHelper.parseJSON(filePath, name) as ArrayList<Set>
            var largestId = 0
            for (item in array){
                if (item.id > largestId)
                    largestId = item.id
            }
            return largestId + 1
        }

    }

}