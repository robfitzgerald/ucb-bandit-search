package com.github.robfitzgerald.dabtree.sampler

import com.github.robfitzgerald.dabtree.sampler.pedrosorei.UCBPedrosoReiSamplerTypeclass
import com.github.robfitzgerald.dabtree.sampler.pedrosorei.UCBPerosoReioGlobalStateOps

object implicits extends SamplerOps with UCBPedrosoReiSamplerTypeclass with UCBPerosoReioGlobalStateOps