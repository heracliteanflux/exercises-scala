import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.catalyst.expressions.aggregate._
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object Lab {
   def main(args: Array[String]) = {
      //val q1_infile   = "/ds410/facebook"
      val q1_infile    = "facebook.csv"
      //val q2_infile_a = "/ds410/tripdata/trip_data_1.parquet"
      //val q2_infile_b = "/ds410/tripdata/trip_data_2.parquet"
      val q1_outfile   = "q1_result.csv"
      //val q2_outfile  = "q2_result.csv"
      val session      = getSession()

      answerQ1(session, q1_infile, q1_outfile)
      //answerQ2(session, q2_infile_a, q2_infile_b, q2_outfile)
   } 

   def getSession() = {
     SparkSession.builder().getOrCreate()
   }

   def answerQ1(session: SparkSession, infile: String, outfile: String): Unit = {

      val mySchema = new StructType(Array(
        new StructField("node1", StringType, false),
        new StructField("node2", StringType, false)
      ))
			
      val df             = session.read.format("csv").option("sep","\t").schema(mySchema).load(infile)
      val dfGrouped      = df.groupBy("node1").agg(count("node2").as("count1"))
      val flipped        = df.selectExpr("node2","node1")
      val flippedGrouped = flipped.groupBy("node2").agg(count("node1").as("count2"))
      val joined         = dfGrouped.join(flippedGrouped, dfGrouped.col("node1") === flippedGrouped.col("node2"), "outer")
      val joinedCleaned  = joined.selectExpr("node1 as node","count1","count2").na.fill(0).selectExpr("node","count1+count2 as neighbors").orderBy("node")
      joinedCleaned.write.format("csv").save(outfile)
   }
  
   /*
   def answerQ2(session: SparkSession, infile1: String, infile2: String,  outfile: String): Unit = {

      val janDF = session.read.format("parquet").load(infile1)
      val febDF = session.read.format("parquet").load(infile2)

      val janDF0 = janDF.selectExpr("medallion","hack_license","to_date(pickup_datetime) as date")
      val janDF1 = janDF0.groupBy("medallion","hack_license").agg(count("date").as("tripsJan"))
      val janDF2 = janDF1.selectExpr("(medallion,hack_license) as pairJan","tripsJan")

      val febDF0 = febDF.selectExpr("medallion","hack_license","to_date(pickup_datetime) as date")
      val febDF1 = febDF0.groupBy("medallion","hack_license").agg(count("date").as("tripsJan"))
      val febDF2 = febDF1.selectExpr("(medallion,hack_license) as pairFeb","tripsFan")

      val joinCondition = janDF2.col("pairJan") === febDF2.col("pairFeb")
      val joinType      = "right_outer"
      val joined        = janDF2.join(febDF2, joinCondition, joinType)
      val joined0       = joined.select(col("pairFeb").getField("medallion").as("medallion"),col("pairFeb").getField("hack_license").as("hack_license"),col("tripsJan"),col("tripsFeb"))
      val joined1       = joined0.na.fill(0,Seq("tripsJan"))
      val joined2       = joined1.where("tripsFeb>tripsJan")

      joined2.write.format("csv").save(outfile)
   }
  */
}