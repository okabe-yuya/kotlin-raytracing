package org.example

import org.example.Vec3
import org.example.Interval

typealias Color = Vec3

fun writeColor(pixelColor: Color) {
    val r = pixelColor.x
    val g = pixelColor.y
    val b = pixelColor.z

    // Translate the [0,1] component values to the byte range [0,255]
    val interval = Interval(0.000, 0.999)
    val rByte: Int = (256 * interval.clamp(r)).toInt()
    val gByte: Int = (256 * interval.clamp(g)).toInt()
    val bByte: Int = (256 * interval.clamp(b)).toInt()

    println("${rByte} ${gByte} ${bByte}")
}

