package ru.darkkeks.telegram.hseremind

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ru.darkkeks.telegram.core.PropertyPresentPolymorphicDeserializer
import ru.darkkeks.telegram.hseremind.youtube.YouTubeRuleSpec
import ru.darkkeks.telegram.hseremind.youtube.YouTubeSource
import ru.darkkeks.telegram.hseremind.youtube.YoutubeFilter
import ru.darkkeks.telegram.hseremind.ruz.RuzFilter
import ru.darkkeks.telegram.hseremind.ruz.RuzRuleSpec
import ru.darkkeks.telegram.hseremind.ruz.RuzSource


data class UserSpec(
        val chats: List<ChatSpec>,
        val version: Int = 1
)

data class ChatSpec(
        val name: String,
        val target: Target,
        val rules: List<RuzRuleSpec>?,
        val lectureRules: List<RuzRuleSpec>?,
        val youtubeRules: List<YouTubeRuleSpec>?
)

@JsonSubTypes(
        JsonSubTypes.Type(value = MeTarget::class, name = "me"),
        JsonSubTypes.Type(value = ChannelTarget::class, name = "channel"),
        JsonSubTypes.Type(value = GroupTarget::class, name = "group")
)
abstract class Target

data class MeTarget(val me: Any) : Target()
data class ChannelTarget(val channel: Long) : Target()
data class GroupTarget(val group: Long) : Target()

fun targetToChatId(user: User, target: Target): Long? {
    return when (target) {
        is GroupTarget -> target.group
        is ChannelTarget -> target.channel
        is MeTarget -> user.id
        else -> null
    }
}


abstract class Source

abstract class Filter


val polymorphicModule: SimpleModule = SimpleModule()
        .addDeserializer(RuzFilter::class.java, PropertyPresentPolymorphicDeserializer(RuzFilter::class.java))
        .addDeserializer(RuzSource::class.java, PropertyPresentPolymorphicDeserializer(RuzSource::class.java))
        .addDeserializer(YoutubeFilter::class.java, PropertyPresentPolymorphicDeserializer(YoutubeFilter::class.java))
        .addDeserializer(YouTubeSource::class.java, PropertyPresentPolymorphicDeserializer(YouTubeSource::class.java))
        .addDeserializer(Target::class.java, PropertyPresentPolymorphicDeserializer(Target::class.java))

val readMapper: ObjectMapper = ObjectMapper(YAMLFactory())
        .registerModule(KotlinModule())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .registerModule(polymorphicModule)

val jsonWriteMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .registerModule(polymorphicModule)
