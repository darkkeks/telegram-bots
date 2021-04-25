package ru.darkkeks.telegram.lifestats.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.darkkeks.telegram.lifestats.Event

@Repository
interface EventRepository : CrudRepository<Event, Int>
