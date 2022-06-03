package dev.isxander.settxi.clothconfig

import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.settxi.serialization.asJson
import dev.isxander.settxi.serialization.populateFromJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.io.File
import java.util.function.Supplier

abstract class SettxiGuiWrapper(val title: Text, val file: File) : ConfigProcessor {
    override val settings = mutableListOf<Setting<*>>()

    fun load() {
        if (!file.exists()) {
            save()
            return
        }

        settings.populateFromJson(Json.decodeFromString(file.readText()))
    }

    fun save() {
        if (!file.exists())
            file.createNewFile()

        file.writeText(Json.encodeToString(settings.asJson()))
    }

    fun clothGui(parent: Screen? = null): Screen =
        ConfigBuilder.create().apply {
            parentScreen = parent
            this.title = this@SettxiGuiWrapper.title

            for (setting in settings) {
                val category = getOrCreateCategory(Text.translatable(setting.category))
                category.addEntry(clothEntry(setting))
            }

            setSavingRunnable { save() }
        }.build()

    private fun ConfigBuilder.clothEntry(setting: Setting<*>) =
        when (setting) {
            is BooleanSetting ->
                entryBuilder().startBooleanToggle(Text.translatable(setting.name), setting.get()).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                }
            is DoubleSetting ->
                entryBuilder().startDoubleField(Text.translatable(setting.name), setting.get()).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                    setMin(setting.range.start)
                    setMax(setting.range.endInclusive)
                }
            is FloatSetting ->
                entryBuilder().startFloatField(Text.translatable(setting.name), setting.get()).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                    setMin(setting.range.start)
                    setMax(setting.range.endInclusive)
                }
            is LongSetting ->
                entryBuilder().startLongSlider(
                    Text.translatable(setting.name),
                    setting.get(),
                    setting.range.first,
                    setting.range.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                }
            is IntSetting ->
                entryBuilder().startIntSlider(
                    Text.translatable(setting.name),
                    setting.get(),
                    setting.range.first,
                    setting.range.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                }
            is StringSetting ->
                entryBuilder().startStrField(Text.translatable(setting.name), setting.get()).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(Text.translatable(setting.description))
                    setSaveConsumer { setting.set(it) }
                }
            is OptionSetting ->
                entryBuilder().startStringDropdownMenu(
                    Text.translatable(setting.name),
                    setting.get().name
                ) { Text.translatable(it) }.apply {
                    defaultValue = Supplier { setting.default.name }
                    setTooltip(Text.translatable(setting.description))
                    isSuggestionMode = false
                    setSelections(setting.options.map { Text.translatable(it.name).string })
                    setSaveConsumer {
                        setting.set(setting.options.first { option ->
                            Text.translatable(option.name).string == it
                        })
                    }
                }
            is EnumSetting<*> ->
                setting.toEnumSelector(entryBuilder())
            is FileSetting ->
                throw UnsupportedOperationException("Cloth config does not support file settings")

            else -> throw IllegalArgumentException("Unknown setting type: ${setting.javaClass}")
        }.build()

    protected fun <T> Array<T>.toOptionContainer(nameProvider: (T) -> String): OptionContainer {
        return object : OptionContainer() {
            init {
                for (value in this@toOptionContainer) {
                    option(nameProvider(value))
                }
            }
        }
    }

    private fun <T : Enum<T>> EnumSetting<T>.toEnumSelector(entryBuilder: ConfigEntryBuilder): EnumSelectorBuilder<T> =
        entryBuilder.startEnumSelector(Text.translatable(name), enumClass, get()).apply {
            defaultValue = Supplier { default }
            setTooltip(Text.translatable(description))
            setSaveConsumer { set(it) }
            setEnumNameProvider { Text.translatable(nameProvider(it as T)) }
        }

}

