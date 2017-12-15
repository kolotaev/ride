package org.kolotaev.rid

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
  private val idCounter = System.randomInt

  def apply(): Id = new Id()
}

class Id {
  import Id._

  // Constructing id value:
  private val id: Array[Byte] = Array[Byte](BinaryLen)

  // here we put first 4 bytes from current timestamp


  id(4) = machineID(0)
  id(5) = machineID(1)
  id(6) = machineID(2)

  def getDecoding = {
    decoding
  }

  def time: Int = {
    id.slice(0, 4)
    55
  }

  def machine: Array[Byte] = {
    id.slice(4, 7)
  }

  def pid: Int = {
    id.slice(7, 9)
    88
  }

  def counter: Int = {
    id.slice(9, 12)
    17
  }

  override def toString: String = {
    "stub"
  }

  private def encode: String = {

  }

  private def decode(s: String) = {

  }
}
