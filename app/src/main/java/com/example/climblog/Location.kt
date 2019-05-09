package com.example.climblog

/**
 * @desc Data class for Location items.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

data class Location(
    var id: Int,
    var name: String,
    var address: String,
    var lastVisited: String,
    var favourite: Boolean,
    var inOrOut: String
)