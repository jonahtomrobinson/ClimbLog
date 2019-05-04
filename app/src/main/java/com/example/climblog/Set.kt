package com.example.climblog

import android.graphics.Color
import android.location.Address
import java.time.LocalDate
import java.util.*

/**
 * Created by Jonah Robinson on 24/02/2019.
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