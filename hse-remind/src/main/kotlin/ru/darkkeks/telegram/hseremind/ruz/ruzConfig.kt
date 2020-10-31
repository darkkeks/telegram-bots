package ru.darkkeks.telegram.hseremind.ruz

import com.fasterxml.jackson.annotation.JsonSubTypes
import ru.darkkeks.telegram.hseremind.*

data class RuzRuleSpec(
        val source: RuzSource,
        val filter: RuzFilter?
)

@JsonSubTypes(
        JsonSubTypes.Type(value = GroupSource::class, name = "group"),
        JsonSubTypes.Type(value = StudentSource::class, name = "student"),
        JsonSubTypes.Type(value = LecturerSource::class, name = "lecturer")
)
abstract class RuzSource : Source()

data class GroupSource(val group: String) : RuzSource()
data class StudentSource(val student: String) : RuzSource()
data class LecturerSource(val lecturer: String) : RuzSource()

@JsonSubTypes(
        JsonSubTypes.Type(value = LectureNameFilter::class, name = "lecture_name"),
        JsonSubTypes.Type(value = WeekDaysFilter::class, name = "week_days"),
        JsonSubTypes.Type(value = LecturerNameFilter::class, name = "lecturer_name"),
        JsonSubTypes.Type(value = LectureTypeFilter::class, name = "lecture_type"),
        JsonSubTypes.Type(value = AllOfRuzFilter::class, name = "all_of"),
        JsonSubTypes.Type(value = AnyOfRuzFilter::class, name = "any_of"),
        JsonSubTypes.Type(value = NoneOfRuzFilter::class, name = "none_of")
)
abstract class RuzFilter : Filter()

data class LectureNameFilter(val lectureName: String) : RuzFilter()
data class WeekDaysFilter(val weekDays: List<Int>) : RuzFilter()
data class LecturerNameFilter(val lecturerName: String) : RuzFilter()
data class LectureTypeFilter(val lectureType: String) : RuzFilter()

data class AllOfRuzFilter(val allOf: List<RuzFilter>) : RuzFilter()
data class AnyOfRuzFilter(val anyOf: List<RuzFilter>) : RuzFilter()
data class NoneOfRuzFilter(val noneOf: List<RuzFilter>) : RuzFilter()
