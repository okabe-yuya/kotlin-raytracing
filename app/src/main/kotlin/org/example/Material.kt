package org.example

import kotlin.math.sqrt
import kotlin.math.pow
import org.example.Rtweekend.*
import org.example.HitRecord

interface Material {
    fun scatter(rIn: Ray, rec: HitRecord, attenuation: Color, scattered: Ray): Boolean
}

data class Lambertian(val albedo: Color): Material {
    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray,
    ): Boolean {
        var scatterDirection = rec.normal + randomUnitVector()
        // Catch degenerate scatter direction
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal
        }
        
        scattered.update(rec.p, scatterDirection)
        attenuation.update(albedo.x, albedo.y, albedo.z)

        return true
    }
}

data class Metal(
    val albedo: Color,
    var fuzz: Double,
): Material {

    init {
        fuzz = if (fuzz < 1.0) fuzz else 1.0
    }

    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray
    ): Boolean {
        var reflected = reflect(rIn.direction, rec.normal)
        reflected = unitVector(reflected) + (fuzz * randomUnitVector())

        scattered.update(rec.p, reflected)
        attenuation.update(albedo.x, albedo.y, albedo.z)

        return scattered.direction.dot(rec.normal) > 0
    }
}

class Dielectric(
    val refractionIndex: Double,
): Material {
    override fun scatter(
        rIn: Ray,
        rec: HitRecord,
        attenuation: Color,
        scattered: Ray,
    ): Boolean {
        attenuation.update(1.0, 1.0, 1.0)

        val ri: Double = if (rec.frontFace) (1.0 / refractionIndex) else refractionIndex
        val unitDirection: Vec3 = unitVector(rIn.direction)
        val cosTheta: Double = minOf(-unitDirection.dot(rec.normal), 1.0)
        val sinTheta: Double = sqrt(1.0 - cosTheta * cosTheta)
        val canNotRefract: Boolean = ri * sinTheta > 1.0

        lateinit var direction: Vec3
        if (canNotRefract || reflectance(cosTheta, ri) > randomDouble()) {
            direction = reflect(unitDirection, rec.normal)
        } else {
            direction = refract(unitDirection, rec.normal, ri)
        }
        scattered.update(rec.p, direction)

        return true
    }

    fun reflectance(cosine: Double, refractionIndex: Double): Double {
        // Use Schlink's approximation for reflectance.
        var r0 = (1 - refractionIndex) / (1 + refractionIndex)
        r0 = r0 * r0
        return r0 + (1 - r0) * (1 - cosine).pow(5.0)
    }
}

