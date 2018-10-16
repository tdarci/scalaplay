# lazy and strict, a discussion...

## on the board

- 🔧 The Mechanics of Strict and Lazy
- 🚂 What is lazy useful for?

## Part 1: The Mechanics of Strict and Lazy

Passing a function to a function. When does the passed-in function get called, and how many times?

```scala
// ------------------
// evaluated before function...

def doubleMe(doIt: Boolean, myVal: Int): Int = if (doIt) myVal + myVal else 0
def iAm5: Int = 5
doubleMe(true, iAm5)

// res5: Int = 10

// ------------------
// show it, using print statements...

def doubleMe(doIt: Boolean, myVal: Int): Int = {
    println("running doubleMe...")
    if (doIt) myVal + myVal else 0
}
def iAm5: Int = {
    println("running iAm5...")
    5
}

doubleMe(true, iAm5)
/*
running iAm5...
running doubleMe...
res8: Int = 10
*/

doubleMe(false, iAm5)
/*
running iAm5...
running doubleMe...
res9: Int = 0
*/
```

👆🏼 "strict" (always evaluates its arguments)

Let's get lazy...
```scala

// ------------------
// change the type of the myVal parameter...
def doubleMe(doIt: Boolean, myVal: => Int): Int = {
    println("running doubleMe...")
    if (doIt) (myVal + myVal) else 0
}

doubleMe(true, iAm5)
/*
running doubleMe...
running iAm5...
running iAm5...
res32: Int = 10
*/

doubleMe(false, iAm5)
/*
running doubleMe...
res33: Int = 0
*/

// NOTE: "THUNK" "THUNK" "THUNK"

// ------------------
// let's get even lazier...
def doubleMe(doIt: Boolean, myVal: => Int): Int = {
    println("running doubleMe...")
    lazy val sloth = myVal
    println("doing our if statement...")
    if (doIt) (sloth + sloth) else 0
}

doubleMe(true, iAm5)
/*
running doubleMe...
doing our if statement...
running iAm5...
res37: Int = 10
*/

doubleMe(false, iAm5)

/*
running doubleMe...
doing our if statement...
res38: Int = 0
*/

```
👆🏼 "lazy" (may choose not to evaluate one or more arguments)

NOTE: Syntax gets weird... between `()` methods/functions and parameterless methods/functions.

## Part 2: Q: What is Lazy Useful For...?
A: ...Streams

take this:
```scala
List(1,2,3,4).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
```

we end up with 4 lists to do this processing:
1. `List(1,2,3,4).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)`
2. `List(11,12,13,14).filter(_ % 2 == 0).map(_ * 3)`
3. `List(12,14).map(_ * 3)`
4. `List(36,42)`

a WHILE would tighten up our code, at the cost of losing composability.

our goal: `   description |<--------------------------------->| evaluation   `

Let's head over to IntelliJ...
