package com.example.climblog

import android.location.Address
import java.time.LocalDate
import java.util.*

/**
 * Created by Jonah Robinson on 24/02/2019.
 */

data class Location(var name: String, var address: String, var lastVisited: String, var favourite: Boolean, var inOrOut: String)