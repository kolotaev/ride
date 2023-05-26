package com.github.kolotaev.ride

import java.util.concurrent.atomic.AtomicInteger
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.time.{LocalDateTime, Instant, ZoneId}


object Id {
  final val BinaryLen: Byte = 12
  final val EncodedLen: Byte = 20

  // We do not use standard base32 char table for better sorting support
  private val encodeTable = "0123456789abcdefghijklmnopqrstuv"

  private val decodeTable: Array[Byte] = Array.fill[Byte](256)(0xFF.toByte)
  for ((el, i) <- encodeTable.zipWithIndex) decodeTable(el) = i.toByte

  // Get helper values
  private val machineID = System.machineID
  private val PID = System.processID
  private val idCounter = new AtomicInteger(System.randomInt)

  def apply(): Id = new Id()

  def apply(str: String): Id = new Id(str)

  def apply(ba: Array[Byte]): Id = new Id(Some(ba))

  private def decode(s: String): Array[Byte] = {
    val pattern = s"[$encodeTable]{20}".r
    s match {
      case pattern() =>
      case _ => throw new IllegalArgumentException(s"Argument should be 20 chars of any in $encodeTable")
    }

    val src: Array[Byte] = s.getBytes(Charset.forName("ASCII"))
    val target = Array.fill[Byte](BinaryLen)(0)
    target(0) = (decodeTable(src(0)) << 3 | decodeTable(src(1)) >> 2).toByte
    target(1) = (decodeTable(src(1)) << 6 | decodeTable(src(2)) << 1 | decodeTable(src(3)) >> 4).toByte
    target(2) = (decodeTable(src(3)) << 4 | decodeTable(src(4)) >> 1).toByte
    target(3) = (decodeTable(src(4)) << 7 | decodeTable(src(5)) << 2 | decodeTable(src(6)) >> 3).toByte
    target(4) = (decodeTable(src(6)) << 5 | decodeTable(src(7))).toByte
    target(5) = (decodeTable(src(8)) << 3 | decodeTable(src(9)) >> 2).toByte
    target(6) = (decodeTable(src(9)) << 6 | decodeTable(src(10)) << 1 | decodeTable(src(11)) >> 4).toByte
    target(7) = (decodeTable(src(11)) << 4 | decodeTable(src(12)) >> 1).toByte
    target(8) = (decodeTable(src(12)) << 7 | decodeTable(src(13)) << 2 | decodeTable(src(14)) >> 3).toByte
    target(9) = (decodeTable(src(14)) << 5 | decodeTable(src(15))).toByte
    target(10) = (decodeTable(src(16)) << 3 | decodeTable(src(17)) >> 2).toByte
    target(11) = (decodeTable(src(17)) << 6 | decodeTable(src(18)) << 1 | decodeTable(src(19)) >> 4).toByte
    target
  }
}

@SerialVersionUID(100L)
class Id(bytes: Option[Array[Byte]] = None) extends Serializable with Ordered[Id] {
  import Id._

  bytes.find { _.length != BinaryLen }.foreach { _ =>
    throw new IllegalArgumentException(s"Binary representation must have length $BinaryLen")
  }

  // Constructing id value:
  private val value: Array[Byte] = bytes.getOrElse {
    val ba = Array.fill[Byte](BinaryLen)(0)

    // put first 4 bytes from current timestamp
    val timestamp: Int = System.timestamp
    ba(0) = (timestamp >> 24).toByte
    ba(1) = (timestamp >> 16).toByte
    ba(2) = (timestamp >> 8).toByte
    ba(3) = timestamp.toByte

    // next 3 bytes are the machine ID taken from md5 hash of the hostname
    ba(4) = machineID(0)
    ba(5) = machineID(1)
    ba(6) = machineID(2)

    // next 2 bytes are the current process ID
    ba(7) = (PID >> 8).toByte
    ba(8) = PID.toByte

    // next 3 bytes are from ID counter
    val i: Int = idCounter.incrementAndGet
    ba(9) = (i >> 16).toByte
    ba(10) = (i >> 8).toByte
    ba(11) = i.toByte
    ba
  }


  def this(str: String) = {
    this(Some(Id.decode(str)))
  }

  def time: LocalDateTime = {
    val ts = ByteBuffer.wrap(value.slice(0, 4)).getInt
    LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), ZoneId.systemDefault)
  }

  def machine: Array[Byte] = value.slice(4, 7)

  def pid: Short = ByteBuffer.wrap(value.slice(7, 9)).getShort

  def counter: Int = {
    val bytes = value.slice(9, 12)
    bytes(0) << 16 | bytes(1) << 8 | bytes(2)
  }

  def getBytes: Array[Byte] = value

  def compare(that: Id): Int = this.toString compare that.toString

  override def toString: String = encode.mkString

  private def encode: Array[Char] = {
    val result = new Array[Char](EncodedLen)
    result(0) = encodeTable(v(0) >> 3)
    result(1) = encodeTable((v(1) >> 6) & 0x1F | (v(0) << 2) & 0x1F)
    result(2) = encodeTable((v(1) >> 1) & 0x1F)
    result(3) = encodeTable((v(2) >> 4) & 0x1F | (v(1) << 4) & 0x1F)
    result(4) = encodeTable(v(3) >> 7 | (v(2) << 1) & 0x1F)
    result(5) = encodeTable((v(3) >> 2) & 0x1F)
    result(6) = encodeTable(v(4) >> 5 | (v(3) << 3) & 0x1F)
    result(7) = encodeTable(v(4) & 0x1F)
    result(8) = encodeTable(v(5) >> 3)
    result(9) = encodeTable((v(6) >> 6) & 0x1F | (v(5) << 2) & 0x1F)
    result(10) = encodeTable((v(6) >> 1) & 0x1F)
    result(11) = encodeTable((v(7) >> 4) & 0x1F | (v(6) << 4) & 0x1F)
    result(12) = encodeTable(v(8) >> 7 | (v(7) << 1) & 0x1F)
    result(13) = encodeTable((v(8) >> 2) & 0x1F)
    result(14) = encodeTable((v(9) >> 5) | (v(8) << 3) & 0x1F)
    result(15) = encodeTable(v(9) & 0x1F)
    result(16) = encodeTable(v(10) >> 3)
    result(17) = encodeTable((v(11) >> 6) & 0x1F | (v(10) << 2) & 0x1F)
    result(18) = encodeTable((v(11) >> 1) & 0x1F)
    result(19) = encodeTable((v(11) << 4) & 0x1F)
    result
  }

  private def v(i: Int): Int = {
    if (value(i) < 0) value(i) + 256 else value(i).toInt
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Id]

  override def equals(other: Any): Boolean = other match {
    case that: Id =>
      (that canEqual this) &&
        value.mkString == that.value.mkString
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(value)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

}
