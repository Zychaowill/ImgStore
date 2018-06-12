
import org.apache.spark._
import SparkContext._

object WordCount {
	def main(args: Array[String]) {
		if (args.length != 3) {
			println("usage is org.test.WordCount <master> <input> <output>")
			return
		}

		val sparkConf = new SparkConf().setAppName("WordCount")
		val sc = new SparkContext(sparkConf)
		val rowRdd = sc.textFile(args(1))
		val resultRdd = rowRdd.flatMap(line => line.split("\\s+"))
			.map(word => (word, 1))
			.reduceByKey(_ + _)
		resultRdd.saveAsTextFile(args(2))
	}
}