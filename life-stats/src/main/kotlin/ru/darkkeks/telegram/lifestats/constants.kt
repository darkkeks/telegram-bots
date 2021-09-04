package ru.darkkeks.telegram.lifestats

object Constants {

    const val MAX_CLASSES = 10

    // add event class
    const val NAME_STATE = "create__enter_name"
    const val DESCRIPTION_STATE = "create__enter_description"
    const val TYPE_STATE = "create__enter_type"

    // report event class
    const val REPORT_CLASS_STATE = "report"

    // edit event class
    const val EDIT_CLASS_STATE = "edit"

    // remove event class
    const val REMOVE_CLASS_STATE = "remove_class"

    // edit event
    const val EDIT_EVENT_STATE = "edit_event"
    const val EDIT_EVENT_COMMENT_STATE = "edit_event_comment"
}
