package com.github.robfitzgerald.banditsearch.banditnode

import com.github.robfitzgerald.banditsearch.{MCTSStats, SearchStats}
import spire.algebra.Monoid
import spire.math.Fractional

/**
  * represents the child in a Multi-Armed Bandit UCB Search
  * @param state the (partial) state in the state-space exploration, represented by this node
  * @param action the action (if any) applied to produce this node
  * @param reward the expected long-term reward value associated with this node, computed by UCB, which should converge to a value in the range [0,1]
  * @param mctsStats simulation observation statistics, in Double precision
  * @tparam S user-provided State type
  * @tparam A user-provided Action type
  */
case class BanditChild [S,A,V : Fractional] (
  state: S,
  action: Option[A],
  reward: Double,
  mctsStats: MCTSStats[V]
) extends BanditNode[S,A,V,Double] {
}

object BanditChild {
  /**
    * promotes a Child to a Parent
    * @param child the child to promote
    * @param uctExplorationCoefficient exploration coefficient to assign to this parent
    * @param evaluate function to evaluate the uctBound, or none to leave un-calculated, which will ignore any operations on this uctBound
    * @param generateChildren function that produces all possible child action/state tuples for this state
    * @tparam S user-provided State type
    * @tparam A user-provided Action type
    * @return a child node promoted to a BanditParentRegular node
    */
  def promote[S,A,V : Fractional](
    child: BanditChild[S,A,V],
    uctExplorationCoefficient: V,
    evaluate: Option[S => V],
    generateChildren: S => Array[(S, Option[A])]
  )(implicit m: Monoid[V]): BanditParent[S,A,V] = {

    val children: Array[BanditChild[S,A,V]] = generateChildren(child.state).
        map { case (childState, childAction) =>
          BanditChild(
            childState,
            childAction,
            0D,
            MCTSStats.empty[V]()
          )
        }

    val uctBound: Option[V] = evaluate.map { fn => fn(child.state) }

    BanditParent(
      SearchState.Activated,
      child.state,
      child.action,
      child.reward,
      child.mctsStats,
      children,
      SearchStats(),
      uctExplorationCoefficient,
      uctBound
    )
  }
}