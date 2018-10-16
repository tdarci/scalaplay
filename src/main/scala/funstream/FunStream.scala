package funstream

// This is the code that implements streams as defined in chapter 5 of Functional Programming in Scala.
// It can be run from the main.scala file in this repo... run:
//     scala main.scala
//

  // QUESTION: HOW are the trait and the object related/linked/etc?
  // ANSWER: the trait uses the "apply" from the object.
  sealed trait FunStream[+A] {

    def exists(test: A => Boolean): Boolean = this match {
      case Cons(head, tail) => {
        println("EXISTS");
        test(head()) || tail().exists(test)
      }
      case _ => false
    }

    def foldRight[B](z: => B)(folder: (A, => B) => B): B = {
      println(s"FOLDRIGHT ${this.headOption}")
      this match {
        case Cons(head, tail) => {
          println("foldRight", head()); folder(head(), tail().foldRight(z)(folder))
        }
        case _ => z
      }
    }

    def map[B](mapper: A => B): FunStream[B] = {
      println(s"MAP ${this.headOption}")
      foldRight(FunStream.empty[B])((head, tail) => FunStream.cons(mapper(head), () => tail))
    }

    def filter(filtFunc: A => Boolean): FunStream[A] = {
      println(s"FILTER ${this.headOption}")
      foldRight(FunStream.empty[A])((head, tail) =>
        if (filtFunc(head)) FunStream.cons(head, () => tail)
        else tail)
    }

    def headOption: Option[A] = this match {
      case Empty => None
      case Cons(head, _) => Some(head()) // Some? turns h into an option...
    }

    def toListRecursive: List[A] = this match {
      case Cons(h, t) => h() :: t().toListRecursive
      case _ => List()
    }

  }

  // i think we call these things "data constructors"
  case object Empty extends FunStream[Nothing]
// note: can't use our handy shortcut notation in the case class (where we avoid writing the open/close parentheses)
// note: we see from this that the head is one item and the tail is a whole list... or a stream of objects, in this case
  case class Cons[+A](head: () => A, tail: () => FunStream[A]) extends FunStream[A]

  // note that this is an object... a static class, essentially... and the name is CONFUSING, since it's the same as our trait... but that turns out to be important
  object FunStream {
    // "smart" constructor for creating non-empty stream...
    // Specifying the tail argument as a function was critical in making for lazy evaluation during our list processing.
    def cons[A](head: A, tail: () => FunStream[A]): FunStream[A] = {
      // cache what we got... memoize to avoid re-calculation
      lazy val h = head
      lazy val t = tail
      // now our "smart" constructor calls and returns our regular data constructor
      Cons(() => h, t)
    }

    // "smart" constructor for creating an empty stream:
    def empty[A]: FunStream[A] = Empty

    // Turn a list of A's into a Stream of A's.
    // And "apply" is a specially-named function.
    // We can create a new Stream like so...
    //     FunStream.apply(A, B, C)
    //     or
    //     FunStream(A, B, C)
    // note: "apply" is not called when using "new" keyword
    def apply[A](as: A*): FunStream[A] = {
      printf(s"APPLY ${as}\n")
      if (as.isEmpty) empty else cons(as.head, () => apply(as.tail: _*)) // where do as.head and as.tail come from?? aha! the * indicates that this is a parameter LIST... and the magig _* turns our list into varargs...
    }
  }
