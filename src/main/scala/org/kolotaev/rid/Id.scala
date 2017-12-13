package org.kolotaev.rid

object Id {
  final val BinaryLen = 12
  final val EncodedLen = 20
  final val DecodedLen = 15

  private val encoding = "0123456789abcdefghijklmnopqrstuv"

  private val decoding: Array[Byte] = Array.fill[Byte](256)(0xFF.toByte)
  for ((el, i) <- encoding.zipWithIndex) decoding(el) = i.toByte

  def apply(): Id = new Id()
}

class Id {
  import Id._

  def getDecoding = {
    decoding
  }

  def time(): Int = {
    55
  }

  def machine(): Int = {
    88
  }

  def pid(): Int = {
    89
  }

  def counter(): Int = {
    17
  }

  override def toString: String = {
    "stub"
  }
}
