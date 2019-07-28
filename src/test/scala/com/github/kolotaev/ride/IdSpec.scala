package com.github.kolotaev.ride

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
    val ids: Array[Id] = Array[Id](Id(), Id(), Id(), Id())
    ids(0).pid should equal (ids(1).pid)
    ids(1).pid should equal (ids(2).pid)
    ids(3).pid should equal (ids(2).pid)
  }

  "ID's pid part" should "be positive" in {
    Id().pid shouldBe > (0.toShort)
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

  "IDs" should "be a 20 characters long string when being printed" in {
    val a = Id()
    s"$a".length should be (20)
  }

  "ID getBytes" should "return its byte-array representation" in {
    val a = Id("b8ugqqqoith6livvvvug")
    a.getBytes should be (Array[Byte](90, 61, 13, 107, 88, -105, 98, 106, -53, -1, -1, -3))
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

  "IDs" should "be constructed correctly when constructed using a byte-array representation" in {
    val bytes = Array[Byte](90, 61, 13, 107, 88, -105, 98, 106, -53, -1, -1, -3)
    val strId = "b8ugqqqoith6livvvvug"
    val a = Id(bytes)
    a.getBytes should be (bytes)
    a.toString should be (strId)
    a should be (Id(strId))
  }

  "IDs" should "not increment counter when constructed with byte-array" in {
    val a = Id()
    Id(a.getBytes)
    Id(a.getBytes)
    val c = Id()
    val diff = c.counter - a.counter
    diff should equal (1)
  }

  "ID" should "throw IllegalArgumentException if wrong byte-array is passed to constructor" in {
    val data = List(
      Array[Byte](),
      Array[Byte](1, 2, 3),
      Array.fill[Byte](11)(1),
      Array.fill[Byte](13)(1)
    )
    for (i <- data) {
      an [IllegalArgumentException] should be thrownBy {
        Id(i)
      }
    }
  }

  "IDs" should "not increment counter when constructed with string" in {
    val a = Id()
    new Id(a.toString)
    new Id(a.toString)
    val c = Id()
    val diff = c.counter - a.counter
    diff should equal (1)
  }

  "ID" should "throw IllegalArgumentException if wrong base32 string is passed to constructor" in {
    val data = List(
      "",
      "invalid",
      "B8SITO2oithdkWou65bg",
      "ssd232",
      "b8sito2oithdkeou65xy"
    )
    for (i <- data) {
      an [IllegalArgumentException] should be thrownBy {
        Id(i)
      }
    }
  }

  "IDs" should "support round-trip with string representation" in {
    val a = Id()
    val b = Id(Id(Id(a.toString).toString).toString)
    s"$b" should equal (s"$a")
  }

  "IDs" should "be converted to itself back and forth and be equal as objects" in {
    val a = Id()
    val b = Id(a.toString)
    b should equal (a)
  }

  "IDs" should "not be equal as objects if they represent different values" in {
    val a = Id()
    val b = Id()
    b shouldNot equal(a)
  }

  "ID" should "not be equal as objects with the string representation of itself" in {
    val a = Id()
    a shouldNot equal(a.toString)
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

  "IDs" should "be comparable" in {
    val a = Id()
    val b = Id()
    val c = Id()
    a should be < b
    b should be < c
    c should be > a
  }

  "IDs" should "be sortable with comparison operators" in {
    val ids: Array[Id] = Array[Id](Id(), Id(), Id(), Id(), Id())
    ids.sortWith(_ < _)
    ids should equal (ids)
  }
}
