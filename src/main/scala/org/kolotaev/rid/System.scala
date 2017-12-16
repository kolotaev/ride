package org.kolotaev.rid

import scala.util.Random
import java.security.MessageDigest
import java.net.{InetAddress, UnknownHostException}

object System {
  def machineID: Array[Byte] = {
    try
      MessageDigest.getInstance("MD5")
        .digest(InetAddress.getLocalHost.getHostName.getBytes)
        .slice(0, 3)
    catch {
      case _: UnknownHostException =>
        // Fallback to random number if hostname is unavailable
        val bytes = Array.fill[Byte](3)(0)
        Random.nextBytes(bytes)
        bytes
    }
  }

  def randomInt: Int = {
    val bytes: Array[Byte] = Array.fill[Byte](3)(0)
    Random.nextBytes(bytes)
    bytes(0) << 16 | bytes(1) << 8 | bytes(2)
  }

  def processID: Long = {
    var result: Long = 0
    try result = management.ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toLong
    catch { case _: Exception => }
    result
  }
}