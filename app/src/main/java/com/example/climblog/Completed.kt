package com.example.climblog

import android.graphics.Color
import android.location.Address
import java.time.LocalDate
import java.util.*

/**
 * Created by Jonah Robinson on 24/02/2019.
 */

data class Completed(
    var id: Int,
    var setId: Int,
    var routeNum: Int,
    var date: String,
    var attempts: String
)