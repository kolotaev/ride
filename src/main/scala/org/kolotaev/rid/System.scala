package org.kolotaev.rid

import scala.util.Random
import java.security.MessageDigest
import java.net.InetAddress

object System {
  def machineID: Array[Byte] = {
    val id = Array[Byte](3)
//    val hostname = management.ManagementFactory.getRuntimeMXBean.getName.split("@")(1)
    val hostname = InetAddress.getLocalHost.getHostName

//    val m = MessageDigest.getInstance("MD5")
//    val b = hostname.getBytes("UTF-8")
//    m.update(b, 0, b.length)
//    new java.math.BigInteger(1, m.digest()).toString(16).reverse.padTo(32, "0").reverse.mkString

//    MessageDigest.getInstance("MD5").digest(hostname.getBytes)
//    m.digest().map("%02x".format(_)).mkString -- slower

    MessageDigest.getInstance("MD5").digest(hostname.getBytes).slice(0, 3)
  }

  def randomInt: Int = {
    val bytes = Array[Byte](3)
    Random.nextBytes(bytes)
    Math.abs(bytes(0) << 16 | bytes(1) << 8 | bytes(2))
  }

  def processID: Long = {
    var result: Long = 0
    try result = management.ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toLong
    catch { case _: Exception => }
    result
  }
}
