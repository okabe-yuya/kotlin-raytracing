package org.example

import org.example.Vec3

typealias Point3 = Vec3

class Ray(
    val origin: Point3 = Point3(),
    val direction: Vec3 = Vec3(),
) {
    fun at(t: Double): Point3 {
        return origin + t * direction
    }
}
