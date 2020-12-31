package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.hseremind.ruz.AllOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.AnyOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.LectureNameFilter
import ru.darkkeks.telegram.hseremind.ruz.LectureTypeFilter
import ru.darkkeks.telegram.hseremind.ruz.LecturerNameFilter
import ru.darkkeks.telegram.hseremind.ruz.NoneOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.RuzFilter
import ru.darkkeks.telegram.hseremind.ruz.ScheduleItem
import ru.darkkeks.telegram.hseremind.ruz.WeekDaysFilter
import ru.darkkeks.telegram.hseremind.youtube.TitleFilter
import ru.darkkeks.telegram.hseremind.youtube.Video
import ru.darkkeks.telegram.hseremind.youtube.YoutubeFilter
import java.time.LocalDateTime

@Component
class ItemFilterService {
    fun shouldNotify(item: ScheduleItem, ruzFilter: RuzFilter, now: LocalDateTime): Boolean {
        return when (ruzFilter) {
            is LectureNameFilter -> {
                ruzFilter.lectureName.toRegex(RegexOption.IGNORE_CASE).matches(item.discipline)
            }
            is WeekDaysFilter -> {
                now.dayOfWeek.value in ruzFilter.weekDays
            }
            is LecturerNameFilter -> {
                item.lecturer ?: return false
                ruzFilter.lecturerName.toRegex(RegexOption.IGNORE_CASE).matches(item.lecturer)
            }
            is LectureTypeFilter -> {
                return item.kindOfWork != null && ruzFilter.lectureType.trim() == item.kindOfWork.trim()
            }
            is AllOfRuzFilter -> {
                ruzFilter.allOf.all { shouldNotify(item, it, now) }
            }
            is AnyOfRuzFilter -> {
                ruzFilter.anyOf.any { shouldNotify(item, it, now) }
            }
            is NoneOfRuzFilter -> {
                ruzFilter.noneOf.none { shouldNotify(item, it, now) }
            }
            else -> false
        }
    }

    fun shouldNotify(item: Video, youtubeFilter: YoutubeFilter): Boolean {
        return when (youtubeFilter) {
            is TitleFilter -> {
                youtubeFilter.title.toRegex(RegexOption.IGNORE_CASE).matches(item.title)
            }
            else -> false
        }
    }
}
