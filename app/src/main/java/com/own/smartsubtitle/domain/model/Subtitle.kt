package com.own.smartsubtitle.domain.model

data class Subtitle(val position: Int, val startTime: Long, val endTime: Long, val textLines: List<String>)
