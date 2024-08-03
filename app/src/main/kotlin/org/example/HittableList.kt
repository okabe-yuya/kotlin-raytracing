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
        rayT: Interval,
        rec: HitRecord,
    ): Boolean {
        var tempRec: HitRecord = HitRecord.default()
        var hitAnything: Boolean = false
        var closestSoFar = rayT.max

        objects.forEach {
            if (it.hit(r, Interval(rayT.min, closestSoFar), tempRec)) {
                hitAnything = true
                closestSoFar = tempRec.t

                rec.t = tempRec.t
                rec.p = tempRec.p
                rec.normal = tempRec.normal
                rec.mat = tempRec.mat
            }
        }

        return hitAnything
    }
}









