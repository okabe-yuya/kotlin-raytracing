/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    // Image
    val imageWidth: Int = 256
    val imageHeight: Int = 256

    // Render
    print("P3\n${imageWidth} ${imageHeight}\n255\n") 

    for  (j in 0..(imageHeight - 1)) {
        for  (i in 0..(imageWidth - 1)) {
            val r = i.toDouble() / (imageWidth - 1)
            val g = j.toDouble() / (imageHeight - 1)
            val b = 0.0

            val ir: Int = (255.999 * r).toInt()
            val ig: Int = (255.999 * g).toInt()
            val ib: Int = (255.999 * b).toInt()

            print("${ir} ${ig} ${ib}\n")
        }
    }


}

