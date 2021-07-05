package ru.darkkeks.telegram.lifestats

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface UserInsertRepository {
    fun insert(user: User): User
}

class UserInsertRepositoryImpl(
    private val jdbcAggregateOperations: JdbcAggregateOperations,
) : UserInsertRepository {
    override fun insert(user: User): User = jdbcAggregateOperations.insert(user)
}

@Repository
interface UserRepository : CrudRepository<User, Long>, UserInsertRepository

@Repository
interface EventRepository : CrudRepository<Event, Int> {
    fun countByEcid(ecid: Int): Int
    fun deleteAllByEcid(ecid: Int)
}

@Repository
interface EventClassRepository : CrudRepository<EventClass, Int> {
    fun findAllByUid(uid: Long): List<EventClass>
}
