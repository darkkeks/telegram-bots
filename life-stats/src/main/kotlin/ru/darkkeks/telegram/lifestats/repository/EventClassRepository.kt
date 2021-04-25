package ru.darkkeks.telegram.lifestats.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.darkkeks.telegram.lifestats.EventClass

@Repository
interface EventClassRepository : CrudRepository<EventClass, Int>
