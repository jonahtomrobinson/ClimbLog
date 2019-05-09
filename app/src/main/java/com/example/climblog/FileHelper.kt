package com.example.climblog

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.nio.channels.FileChannel
import java.nio.charset.Charset



/**
 * @desc Static helper class for interacting with JSON files.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

class FileHelper {

    companion object {
        fun newInstance(): FileHelper {
            return FileHelper()
        }

        /** Update JSON data.
         * Pass in. id: unique object id, filepath, type: Object type, locMap.
         * @param id: The unique id of the item.
         * @param type: A string storing the item's type.
         * @param MutableMap: A map containing the data to update.
         * @param filePath: The string filepath of the respective JSON file.
         */
        fun updateData(id: Int, type: String, locMap: MutableMap<String, String>, filePath: String) {
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            var updatedItem: Any? = null
            var uniqueId: String

            /** Decide unique id based on object type*/
            when (type) {
                "location" -> uniqueId = "id"
                "set" -> uniqueId = "id"
                else -> return

            }

            var toRemove = -1
            /** Loop Json Array and remove case where id == passed id.*/
            for (i in 0..jsonArray.length()-1) {
                if (jsonArray.getJSONObject(i).get(uniqueId) == id) {
                    updatedItem = jsonObjToKotObj(jsonArray.getJSONObject(i), type)
                    toRemove = i
                }
            }

            if (toRemove != -1){
                jsonArray.remove(toRemove)
            }

            /** If case found add it to the bottom of the JSON file.*/
            if (updatedItem != null) {
                for ((k, v) in locMap) {

                    /** Case: item is a Location.*/
                    if (updatedItem is Location) {
                        when (k) {
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

        /** Delete Json item. */

        /**
         * Deletes a Json item from its respective file.
         *
         * @param id: The an id for the item being deleted.
         * @param type: A string storing the item's type.
         * @param filePath: The string filepath of the respective JSON file.
         */
        fun deleteData(id: Int, type: String, filePath: String) {
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            var itemId: String

            /** Decide unique id based on object type*/
            when (type) {
                "location" -> itemId = "id"
                "set" -> itemId = "id"
                "completed" -> itemId = "setId"
                else -> return

            }

            var toRemove = ArrayList<Int>()
            /** Loop Json Array and remove case where id == passed id.*/
            for (i in 0..jsonArray.length() - 1) {
                if (jsonArray.getJSONObject(i).getInt(itemId) == id) {
                    toRemove.add(i)
                }

            }

            if (toRemove.isNotEmpty()){
                for (i in toRemove)
                jsonArray.remove(i)
            }

            json.put(type, jsonArray)
            saveJSON(json.toString(), filePath)

        }

        /** Add an item to the correct Json file.*/
        fun addData(obj: Any, type: String, filePath: String) {
            val json = JSONObject()
            val jsonArray = getJSONArray(type, filePath)

            jsonArray.put(kotObjToJsonObj(obj))
            json.put(type, jsonArray)

            saveJSON(json.toString(), filePath)
        }

        /**
         * Utility function, gets JSON array using a filePath and array name.
         *
         * @param type: A string storing the item's type.
         * @param filePath: The string filepath of the respective JSON file.
         */
        private fun getJSONArray(type: String, filePath: String): JSONArray {
            return JSONObject(readFile((filePath))).getJSONArray(type)
        }

        /**
         * Convert a kotlin object to a JSON object.
         *
         * @param obj: A kotlin object.
         * @return JSONObject?: A nullable JSONObject version of obj.
         */
        private fun kotObjToJsonObj(obj: Any): JSONObject? {
            when (obj) {
                is Location -> return JSONObject()
                    .put("id", obj.id)
                    .put("name", obj.name)
                    .put("address", obj.address)
                    .put("lastVisited", obj.lastVisited)
                    .put("favourite", obj.favourite)
                    .put("inOrOut", obj.inOrOut)
                is Set -> return JSONObject()
                    .put("id", obj.id)
                    .put("locationName", obj.locationName)
                    .put("difficulty", obj.difficulty)
                    .put("colour", obj.colour)
                    .put("identifier", obj.identifier)
                    .put("date", obj.date)
                    .put("routes", obj.routes)
                is Completed -> return JSONObject()
                    .put("id", obj.id)
                    .put("setId", obj.setId)
                    .put("routeNum", obj.routeNum)
                    .put("date", obj.date)
                    .put("attempts", obj.attempts)
                else -> return null
            }
        }

        /**
         * Convert a kotlin object to a JSON object.
         *
         * @param JSONObject: A JSONObject.
         * @param type: A string storing the item's type.
         * @return ANY: THe respective kotlin object to the JSON object based on type.
         */
        private fun jsonObjToKotObj(jsonObject: JSONObject, type: String): Any? {
            when (type) {
                "location" -> return Location(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("address"),
                    jsonObject.getString("lastVisited"),
                    jsonObject.getBoolean("favourite"),
                    jsonObject.getString("inOrOut")
                )
                "set" -> return Set(
                    jsonObject.getInt("id"),
                    jsonObject.getString("locationName"),
                    jsonObject.getString("difficulty"),
                    jsonObject.getString("colour"),
                    jsonObject.getString("identifier"),
                    jsonObject.getString("date"),
                    jsonObject.getInt("routes")
                )
                "completed" -> return Completed(
                    jsonObject.getInt("id"),
                    jsonObject.getInt("setId"),
                    jsonObject.getInt("routeNum"),
                    jsonObject.getString("date"),
                    jsonObject.getString("attempts")
                )
                else -> return null
            }
        }

        /**
         * Read a file as a stream. Returns JSON string.
         *
         * @param filePath: The string filepath of the respective JSON file.
         * @return String: The read file as a JSON string.
         */
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

        /**
         * Save a JSON string into a file.
         *
         * @param jsonString: A input JSON file as a JSON string.
         * @param filePath: The string filepath of the respective JSON file.
         */
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
        fun getLocationFilePath(context: Context): String {
            val fileName = "Locations.json"
            val storageDir = context.filesDir
            val filePath = storageDir.absolutePath + "/" + fileName
            val file = File(filePath)
            if (!file.exists()) {
                val json = JSONObject()
                json.put("location", JSONArray())
                saveJSON(json.toString(), filePath)
            }
            return filePath
        }

        /** Get the file path for Set.json.*/
        fun getSetFilePath(context: Context): String {
            val fileName = "Sets.json"
            val storageDir = context.filesDir
            val filePath = storageDir.absolutePath + "/" + fileName
            val file = File(filePath)
            if (!file.exists()) {
                val json = JSONObject()
                json.put("set", JSONArray())
                saveJSON(json.toString(), filePath)
            }
            return filePath
        }

        /** Get the file path for Completed.json.*/
        fun getCompletedFilePath(context: Context): String {
            val fileName = "Completed.json"
            val storageDir = context.filesDir
            val filePath = storageDir.absolutePath + "/" + fileName
            val file = File(filePath)
            if (!file.exists()) {
                val json = JSONObject()
                json.put("completed", JSONArray())
                saveJSON(json.toString(), filePath)
            }
            return filePath
        }

        /**
         * Parses a JSON file and returns an ArrayList containing the values.
         *
         * @param type: A string storing the item's type.
         * @param filePath: The string filepath of the respective JSON file.
         * @return ArrayList<Any>: An ArrayList holding an array of item objects of the respective type.
         */
        fun parseJSON(type: String, filePath: String): ArrayList<Any> {
            val file = readFile(filePath)
            val arrayList = ArrayList<Any>()

            if (file != "") {
                val jsonObject = JSONObject(readFile(filePath))
                for (i in 0..(jsonObject.getJSONArray(type).length() - 1)) {
                    arrayList.add(jsonObjToKotObj(jsonObject.getJSONArray(type).getJSONObject(i), type) as Any)
                }
            }

            return arrayList
        }

        /** Find the next "ID"/counter value for the JSON file.
         *
         * @param type: A string storing the item's type.
         * @param filePath: The string filepath of the respective JSON file.
         */
        fun nextId(type: String, filePath: String): Int {

            when (type) {
                "location" -> {
                    val array = parseJSON(type, filePath) as ArrayList<Location>
                    var largestId = 0
                    for (item in array) {
                        if (item.id > largestId)
                            largestId = item.id
                    }
                    return largestId + 1
                }
                "set" -> {
                    val array = parseJSON(type, filePath) as ArrayList<Set>
                    var largestId = 0
                    for (item in array) {
                        if (item.id > largestId)
                            largestId = item.id
                    }
                    return largestId + 1
                }
                "completed" -> {
                    val array = parseJSON(type, filePath) as ArrayList<Completed>
                    var largestId = 0
                    for (item in array) {
                        if (item.id > largestId)
                            largestId = item.id
                    }
                    return largestId + 1
                }
                else -> return (0)
            }

        }

    }

}