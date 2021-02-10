package ru.darkkeks.kksstat

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.darkkeks.kksstat.schema.Tables.SUBMISSIONS
import ru.darkkeks.telegram.core.createLogger
import java.time.LocalDateTime

@SpringBootApplication
class KksStatApp

fun main(args: Array<String>) {
    SpringApplication.run(KksStatApp::class.java, *args)
}

@RestController
@RequestMapping("/api")
class KksStatController(
    val standingsRepository: StandingsRepository
) {
    val logger = createLogger<KksStatController>()

    @GetMapping("/get")
    fun getStandings(): StandingsResponse {
        return StandingsResponse(getMergedStandings())
    }

    fun getMergedStandings(): Standings {
        val all = standingsRepository.find()
        val groups = all
            .sortedByDescending { it.submitTime }
            .distinctBy { it.contestId }
        if (groups.isEmpty()) {
            return Standings(listOf(), listOf())
        }
        val mergedRows = groups
            .flatMap { it.standings.rows }
            .sortedWith(compareBy(
                { -it.score },
                { it.user }
            ))
            .mapIndexed { index, row ->
                row.copy(place = (index + 1).toString(), isSelf = false)
            }

        return Standings(
            tasks = groups.first().standings.tasks,
            rows = mergedRows
        )
    }

    @PostMapping("/send")
    fun postGroupStandings(@RequestBody groupStandings: GroupStandings) {
        logger.info("Received standings from user {}, contest id {}", groupStandings.login, groupStandings.contestId)
        val contestId = groupStandings.contestId?.toIntOrNull() ?: 0
        val submissionRecord = SubmissionRecord(
            login = groupStandings.login ?: "",
            contestId = contestId,
            standings = groupStandings.standings,
            submitTime = LocalDateTime.now()
        )
        standingsRepository.save(submissionRecord)
    }
}

@Component
class StandingsRepository(
    val create: DSLContext,
    val mapper: ObjectMapper
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

    fun find(): List<SubmissionRecord> {
        return create
            .select(SUBMISSIONS.LOGIN, SUBMISSIONS.CONTEST_ID, SUBMISSIONS.STANDINGS, SUBMISSIONS.SUBMIT_TIME)
            .from(SUBMISSIONS)
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
    val standings: Standings
)

data class StandingsResponse(
    val standings: Standings
)

data class GroupStandings(
    @JsonProperty("contest_id")
    val contestId: String?,
    val login: String?,
    val standings: Standings
)

data class Standings(
    val tasks: List<TaskInfo>,
    val rows: List<StandingsRow>
)

data class TaskInfo(
    val name: String,
    val contest: String
)

data class StandingsRow(
    val place: String,
    val user: String,
    val tasks: List<TaskScore>,
    val solved: Int,
    val score: Int,
    @JsonProperty("is_self")
    val isSelf: Boolean
)

data class TaskScore(
    val score: String?,
    val status: String
)
