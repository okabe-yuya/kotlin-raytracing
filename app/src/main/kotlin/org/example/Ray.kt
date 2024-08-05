package org.example

import org.example.Vec3

typealias Point3 = Vec3

class Ray(
    var origin: Point3 = Point3(),
    var direction: Vec3 = Vec3(),
) {
    fun at(t: Double): Point3 {
        return origin + t * direction
    }

    fun update(origin: Point3, direction: Vec3) {
        this.origin = origin
        this.direction = direction
    }

    companion object {
        fun default(): Ray {
            return Ray(
                origin = Point3(0.0, 0.0, 0.0),
                direction = Vec3(0.0, 0.0, 0.0),
            )
        }
    }
}

