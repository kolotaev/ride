package org.kolotaev.rid

import org.scalatest._

class IdSpec extends FlatSpec with Matchers {

  "ID" should "support parts extraction" in {
    val id = Id
    id.time should be (888)
    id.machine should be (888)
    id.pid should be (888)
    id.counter should be (888)
  }

//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }
}
