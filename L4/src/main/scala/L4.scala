object Lab {

   // input  : 3 integers x,y,z
	 // return : a tuple of the integers in sorted order
	 // for example, if the input is 2, 1, 4, the output should be (1, 2, 4)
	 // Requirements
	 //   do not use lists, vectors, or any other collections
	 //   do not use loops or recursion either
	 //   do not use the return keyword
	 //   you can use if statements and you can define helper functions (anonymous functions or other non-recursive functions)
   def q1_sorted_tuple (x: Int, y: Int, z:Int) = {
      if (x<y) if (y<z) (x,y,z) else if (x<z) (x,z,y) else (z,x,y) else if (x<z) (y,x,z) else if (y<z) (y,z,x) else (z,y,x)
   }
  
   // print : a greeting message using the name argument that tells you what the age will be in one year
	 // for instance, `q2_string_practice(”World”, 23)` should print "Hello, World, next year you will be 24."
	 // Requirements
	 //   the body of the function must be 1 line (no semicolons) and must use string interpolation
	 // note that the return type of this function is `Unit` which means that it is actually a procedure and does not return anything
   def q2_string_practice (name: String, age: Int): Unit = {
      println(s"Hello, $name, next year you will be ${age + 1}.")
   }

   def q3_indexed_sum (input: Seq[Int]) = {
      input.foldLeft(0){(a, b) => a + (input.indexOf(b) + 1) * b}
   }

   def q4_indexed_sum (input: Seq[Float]) = {
      input.foldLeft(0.0f){(a, b) => a + (input.indexOf(b) + 1) * b}
   }

   def q5_application (x: Int)(f: (Int, Int) => Int) = {
      f(f(f(x, 1), 1), 1)
   }

   def q6_cubed_no2 (input: Seq[Int]) = {
      input.map{x=>x*x*x}.filter{x => !x.toString.contains('2')}
   }

   def q7_cubed (x: Int): Vector[Int] = {
      Vector.tabulate(x){x => x * x * x}
   }

   def q8_cubed (x: Int) = {
      (for (i <- (0 until x)) yield {i * i * i}).toVector
   }

   def q9_find (input: Seq[Int], value: Int, index: Int=0): Int = {
      if (index >= input.size) -1
      else if (input(index) == value) index
      else q9_find(input, value, index + 1)
   }

   def q10_find (input: Seq[Int])(f: Int=>Boolean, index: Int=0): Int = {
      if (index >= input.size) -1
      else if (f(input(index))) index
      else q10_find(input)(f, index + 1)
   }

   def q11_tribonacci (x: Int, y: Int, z: Int, n: Int): Int = {
      if (n <= 0) -1
      else if (n == 1) x
      else if (n == 2) y
      else if (n == 3) z
      else q11_tribonacci(x, y, z, n - 1) + q11_tribonacci(x, y, z, n - 2) + q11_tribonacci(x, y, z, n - 3)
   }

   @annotation.tailrec
   def q12_tribonacci (x: Int, y: Int, z: Int, n: Int): Int = {
      if (n <= 0) -1
      else if (n == 1) x
      else if (n == 2) y
      else if (n == 3) z
      else q12_tribonacci(y, z, x + y + z, n - 1)
   }
}