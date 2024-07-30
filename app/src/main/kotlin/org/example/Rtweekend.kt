package org.example.Rtweekend

import kotlin.random.Random

import org.example.Color
import org.example.Ray
import org.example.Vec3
import org.example.Interval

val infinity: Double = Double.POSITIVE_INFINITY
val pi: Double = 3.1415926535897932385

fun degreesToRadius(degrees: Double): Double {
    return degrees * pi / 180.0
}

fun randomDouble(): Double = Random.nextDouble(0.0, 1.0)
fun randomDouble(min: Double, max: Double): Double = Random.nextDouble(min, max)

