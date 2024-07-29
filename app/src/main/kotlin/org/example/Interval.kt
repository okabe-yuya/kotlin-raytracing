package org.example

import org.example.Rtweekend.infinity

class Interval(
    val min: Double = +infinity,
    val max: Double = -infinity,
) {

    fun size(): Double = max - min
    fun contains(x: Double): Boolean = min <= x && x <= max
    fun surrounds(x: Double): Boolean = min < x && x < max

    companion object {
        val empty = Interval(+infinity, -infinity)
        val universe = Interval(-infinity, +infinity)
    }
}

