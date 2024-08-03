package org.example

import kotlin.math.max
import kotlin.math.sqrt
import org.example.Hittable
import org.example.HitRecord
import org.example.Vec3
import org.example.Material

class Sphere(
    val center: Point3,
    radius: Double,
    val mat: Material,
) : Hittable {
    val radius: Double = max(0.0, radius)

    override fun hit(
        r: Ray,
        rayT: Interval,
        rec: HitRecord,
    ): Boolean {
        val oc = center - r.origin
        val a = r.direction.lengthSquared()
        val h = r.direction.dot(oc)
        val c = oc.lengthSquared() - radius * radius

        val discriminant = h * h - a * c
        if (discriminant < 0.0) {
            return false
        }

        val sqrtd = sqrt(discriminant)

        // Find the nearest root that lies in the acceptable range.
        var root = (h - sqrtd) / a
        if (!rayT.surrounds(root)) {
            root = (h + sqrtd) / a
            if (!rayT.surrounds(root)) {
                return false
            }
        }

        rec.t = root
        rec.p = r.at(rec.t)

        val outwardNormal: Vec3 = (rec.p - center) / radius
        rec.setFaceNormal(r, outwardNormal) 
        rec.mat = mat

        return true
    }
}
