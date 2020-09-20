package ru.darkkeks.telegram.hseremind

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

@Component
interface UserRepository : CrudRepository<User, Long>
