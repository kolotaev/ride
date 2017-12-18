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

  "ID's machine part" should "be the same for all Ids generated on the same machine" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id())
    ids(0).machine should equal (ids(1).machine)
    ids(1).machine should equal (ids(2).machine)
  }

  "ID's pid part" should "be the same for all IDs generated within the same process" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id())
    ids(0).pid should equal (ids(1).pid)
    ids(1).pid should equal (ids(2).pid)

    ids(0).pid shouldBe equal (0)
  }

  "ID's counter part" should "increase monotonically" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id(), Id(), Id())
    ids(0).counter should be < ids(1).counter
    ids(1).counter should be < ids(2).counter
    ids(2).counter should be < ids(3).counter
    ids(3).counter should be < ids(4).counter
  }

//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }
}
