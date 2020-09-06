package ru.darkkeks.telegram.core

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource


@Configuration
@ComponentScan
@PropertySource("classpath:core-properties.yml", factory = YamlPropertyLoaderFactory::class)
class TelegramBotsConfiguration

