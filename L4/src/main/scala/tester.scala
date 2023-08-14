object Tester extends App {
   // Note that by extending App, we are making this the entry point to all the code
   // any loose statements here (not in functions) are going to be executed when the code starts

   println("Starting Tests")

   println("Q1\nCalling q1_sorted_tuple(4,2,1)...")
   val result1 = Lab.q1_sorted_tuple(4,2,1)
   println(result1)

   println("Q2\nCalling q2_string_practice(world, 23)...")
   val result2 = Lab.q2_string_practice("world", 23)
   println(result2)
   
   println("Q3\nCalling q3_indexed_sum(List(4,1,2)...")
   val result3 = Lab.q3_indexed_sum(List(4,1,2))
   println(result3)
   println("Calling q3_indexed_sum(Vector(1,2,3,4)...")
   val result3_2 = Lab.q3_indexed_sum(Vector(1,2,3,4))
   println(result3_2)

   println("Q4\nCalling q4_indexed_sum(Vector(1,2,3,4))...")
   val result4=Lab.q4_indexed_sum(Vector(1,2,3,4))
   println(result4)

   println("Q5\nCalling q5_application with x=1 and f being the function that adds its inputs...")
   val result5 = Lab.q5_application(1){(x,y) => x+y}
   println(result5)
   println("Calling q5_application with x=2 and f being the function that subtracts the second from its first input...")
   val result5_2 = Lab.q5_application(2){(x,y) => x-y}
   println(result5_2)

   println("Q6\nCalling q6_cubed_no2(2,3,4,5,0)...")
   val result6_0=Lab.q6_cubed_no2(Vector(2,3,4,5,0))
   println(s"Answer:\nVector(8, 64, 0)\nResult:\n$result6_0")

   println("Q7\nCalling q7_cubed(5)...")
   val result7_0=Lab.q7_cubed(5)
   println(s"Answer:\nVector(0, 1, 8, 27, 64)\nResult:\n$result7_0")

   println("Q8\nCalling q8_cubed(5)...")
   val result8_0=Lab.q8_cubed(5)
   println(s"Answer:\nVector(0, 1, 8, 27, 64)\nResult:\n$result8_0")

   println("Q9\nCalling q9_find(List(2,4,6,8,10),8)...")
   val result9_0=Lab.q9_find(List(2,4,6,8,10),8)
   println(s"Answer:\n3\nResult:\n$result9_0")

   println("Q10\nCalling q10_find(Vector(1,3,4,5,6,7,8),{x=>x%2==0})...")
   val result10_0=Lab.q10_find(Vector(1,3,4,5,6,7,8)){x=>x%2==0}
   println(s"Answer:\n2\nResult:\n$result10_0")

   println("Q11\nCalling q11_tribonacci(0,0,1,11)...")
   val result11_0=Lab.q11_tribonacci(0,0,1,11)
   println(s"Answer:\n81\nResult:\n$result11_0")

   println("Q\nCalling q12_tribonacci(0,0,1,12)...")
   val result12_0=Lab.q12_tribonacci(0,0,1,12)
   println(s"Answer:\n149\nResult:\n$result12_0")
}

// object Tester extends App {
//    val result1: (Int, Int, Int) = Lab.q1_sorted_tuple(4,2,1)
//    val result2:  Unit  = Lab.q2_string_practice("world", 23)
//    val result3a: Int = Lab.q3_indexed_sum(List(4,1,2))
//    val result3b: Int = Lab.q3_indexed_sum(Vector(4,1,2))
//    val result4a: Float = Lab.q4_indexed_sum(List(1f, 2f, 3f))
//    val result4b: Float = Lab.q4_indexed_sum(Vector(1f, 2f, 3f))
//    val result5:  Int = Lab.q5_application(1){(x,y) => x+y}
//    val result6a: Seq[Int] = Lab.q6_cubed_no2(List(4,5,6))
//    val result6b: Seq[Int] = Lab.q6_cubed_no2(Vector(4,5,6))
//    val result7:  Vector[Int] = Lab.q7_cubed(5)
//    val result8:  Vector[Int] = Lab.q8_cubed(5)
//    val result9a: Int = Lab.q9_find(List(3,4,5), 3)
//    val result9b: Int = Lab.q9_find(Vector(3,4,5), 3)
//    val result10: Int = Lab.q10_find(List(2,3,4)){x => x%2 == 0}
//    val result11: Int = Lab.q11_tribonacci(1,2,3,4)
//    val result12: Int = Lab.q12_tribonacci(1,2,3,4)
// }