package org.kolotaev.rid

object Id {
  def apply: Id = new Id()
}

class Id {
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
