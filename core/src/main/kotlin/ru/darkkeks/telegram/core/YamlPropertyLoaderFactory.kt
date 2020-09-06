package ru.darkkeks.telegram.core

import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.CompositePropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource
import org.springframework.util.StringUtils


class YamlPropertyLoaderFactory : DefaultPropertySourceFactory() {
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val propertySource = CompositePropertySource(name ?: getNameForResource(resource.resource))
        YamlPropertySourceLoader().load(resource.resource.filename, resource.resource).forEach {
            propertySource.addPropertySource(it)
        }
        return propertySource
    }

    private fun getNameForResource(resource: Resource): String {
        var name = resource.description
        if (!StringUtils.hasText(name)) {
            name = resource.javaClass.simpleName + "@" + System.identityHashCode(resource)
        }
        return name
    }
}
