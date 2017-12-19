package org.kolotaev.ride

import java.util.concurrent.atomic.AtomicInteger
import java.nio.ByteBuffer
import java.time.{LocalDateTime, Instant, ZoneId}

object Id {
  final val BinaryLen: Byte = 12
  final val EncodedLen: Byte = 20
  final val DecodedLen: Byte = 15

  private val encodeTable = "0123456789abcdefghijklmnopqrstuv"

  // Initialize decoding array
  private val decoding: Array[Byte] = Array.fill[Byte](256)(0xFF.toByte)
  for ((el, i) <- encodeTable.zipWithIndex) decoding(el) = i.toByte

  // Get helper values
  private val machineID = System.machineID
  private val PID = System.processID
  private val idCounter = new AtomicInteger(System.randomInt)

  def apply(): Id = new Id()
}

class Id {
  import Id._

  // Constructing id value:
  private val value: Array[Byte] = Array.fill[Byte](BinaryLen)(0)

  // put first 4 bytes from current timestamp
  val timestamp: Int = System.timestamp
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
  val i: Int = idCounter.incrementAndGet()
  value(9) = (i >> 16).toByte
  value(10) = (i >> 8).toByte
  value(11) = i.toByte


  def time: LocalDateTime = {
    val ts = ByteBuffer.wrap(value.slice(0, 4)).getInt
    LocalDateTime.ofInstant(Instant.ofEpochSecond(ts), ZoneId.systemDefault)
  }

  def machine: Array[Byte] = {
    value.slice(4, 7)
  }

  def pid: Short = {
    ByteBuffer.wrap(value.slice(7, 9)).getShort
  }

  def counter: Int = {
    // todo - int enough???
    val bytes = value.slice(9, 12)
    bytes(0) << 16 | bytes(1) << 8 | bytes(2)
  }

  override def toString: String = {
    encode.mkString
  }

  private def encode: Array[Char] = {
    val result = Array.fill[Char](EncodedLen)(0xFF.toChar)
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

  private def decode(s: String): Id = {
    Id()
  }

  private def v(i: Int): Int = {
    if (value(i) < 0) value(i) + 256 else value(i).toInt
  }
}
