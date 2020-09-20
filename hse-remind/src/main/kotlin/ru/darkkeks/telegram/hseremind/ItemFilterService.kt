package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ItemFilterService {
    fun shouldNotify(item: ScheduleItem, filter: Filter): Boolean {
        return when (filter) {
            is LectureNameFilter -> {
                filter.lectureName.toRegex(RegexOption.IGNORE_CASE).matches(item.discipline)
            }
            is WeekDaysFilter -> {
                val now = LocalDateTime.now(RuzUtils.moscowZoneId)
                now.dayOfWeek.value in filter.weekDays
            }
            is LecturerNameFilter -> {
                item.lecturer ?: return false
                filter.lecturerName.toRegex(RegexOption.IGNORE_CASE).matches(item.lecturer)
            }
            is LectureTypeFilter -> {
                filter.lectureType.trim() == item.kindOfWork.trim()
            }
            is AllOfFilter -> {
                filter.allOf.all { shouldNotify(item, it) }
            }
            is AnyOfFilter -> {
                filter.anyOf.any { shouldNotify(item, it) }
            }
            is NoneOfFilter -> {
                filter.noneOf.none { shouldNotify(item, it) }
            }
            else -> false
        }
    }
}
