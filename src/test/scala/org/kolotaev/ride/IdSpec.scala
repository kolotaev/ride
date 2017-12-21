package org.kolotaev.ride

import org.scalatest._
import java.time._
import scala.util.Random
import java.io.{ObjectOutputStream, ObjectInputStream}
import java.io.{FileOutputStream, FileInputStream}


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

    ids(0).pid shouldBe > (0)
  }

  "ID's counter part" should "increase monotonically" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id(), Id(), Id())
    ids(0).counter should be < ids(1).counter
    ids(1).counter should be < ids(2).counter
    ids(2).counter should be < ids(3).counter
    ids(3).counter should be < ids(4).counter
    (ids(4).counter - ids(0).counter) should equal (4)
  }

  "ID" should "be converted to string" in {
    val id1 = Id()
    val id2 = Id()
    s"$id1" shouldNot equal (s"$id2")
  }

  "IDs" should "print me!!!!!!!" in {
    (1 to 100) foreach {_ =>
      println(Id())
    }
    1 should equal (5)
  }

  "IDs" should "be unique" in {
    val ids: Array[String] = Array.fill[String](1000000){ Id().toString }
    ids.toSet.size should equal (ids.length)
  }

  "IDs" should "be unique with random time sleep" in {
    val ids: Array[String] = Array.fill[String](1000000) {
      Thread.sleep(Random.nextInt(1))
      Id().toString
    }
    ids.toSet.size should equal (ids.length)
  }

  "IDs" should "not increment counter when constructed with string" in {
    val a = Id()
    new Id(a.toString)
    new Id(a.toString)
    val c = Id()
    val diff = c.counter - a.counter
    diff should equal (1)
  }

  "IDs" should "support round-trip" in {
    val a = Id()
    val b = Id(Id(Id(a.toString).toString).toString)
    s"$b" should equal (s"$a")
  }

  "IDs" should "be converted to itself back and forth and be not equal as objects" in {
    val a = Id()
    val b = Id(a.toString)
    b shouldNot equal (a)
  }

  "IDs" should "be converted to itself back and forth and be equal as strings" in {
    val a = Id()
    val b = new Id(a.toString)
    s"$b" should equal (s"$a")
  }

  "ID" should "be serializable" in {
    val file = s"/tmp/org_kolotaev_ride_${System.timestamp}"
    val a = Id()

    val os = new ObjectOutputStream(new FileOutputStream(file))
    os.writeObject(a)
    os.close()

    val is = new ObjectInputStream(new FileInputStream(file))
    val b = is.readObject
    is.close()

    s"$b" should equal (s"$a")
  }

  "IDs" should "be sortable" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id(), Id(), Id())
    ids.sortWith(_ < _)
    ids should equal (ids)
  }
}
