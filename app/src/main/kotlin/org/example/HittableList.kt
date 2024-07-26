package org.example

import org.example.Hittable
import org.example.HitRecord

class HittableList(
    val object_: Hittable? = null
): Hittable {
    private var objects: MutableList<Hittable> = mutableListOf()

    init {
        if (object_ != null) {
            add(object_)
        }
    }

    fun clear() = objects.clear()
    fun add(object_: Hittable) = objects.add(object_)

    override fun hit(
        r: Ray,
        rayTmin: Double,
        rayTmax: Double,
        rec: HitRecord,
    ): Boolean {
        var tempRec: HitRecord = HitRecord.default()
        var hitAnything: Boolean = false
        var closestSoFar = rayTmax

        objects.forEach {
            if (it.hit(r, rayTmin, closestSoFar, tempRec)) {
                hitAnything = true
                closestSoFar = tempRec.t

                rec.t = tempRec.t
                rec.p = tempRec.p
                rec.normal = tempRec.normal
            }
        }

        return hitAnything
    }
}









