package ru.darkkeks.telegram.hseremind.ruz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RuzApi {

    @GET("search")
    fun search(
            @Query("term") term: String,
            @Query("type") type: String? = null
    ): Call<List<SearchResult>>

    @GET("schedule/{type}/{id}")
    fun schedule(
            @Path("type") type: String,
            @Path("id") id: Int,
            @Query("start") start: String? = null,
            @Query("finish") finish: String? = null
    ): Call<List<ScheduleItem>>

}

class SearchResult(
        val id: Int,
        val label: String,
        val description: String,
        val type: String
)

class ScheduleItem(
        //  "Online (ауд бронь 13)",
        val auditorium: String,
        //  100,
        val auditoriumAmount: Int,
        //  4861,
        val auditoriumOid: Int,
        //  "STAFF\\makrylova",
        val author: String,
        //  "09:30",
        val beginLesson: String,
        //  "Покровский б-р, д.11",
        val building: String,
        //  936106,
        val contentOfLoadOid: Int,
        //  "4159521155",
        val contentOfLoadUID: String,
        //  "2020-08-30T23:38:40Z00:00",
        val createddate: String,
        //  "2020.09.01",
        val date: String,
        //  "\/Date(1598907600000+0300)\/",
        val dateOfNest: String,
        //  2,
        val dayOfWeek: Int,
        //  "Вт",
        val dayOfWeekString: String,
        //  "",
        val detailInfo: String,
        //  "Алгоритмы и структуры данных 2 (углубленный курс) (рус)",
        val discipline: String,
        //  58722,
        val disciplineOid: Int,
        //  "3855949243",
        val disciplineinplan: String,
        //  7,
        val disciplinetypeload: Int,
        //  2,
        val duration: Int,
        //  "10:50",
        val endLesson: String,
        //  "АиСД2(угл)_Б2019_ПМИИ_3_2#С#Алгоритмы и структуры данных 2 (углубленный курс)",
        val group: String,
        //  28914,
        val groupOid: Int,
        //  5588,
        val group_facultyoid: Int,
        //  0,
        val hideincapacity: Int,
        //  false,
        val isBan: Boolean,
        //  "Практическое занятие",
        val kindOfWork: String,
        //  964,
        val kindOfWorkOid: Int,
        //  "1861272403",
        val kindOfWorkUid: String,
        //  "ст.преп. Смирнов Иван Федорович",
        val lecturer: String?,
        //  null,
        val lecturerCustomUID: Int?,
        //  "ifsmirnov@hse.ru",
        val lecturerEmail: String?,
        //  47938,
        val lecturerOid: Int,
        //  "1963742336",
        val lecturerUID: String,
        //  1,
        val lessonNumberEnd: Int,
        //  1,
        val lessonNumberStart: Int,
        //  7526334,
        val lessonOid: Int,
        //  "2020-08-31T20:46:31Z00:00",
        val modifieddate: String,
        //  null,
        val note: String?,
        //  "2к ПМИ 1,2 мод 2020\/2021",
        val parentschedule: String,
        //  null,
        val replaces: String?,
        //  null,
        val stream: String?,
        //  0,
        val streamOid: Int,
        //  0,
        val stream_facultyoid: Int,
        //  null,
        val subGroup: String?,
        //  0,
        val subGroupOid: Int,
        //  0,
        val subgroup_facultyoid: Int,
        //  "https:\/\/zoom.us\/j\/97496250882",
        val url1: String?,
        //  null,
        val url1_description: String?,
        //  null,
        val url2: String?,
        //  null
        val url2_description: String?
)
