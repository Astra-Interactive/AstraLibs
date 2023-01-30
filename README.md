[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/ktx-core?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)[![kotlin_version](https://img.shields.io/badge/kotlin-1.7.0-blueviolet?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)
[![spigot_version](https://img.shields.io/badge/spigot-%3E1.13-green?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)

# AstraLibs
This is a library with helpful functions for AstraInteractive plugins

As an example - you can see how it beign used in [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

### You can use AstraLibs as you want, but you are not allowed to use it in "pay to win projects"
## [Wiki](https://github.com/Astra-Interactive/AstraLibs/wiki)
You can see wiki[UNCOMPLETED] or even better - see [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

AstraTemplate used as template in all plugins of AstraInteractive. Architecture of AstraTemplate is easy and allows develop Spigot plugins easily and fast

## Add a ktx-core dependency to your project
This will allow you to use non-spigot utilities
```groovy
implementation("ru.astrainteractive.astralibs:ktx-core:<version>")
// module with simple sqlite ORM
implementation("ru.astrainteractive.astralibs:orm:<version>")
```
## Add a spigt-core dependency to your project
This will allow you to use spigot utilities
```groovy
implementation("ru.astrainteractive.astralibs:spigot-core:<version>")
// Module with spigot GUI
implementation("ru.astrainteractive.astralibs:spigot-gui:<version>")
```
## Also, add repository
```groovy
repositories {
  mavenCentral()
}
```

## What can it do?
- Advanced utility functions
- valueOfOrNull for enums
- Dependency containers such as Module, Reloadable and Factory
- Domain utilities such as UseCase or Mapper
- dsl command executor
- dsl event handler
- advanced class to handle Configuration values
- advanced functions for ConfigurationSection such as getFloat, getHexString,
- advanced extensions such as String.HEX(), Player.uuid
- self written simple but useful ORM wrapper on SQLite and MySQL
- simple httpClient(with get and post) over java URL
- File management - You can easily create config files with one line: FileManager("data/config.yml")
- Logging in file
- Serializer with kotlinx - allow you to create data classes and forget about parsing config files by hand
- Menu and paginated menu
- Async work with coroutines - PluginScope and AsyncComponent, which can be used as ViewModel
- Reflection util - setField, getField etc
- Serializer - allows to encode values into byteArray or string with custom IOProvider
- And other awesome things - look in AstraTemplate

As an example - you can see how it beign used in [AstraAuction](https://github.com/Astra-Interactive/AstraAuctions)

You can check auto generated documentation [HERE](https://astrainteractive.ru/documentation/)
