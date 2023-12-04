package com.own.smartsubtitle.domain.usecases

import com.own.smartsubtitle.domain.model.Subtitle
import timber.log.Timber
import java.lang.StringBuilder

class SplitSourceToSubtitlesUseCase {
    operator fun invoke(source: String): List<Subtitle> {
        val subtitles = mutableListOf<Subtitle>()
        val result = source.split("\n\n")
        result.forEach {
            if (it.isNotEmpty()) {
                subtitles.add(createSubtitle(it))
            }
        }
        return subtitles
    }

    private fun createSubtitle(source: String): Subtitle {
        val lines = source.split("\n")
        val times = lines[1].split(" --> ")
        val text = StringBuilder()

        lines.forEachIndexed { index, s ->
            if (index > 1) text.append(s + "\n")
        }

        return Subtitle(
            lines[0].toInt(),
            getMillis(times[0]),
            getMillis(times[1]),
            lines.subList(2, lines.size)
        )
    }

    private fun getMillis(source: String): Long {
        val hours = source.substring(0,2).toInt()
        val minutes = source.substring(3,5).toInt()
        val seconds = source.substring(6,8).toInt()
        val millis = source.substring(9,12).toInt()
        return (((hours * 60 + minutes) * 60 + seconds) * 1000 + millis).toLong()
    }
}