package com.example.climblog

import android.location.Address
import java.time.LocalDate
import java.util.*

/**
 * Created by Jonah Robinson on 24/02/2019.
 */

data class Location(val name: String, val address: String, val lastVisited: String, val favourite: Boolean, val inOrOut: String)