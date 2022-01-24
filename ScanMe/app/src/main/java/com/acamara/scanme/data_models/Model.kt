package com.acamara.scanme.data_models

import com.acamara.scanme.R

enum class Model private constructor(val titleResId: Int, val layoutResId: Int) {
    RED(R.string.one, R.layout.promo_layout_1),
    BLUE(R.string.two, R.layout.promo_layout_2),
    GREEN(R.string.three, R.layout.promo_layout_3)
}