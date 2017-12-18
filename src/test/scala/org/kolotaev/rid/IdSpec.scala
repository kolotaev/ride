package org.kolotaev.rid

import org.scalatest._
import java.time._

class IdSpec extends FlatSpec with Matchers {
  "ID" should "support parts extraction: time" in {
    val id: Id = Id()
    val now = LocalDateTime.now(ZoneId.systemDefault)
    id.time.getYear should be (now.getYear)
    id.time.getMonth should be (now.getMonth)
    id.time.getHour should be (now.getHour)
  }

  "ID" should "support parts extraction: machine ID" in {
    val id: Id = Id()
    id.machine should have length 3
  }

  "ID" should "support parts extraction: PID" in {
    val id: Id = Id()
    id.pid should equal (37)
  }

  "ID" should "support parts extraction: current counter" in {
    val id: Id = Id()
    id.counter should equal (3)
  }

//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }
}
