package org.kolotaev.rid

import java.util.concurrent.atomic.AtomicInteger

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
  val timestamp: Int = (java.lang.System.currentTimeMillis / 1000).toInt
  value(0) = (timestamp >> 24).toByte
  value(1) = (timestamp >> 16).toByte
  value(2) = (timestamp >> 8).toByte
  value(3) = timestamp.toByte

  // next 3 bytes are the machine ID taken from its md5 hash of the hostname
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


  def getDecoding = {
    decoding
  }

  def time: Int = {
    value.slice(0, 4)
    55
  }

  def machine: Array[Byte] = {
    value.slice(4, 7)
  }

  def pid: Int = {
    value.slice(7, 9)
    88
  }

  def counter: Int = {
    value.slice(9, 12)
    17
  }

  override def toString: String = {
    "stub"
  }

  private def encode: String = {
    "foo"
  }

  private def decode(s: String) = {

  }
}
