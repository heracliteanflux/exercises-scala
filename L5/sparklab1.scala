import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object Lab {
   def main(args: Array[String]) = {
      val sc         = getSC()
      val infile     = "../../../spark__the_definitive_guide/Spark-The-Definitive-Guide-master/data/flight-data/csv/2010-summary.csv"
      val q1_outfile = "q1answerfile"
      val q2_outfile = "q2answerfile"

      answerQ1(sc, infile, q1_outfile)
      answerQ2(sc, infile, q2_outfile)
   } 

   def getSC() = {
     val conf = new org.apache.spark.SparkConf().setAppName("")
     new org.apache.spark.SparkContext(conf)
   }

   def answerQ1(sc: SparkContext, infile: String, outfile: String): Unit = {
     val rdd      = sc.textFile(infile)
     val filtered = rdd.filter(x=>x.contains("DEST")==false&&x.contains("Bonaire")==false)
     val cleaned  = filtered.map(_.split(",")).map(x=>(x(0),x(1)))
     val flipped  = cleaned.map(x=>(x._2,x._1))
     val destOrig = cleaned.intersection(flipped)
     val result   = destOrig.map{case (k,v)=>(k,1)}.reduceByKey((x,y)=>x+y)
     result.saveAsTextFile(outfile)
   }
  
   def answerQ2(sc: SparkContext, infile: String,  outfile: String): Unit = {
     val rdd      = sc.textFile(infile)
     val filtered = rdd.filter(x=>x.contains("DEST")==false&&x.contains("Bonaire")==false).map(_.split(","))
     val incoming = filtered.map(x=>(x(0),x(2).toInt * -1))
     val outgoing = filtered.map(x=>(x(1),x(2).toInt))
     val result   = outgoing.union(incoming).reduceByKey((x,y)=>x+y)
     result.saveAsTextFile(outfile)
   }
}