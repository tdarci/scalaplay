import funstream.{FunStream}

object MyThing {
  def main(args: Array[String]): Unit = {
    runFunstream
//    runFibonacci
  }

  private def runFunstream = {
    val foo = FunStream(1, 2, 3, 4).map(_ + 20).filter(_ % 2 == 0).map(_ * 300).toListRecursive
    println("")
    println("== DONE ==")
    println(foo)
  }

  def runFibonacci: Unit = {
    println(fib(1))
    println(fib(2))
    println(fib(3))
    println(fib(4))
    println(fib(5))
    println(fib(6))
    println(fib(7))

    val foo = Array[String]("Apple", "Banana", "Cat", "Dog", "Hello","World")
    println(isSorted(foo, (a: String, b: String) => a < b))
  }

  def abs(n: Int): Int =
    if (n < 0) -n
    else n

  def fib(n: Int): Int = {
//    @annotation.tailrec
    def go(n: Int): Int = {
      if (n <= 2) 1
      else go(n - 2) + go(n - 1)
    }

    go(n)
  }

  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    @annotation.tailrec
    def loop(n: Int): Boolean = {
      if (n > as.length - 2) true
      else if (! ordered(as(n), as(n + 1))) false
      else loop(n+1)
    }
    loop(0)
  }

}

