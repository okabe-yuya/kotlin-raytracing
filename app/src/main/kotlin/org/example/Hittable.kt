package org.example

import org.example.Lambertian

data class HitRecord(
    var p: Point3,
    var t: Double,
    var normal: Vec3,
    var mat: Material? = null,
) {
    var frontFace: Boolean = false

    fun setFaceNormal(r: Ray, outwardNormal: Vec3) {
        frontFace = r.direction.dot(outwardNormal) < 0.0
        normal = if (frontFace) outwardNormal else -outwardNormal
    }

    companion object {
        fun default(): HitRecord {
            return HitRecord(
                p = Point3(0.0, 0.0, 0.0),
                t = 0.0,
                normal = Vec3(0.0, 0.0, 0.0),
            )
        }
    }
}

interface Hittable {
    fun hit(r: Ray, rayT: Interval, rec: HitRecord): Boolean
}

