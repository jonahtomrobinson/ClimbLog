package com.example.climblog

/**
 * @desc Data class for Completed items.
 * @author Jonah Robinson <jonahtomrobinson@gmail.com>
 * @date 07/05/2019
 */

data class Completed(
    var id: Int,
    var setId: Int,
    var routeNum: Int,
    var date: String,
    var attempts: String
)