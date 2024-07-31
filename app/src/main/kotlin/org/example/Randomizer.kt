package org.example

import kotlin.random.Random

fun randomDouble(): Double = Random.nextDouble(0.0, 1.0)
fun randomDouble(min: Double, max: Double): Double = Random.nextDouble(min, max)

