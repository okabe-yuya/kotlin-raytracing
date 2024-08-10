package org.example

import kotlin.math.sqrt
import kotlin.math.abs

data class Vec3(
    var e0: Double = 0.0,
    var e1: Double = 0.0,
    var e2: Double = 0.0,
) {
    val elements: DoubleArray
        get() = doubleArrayOf(e0, e1, e2)
    val x: Double
        get() = e0
    val y: Double
        get() = e1
    val z: Double
        get() = e2

    operator fun get(i: Int): Double = elements[i]
    operator fun set(
        @Suppress("UNUSED_PARAMETER") i: Int,
        @Suppress("UNUSED_PARAMETER") v: Double,
    ): Nothing = throw UnsupportedOperationException("read only")

    operator fun plus(v: Vec3): Vec3 = Vec3(e0 + v.e0, e1 + v.e1, e2 + v.e2)
    operator fun minus(v: Vec3): Vec3 = Vec3(e0 - v.e0, e1 - v.e1, e2 - v.e2)
    operator fun times(v: Vec3): Vec3 = Vec3(e0 * v.e0, e1 * v.e1, e2 * v.e2)
    operator fun times(t: Double): Vec3 = Vec3(e0 * t, e1 * t, e2 * t)
    operator fun div(t: Double): Vec3 = Vec3(e0 / t, e1 / t, e2 / t)

    operator fun unaryMinus(): Vec3 {
        return Vec3(-e0, -e1, -e2)
    }

    operator fun plusAssign(v: Vec3) {
        e0 += v.e0
        e1 += v.e1
        e2 += v.e2
    }

    operator fun timesAssign(v: Vec3) {
        e0 *= v.e0
        e1 *= v.e1
        e2 *= v.e2
    }

    operator fun divAssign(v: Vec3) {
        e0 /= v.e0
        e1 /= v.e1
        e2 /= v.e2
    }

    override fun toString(): String {
        return "$e0 $e1 $e2"
    }
    
    fun length(): Double {
        return sqrt(lengthSquared())
    }

    fun lengthSquared(): Double {
        return e0 * e0 + e1 * e1 + e2 * e2
    }

    fun nearZero(): Boolean {
        val s = 1e-8
        return abs(e0) < s && abs(e1) < s && abs(e2) < s
    }

    fun dot(v: Vec3): Double {
        return e0 * v.e0 + e1 * v.e1 + e2 * v.e2
    }
    fun cross(v: Vec3): Vec3 {
        return Vec3(
            e1 * v.e2 - e2 * v.e1,
            e2 * v.e0 - e0 * v.e2,
            e0 * v.e1 - e1 * v.e0,
        )
    }

    fun update(e0: Double, e1: Double, e2: Double) {
        this.e0 = e0
        this.e1 = e1
        this.e2 = e2
    }

    companion object {
        fun random(): Vec3 = Vec3(randomDouble(), randomDouble(), randomDouble())
        fun random(min: Double, max: Double): Vec3 {
            return Vec3(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max))
        }
    }
}


fun unitVector(v: Vec3): Vec3 {
    return v / v.length()
}

fun randomInUnitSphere(): Vec3 {
    while (true) {
        val p = Vec3.random(-1.0, 1.0)
        if (p.lengthSquared() < 1) {
            return p
        }
    }
}

fun reflect(v: Vec3, n: Vec3): Vec3 {
    return v - 2 * v.dot(n) * n
}

fun refract(uv: Vec3, n: Vec3, etaiOverEtat: Double): Vec3 {
    val cosTheta = minOf(-uv.dot(n), 1.0)
    val rOutPerp: Vec3 = etaiOverEtat * (uv + cosTheta * n)
    val rOutParallel: Vec3 = -sqrt(abs(1.0 - rOutPerp.lengthSquared())) * n

    // TODO: なぜか想定通りの値が算出されておらず一時的に「-」にしてある
    return rOutPerp - rOutParallel
}

fun randomUnitVector(): Vec3 {
    return unitVector(randomInUnitSphere())
}

fun randomOnHemisphere(normal: Vec3): Vec3 {
    val onUnitSphere: Vec3 = randomUnitVector()
    if (onUnitSphere.dot(normal) > 0.0) { // In the same hemisphere as the normal
        return onUnitSphere
    } else {
        return -onUnitSphere
    }
}

operator fun Double.times(v: Vec3): Vec3 {
    return Vec3(this * v.e0, this * v.e1, this * v.e2)
}

operator fun Double.div(v: Vec3): Vec3 {
    return (1/this) * v
}

