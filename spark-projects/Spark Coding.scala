import scala.math.random

val n = 1000000
val count = sc.parallelize(1 until n, 100).map{i =>
	val x = random * 2 - 1
	val y = random * 2 - 1
	if (x * x + y * y < 1) 1 else 0
}.reduce(_ + _)
println("Pi is roughly " + 4.0 * count / n)