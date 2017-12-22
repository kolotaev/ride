package org.kolotaev.ride

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
}

// todo - decide on long value
@SerialVersionUID(100L)
class Id extends Serializable with Ordered[Id] {
  import Id._

  // Constructing id value:
  private val value: Array[Byte] = Array.fill[Byte](BinaryLen)(0)

  // put first 4 bytes from current timestamp
  private val timestamp: Int = System.timestamp
  value(0) = (timestamp >> 24).toByte
  value(1) = (timestamp >> 16).toByte
  value(2) = (timestamp >> 8).toByte
  value(3) = timestamp.toByte

  // next 3 bytes are the machine ID taken from md5 hash of the hostname
  value(4) = machineID(0)
  value(5) = machineID(1)
  value(6) = machineID(2)

  // next 2 bytes are the current process ID
  value(7) = (PID >> 8).toByte
  value(8) = PID.toByte

  // next 3 bytes are from ID counter
  private val i: Int = idCounter.incrementAndGet
  value(9) = (i >> 16).toByte
  value(10) = (i >> 8).toByte
  value(11) = i.toByte


  def this(str: String) {
    this
    // Spare counter increment is not valid in case of constructing from string
    idCounter.decrementAndGet

    val pattern = s"[$encodeTable]{20}".r
    str match {
      case pattern() =>
      case _ => throw new IllegalArgumentException(s"Argument should be 20 chars of any in $encodeTable")
    }

    decode(str)
  }

  def time: LocalDateTime = {
    val ts = ByteBuffer.wrap(value.slice(0, 4)).getInt
    LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), ZoneId.systemDefault)
  }

  def machine: Array[Byte] = value.slice(4, 7)

  def pid: Short = ByteBuffer.wrap(value.slice(7, 9)).getShort

  def counter: Int = {
    // todo - int enough???
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

  private def decode(s: String): Unit = {
    val src: Array[Byte] = s.getBytes(Charset.forName("ASCII"))
    value(0) = (decodeTable(src(0)) << 3 | decodeTable(src(1)) >> 2).toByte
    value(1) = (decodeTable(src(1)) << 6 | decodeTable(src(2)) << 1 | decodeTable(src(3)) >> 4).toByte
    value(2) = (decodeTable(src(3)) << 4 | decodeTable(src(4)) >> 1).toByte
    value(3) = (decodeTable(src(4)) << 7 | decodeTable(src(5)) << 2 | decodeTable(src(6)) >> 3).toByte
    value(4) = (decodeTable(src(6)) << 5 | decodeTable(src(7))).toByte
    value(5) = (decodeTable(src(8)) << 3 | decodeTable(src(9)) >> 2).toByte
    value(6) = (decodeTable(src(9)) << 6 | decodeTable(src(10)) << 1 | decodeTable(src(11)) >> 4).toByte
    value(7) = (decodeTable(src(11)) << 4 | decodeTable(src(12)) >> 1).toByte
    value(8) = (decodeTable(src(12)) << 7 | decodeTable(src(13)) << 2 | decodeTable(src(14)) >> 3).toByte
    value(9) = (decodeTable(src(14)) << 5 | decodeTable(src(15))).toByte
    value(10) = (decodeTable(src(16)) << 3 | decodeTable(src(17)) >> 2).toByte
    value(11) = (decodeTable(src(17)) << 6 | decodeTable(src(18)) << 1 | decodeTable(src(19)) >> 4).toByte
  }

  private def v(i: Int): Int = {
    if (value(i) < 0) value(i) + 256 else value(i).toInt
  }
}
