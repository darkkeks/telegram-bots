package ru.darkkeks.telegram.hseremind

import org.springframework.stereotype.Component
import ru.darkkeks.telegram.hseremind.ruz.AllOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.AnyOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.GroupSource
import ru.darkkeks.telegram.hseremind.ruz.LectureNameFilter
import ru.darkkeks.telegram.hseremind.ruz.LectureTypeFilter
import ru.darkkeks.telegram.hseremind.ruz.LecturerNameFilter
import ru.darkkeks.telegram.hseremind.ruz.LecturerSource
import ru.darkkeks.telegram.hseremind.ruz.NoneOfRuzFilter
import ru.darkkeks.telegram.hseremind.ruz.RuzFilter
import ru.darkkeks.telegram.hseremind.ruz.RuzRuleSpec
import ru.darkkeks.telegram.hseremind.ruz.RuzSource
import ru.darkkeks.telegram.hseremind.ruz.StudentSource
import ru.darkkeks.telegram.hseremind.ruz.WeekDaysFilter
import ru.darkkeks.telegram.hseremind.youtube.PlaylistSource
import ru.darkkeks.telegram.hseremind.youtube.TitleFilter

const val spec = """
---
chats:
  - name: Example
    target:
      me: true
    lecture_rules:
      - source:
          student: "Бобень"
  - name: "193 Общий"
  
    # Куда отсылать уведомления, одно из
    #   channel: 123123
    #   group: 123123
    #   me: true
    target:
      channel: -1001288233266

    lecture_rules:
      - source:
          group: "БПМИ193"
        filter:
          none_of:
            - week_days:
                - 3
            - any_of:
              - lecturer_name: '123'
            - all_of:
              - lecturer_name: '123'
              - lecturer_name: '123'
            - none_of:
              - lecturer_name: '123'
            - lecture_name: '.*Англ.*'
      - source:
          group: "БПМИ193"
        filter:
          lecture_name: '.*Англ.*'

  - name: "Лекции"
    target:
      channel: -1001459653369

    # Брать видео из плейлистов ютаба
    youtube_rules:
      - source:
          # хештег под видео
          name: 'ДМ2'
          # id плейлиста (можно скопировать из ссылки на плейлист)
          playlist: 'PLEwK9wdS5g0rlJ8sZo5x0rJYRdl4XFeZt'
      - source:
          name: 'ДМ2'
          playlist: 'PL1Uob8iPTHGRZB9F2yjqXhGp75g1ZJFk-'
      - source:
          name: 'ТВиМС'
          playlist: 'PLEwK9wdS5g0rSOfuEG8wmghP682aEPPud'
      - source:
          name: 'МА2'
          playlist: 'PLEwK9wdS5g0qV-430pfXzTawd6pI_VUgq'
      - source:
          name: 'АКОС'
          playlist: 'PLEwK9wdS5g0qaFRRs8pcO3NT5W2IgPBfb'
      - source:
          name: 'АКОС'
          playlist: 'PLckDP3jNUDU7GH2aC2lIAilmo8k2RyWxv'
      - source:
          name: 'ОМВ'
          playlist: 'PLEwK9wdS5g0ouOxtCkrw9_niJ5IW9zm0G'
      - source:
          name: 'ОМВ'
          # группа 1
          playlist: 'PLEwK9wdS5g0oDs3nzKKdGVbErbqU944VB'
      - source:
          name: 'ОМВ'
          # группа 2
          playlist: 'PLEwK9wdS5g0pLVY8YXb7kRJLD26r1wQsR'
      - source:
          name: 'ОМВ'
          # группа 3
          playlist: 'PLEwK9wdS5g0qMOpvgu5WMMXyuQYCmqhQF'
      - source:
          name: 'ОМВ'
          # группа 4
          playlist: 'PLEwK9wdS5g0pu6sFaw5xCq1TmwxZtSEZC'      
version: 1
"""

fun main() {
    val config = safeParseSpec(spec)
    println(config)
    println(ConfigSerializer().configToString(config!!))
}

class ConfigSerializer {

