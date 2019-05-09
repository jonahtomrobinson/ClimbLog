package com.example.climblog

/**
 * @desc Data class for Set items (groups of routes/single climbs).
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

data class Set(
    var id: Int,
    var locationName: String,
    var difficulty: String,
    var colour: String,
    var identifier: String,
    var date: String,
    var routes: Int
)