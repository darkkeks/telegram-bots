package ru.darkkeks.kksstat

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.darkkeks.kksstat.schema.Tables.SUBMISSIONS
import ru.darkkeks.telegram.core.createLogger
import java.time.LocalDateTime

@SpringBootApplication
class KksStatApp

fun main(args: Array<String>) {
    SpringApplication.run(KksStatApp::class.java, *args)
}

object EjudgeConstants {
    private const val DEFAULT_YEAR = 2021

    private val CONTEST_IDS_BY_YEAR = mapOf(
        2020 to (130..141).toSet(),
        2021 to (201..209).toSet() + setOf(2010, 2021),
    )

    fun getContestIds(year: Int?): Set<Int>? {
        return CONTEST_IDS_BY_YEAR[year ?: DEFAULT_YEAR]
    }
}

@RestController
@RequestMapping("/api")
class KksStatController(
    val standingsRepository: StandingsRepository,
) {
    val logger = createLogger<KksStatController>()

    val emptyScore = TaskScore(null, "Not submitted")

    @GetMapping("/get")
    fun getStandings(@RequestParam("year") year: Int?): ResponseEntity<Any> {
        val contestIds = EjudgeConstants.getContestIds(year)
            ?: return ResponseEntity.badRequest().body("Year value is invalid")
        return ResponseEntity.ok(StandingsResponse(getMergedStandings(contestIds)))
    }

    @GetMapping("/get/flat")
    fun getFlatStandings(@RequestParam("year") year: Int?): ResponseEntity<Any> {
        val contestIds = EjudgeConstants.getContestIds(year)
            ?: return ResponseEntity.badRequest().body("Year value is invalid")
        val standings = getMergedStandings(contestIds)
        return ResponseEntity.ok(FlatStandingsResponse(flattenStandings(standings)))
    }

    fun flattenStandings(standings: Standings): FlatStandings {
        val statuses = standings.rows
            .flatMap { it.tasks }
            .map { it.status }
            .distinct()
            .withIndex()
            .associate { (index, value) -> index to value }
        val indexByStatus = statuses
            .map { (index, value) -> value to index }
            .toMap()
        return FlatStandings(
            tasks = standings.tasks,
            rows = standings.rows.map { row ->
                FlatStandingsRow(
                    place = row.place,
                    user = row.user,
                    contestId = row.contestId,
                    solved = row.solved,
                    score = row.score,
                    isSelf = row.isSelf,
                    scores = row.tasks.map { task -> task.score?.toIntOrNull() },
                    statuses = row.tasks.map { task -> indexByStatus[task.status]!! }
                )
            },
            statuses = statuses
        )

    }

    fun getMergedStandings(contestIds: Set<Int>): Standings {
        val groups = standingsRepository.findLatest(contestIds)
        if (groups.isEmpty()) {
            return Standings(listOf(), listOf())
        }

        val allTasks = groups
            .map { it.standings.tasks }
            .maxByOrNull { it.size }!!

        val mergedRows = groups
            .flatMap { group ->
                group.standings.rows
                    // enrich rows with group contest id
                    .map { row -> row.copy(contestId = group.contestId) }
                    // fill missing contest
                    .map { row ->
                        val scoresByTask = group.standings.tasks.zip(row.tasks).toMap()
                        val tasks = allTasks.map { task -> scoresByTask[task] ?: emptyScore }
                        row.copy(tasks = tasks)
                    }
            }
            .sortedWith(compareBy(
                { -it.score },
                { it.user }
            ))
            .mapIndexed { index, row ->
                row.copy(place = (index + 1).toString(), isSelf = false)
            }

        return Standings(
            tasks = allTasks,
            rows = mergedRows
        )
    }

    @PostMapping("/send")
    fun postGroupStandings(@RequestBody groupStandings: GroupStandings): ResponseEntity<String> {
        logger.info("Received standings from user {}, contest id {}", groupStandings.login, groupStandings.contestId)
        val submissionRecord = SubmissionRecord(
            login = groupStandings.login,
            contestId = groupStandings.contestId,
            standings = groupStandings.standings,
            submitTime = LocalDateTime.now()
        )
        standingsRepository.save(submissionRecord)
        return ResponseEntity.ok("Ok")
    }
}

@Component
class StandingsRepository(
    val create: DSLContext,
    val mapper: ObjectMapper,
) {
    fun save(record: SubmissionRecord) {
        val standings = mapper.writeValueAsString(record.standings)
        create
            .insertInto(SUBMISSIONS)
            .set(SUBMISSIONS.LOGIN, record.login)
            .set(SUBMISSIONS.CONTEST_ID, record.contestId)
            .set(SUBMISSIONS.STANDINGS, JSONB.valueOf(standings))
            .set(SUBMISSIONS.SUBMIT_TIME, record.submitTime)
            .execute()
    }

    fun findLatest(contestIds: Set<Int>): List<SubmissionRecord> {
        return create
            .select(SUBMISSIONS.LOGIN, SUBMISSIONS.CONTEST_ID, SUBMISSIONS.STANDINGS, SUBMISSIONS.SUBMIT_TIME)
            .distinctOn(SUBMISSIONS.CONTEST_ID)
            .from(SUBMISSIONS)
            .where(SUBMISSIONS.CONTEST_ID.`in`(contestIds))
            .orderBy(SUBMISSIONS.CONTEST_ID, SUBMISSIONS.SUBMIT_TIME.desc())
            .fetch { record ->
                SubmissionRecord(
                    login = record[SUBMISSIONS.LOGIN],
                    contestId = record[SUBMISSIONS.CONTEST_ID],
                    standings = mapper.readValue(record[SUBMISSIONS.STANDINGS].data()),
                    submitTime = record[SUBMISSIONS.SUBMIT_TIME]
                )
            }
    }
}

data class SubmissionRecord(
    val submitTime: LocalDateTime,
    val login: String,
    val contestId: Int,
    val standings: Standings,
)

data class StandingsResponse(
    val standings: Standings,
)

data class GroupStandings(
    @JsonProperty("contest_id")
    val contestId: Int,
    val login: String,
    val standings: Standings,
)

data class Standings(
    val tasks: List<TaskInfo>,
    val rows: List<StandingsRow>,
)

data class TaskInfo(
    val name: String,
    val contest: String,
)

data class StandingsRow(
    val place: String,
    val user: String,
    @JsonProperty("contest_id")
    val contestId: Int,
    val tasks: List<TaskScore>,
    val solved: Int,
    val score: Int,
    @JsonProperty("is_self")
    val isSelf: Boolean,
)

data class TaskScore(
    val score: String?,
    val status: String,
)

data class FlatStandingsResponse(
    val standings: FlatStandings,
)

data class FlatStandings(
    val tasks: List<TaskInfo>,
    val rows: List<FlatStandingsRow>,
    val statuses: Map<Int, String>,
)

data class FlatStandingsRow(
    val place: String,
    val user: String,
    @JsonProperty("contest_id")
    val contestId: Int,
    val solved: Int,
    val score: Int,
    @JsonProperty("is_self")
    val isSelf: Boolean,
    val scores: List<Int?>,
    val statuses: List<Int>,
)
