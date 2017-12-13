package org.kolotaev.rid

object System {
  def machineID: Array[Byte] = {
    val id = Array[Byte](3)
    id
  }

  def randomInt: Int = {
    val id = Array[Byte](3)
    55
  }

  def processID: Long = {
    // todo - only for *nix now.
    import sys.process._
    Seq("sh", "-c", "echo $PPID").!!.trim.toLong

    //  val vote = null
    //  def getPID: Long = {
    //    val processName = management.ManagementFactory.getRuntimeMXBean.getName
    //    if (processName != null && processName.length > 0) try
    //      return Long.parseLong(processName.split("@")(0))
    //    catch {
    //      case e: Exception =>
    //        return 0
    //    }
    //    0
    //  }
  }
}