    fun configToString(config: UserSpec): String {
        val chats = config.chats.mapIndexed { index, chatSpec ->
            var header = """
               |${index + 1}. Название: ${chatSpec.name}
               |   ${targetToString(chatSpec.target)}
            """

            if (!chatSpec.lectureRules.isNullOrEmpty()) {
                header += lectureRulesToString(chatSpec.lectureRules)
            }
            if (!chatSpec.youtubeRules.isNullOrEmpty()) {
                header += null + "\n"
            }

            header
        }
            .joinToString("\n")

        println("Pre margin")
        println(chats)
        println()

        return chats
            .trimMargin()
    }

    private fun lectureRulesToString(rules: List<RuzRuleSpec>): String {
        if (rules.size == 1) {
            val rule = rules.first()
            var header = "|   Уведомления о парах ${sourceToString(rule.source)}"
            if (rule.filter != null) {
                header += " с фильтром"
                header += "\n" + filterToString(rule.filter)
            }
            return header
        } else {
            return "|   Уведомления о парах:\n" + rules
                .joinToString("\n") { rule ->
                    ("- " + sourceToString(rule.source)).let {
                        if (rule.filter == null) {
                            it
                        } else {
                            it + " с фильтром:\n" +
                                    filterToString(rule.filter).prependIndent("  ")
                        }
                    }

                }
                .prependIndent("|   ")
        }
    }

    private fun filterToString(filter: Filter): String = when (filter) {
        is LectureNameFilter -> """Название лекции матчится выражением "${filter.lectureName}""""
        is LecturerNameFilter -> """Имя лектора матчится выражением "${filter.lecturerName}""""
        is WeekDaysFilter -> {
            if (filter.weekDays.size == 1) {
                """День недели = ${weekDayToString(filter.weekDays.first())}"""
            } else {
                """День недели один из [${filter.weekDays.joinToString { weekDayToString(it) }}]"""
            }
        }
        is LectureTypeFilter -> """Тип лекции равен "${filter.lectureType}""""

        is AllOfRuzFilter -> conditionToString(filter.allOf, "AND")
        is AnyOfRuzFilter -> conditionToString(filter.anyOf, "OR")
        is NoneOfRuzFilter -> conditionToString(filter.noneOf, "AND", "NOT ")

        is TitleFilter -> """Название видео матчится выражением "${filter.title}""""

        else -> "[can't serialize]"
    }

    private fun conditionToString(parts: List<Filter>, joiner: String, prefix: String = ""): String {
        val items = parts.map { part ->
            prefix + if (isComplex(part)) {
                "\n" + filterToString(part).prependIndent("  ")
            } else {
                filterToString(part)
            }
        }
        return if (parts.size > 1) {
            "- " + items.joinToString("\n- $joiner ")
        } else {
            items.first()
        }
    }

    private fun isComplex(filter: Filter) =
        (filter is AnyOfRuzFilter && filter.anyOf.size > 1)
                || (filter is AllOfRuzFilter && filter.allOf.size > 1)
                || (filter is NoneOfRuzFilter && filter.noneOf.size > 1)

    private fun weekDayToString(weekDay: Int): String = when (weekDay) {
        1 -> "Понедельник"
        2 -> "Вторник"
        3 -> "Среда"
        4 -> "Четверг"
        5 -> "Пятница"
        6 -> "Суббота"
        7 -> "Воскресенье"
        else -> "[can't serialize]"
    }

    private fun sourceToString(source: RuzSource): String = when (source) {
        is GroupSource -> """группы "${source.group}""""
        is StudentSource -> """студента "${source.student}""""
        is LecturerSource -> """лектора "${source.lecturer}""""
        else -> "[can't serialize]"
    }

    private fun targetToString(target: Target): String = when (target) {
        is MeTarget -> "Тебе в личные сообщения."
        is GroupTarget -> "В группу с id = <pre>${target.group}</pre>."
        is ChannelTarget -> "В канал с id = <pre>${target.channel}</pre>."
        else -> "В [can't serialize]"
    }
}
