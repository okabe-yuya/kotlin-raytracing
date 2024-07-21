package org.example

import org.example.Vec3

typealias Color = Vec3

fun writeColor(pixelColor: Color) {
    val r = pixelColor.x
    val g = pixelColor.y
    val b = pixelColor.z

    val rByte: Int = (255.999 * r).toInt()
    val gByte: Int = (255.999 * g).toInt()
    val bByte: Int = (255.999 * b).toInt()

    println("${rByte} ${gByte} ${bByte}")
}

