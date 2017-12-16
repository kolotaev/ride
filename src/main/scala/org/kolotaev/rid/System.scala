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

  def processID: Int = {
    // default to random value if PID can't be obtained. In most cases it won't happen
    var result: Int = Random.nextInt
    try result = management.ManagementFactory.getRuntimeMXBean.getName.split("@")(0).toInt
    catch { case _: Exception => }
    result
  }
}
