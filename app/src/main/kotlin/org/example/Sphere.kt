package org.example

import kotlin.math.max
import kotlin.math.sqrt
import org.example.Hittable
import org.example.HitRecord
import org.example.Vec3


class Sphere(val center: Point3, radius: Double) : Hittable {
    val radius: Double = max(0.0, radius)

    override fun hit(
        r: Ray,
        rayTmin: Double,
        rayTmax: Double,
        rec: HitRecord,
    ): Boolean {
        val oc = center - r.origin
        val a = r.direction.length()
        val h = r.direction.dot(oc)
        val c = oc.length() - radius * radius

        val discriminant = h * h - a * c
        if (discriminant < 0.0) {
            return false
        }

        val sqrtd = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (h - sqrtd) / a
        if (root <= rayTmin || rayTmax <= root) {
            root = (h + sqrtd) / a
            if (root <= rayTmin || rayTmax <= root) {
                return false
            }
        }

        val newT = root
        val newP = r.at(newT)
        val newNormal = (newP - center) / radius 
        val outwardNormal = (newP - center) / radius
        val newRec = HitRecord(
            p = newP,
            t = newT,
            normal = newNormal,
        )
        newRec.setFaceNormal(r, outwardNormal) 

        return true
    }
}
