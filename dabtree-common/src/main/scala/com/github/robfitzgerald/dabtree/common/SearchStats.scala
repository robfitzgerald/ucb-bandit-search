package com.github.robfitzgerald.dabtree.common

case class SearchStats(activated: Int = 1, suspended: Int = 0, cancelled: Int = 0, childPromoted: Int = 0) {
  def ++ (that: SearchStats): SearchStats = {
    SearchStats(
      this.activated + that.activated,
      this.suspended + that.suspended,
      this.cancelled + that.cancelled,
      this.childPromoted + that.childPromoted
    )
  }
  def totalStateTransitions: Int = (activated - 1) + suspended + cancelled
}
