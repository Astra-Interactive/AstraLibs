# AstraLibs
This is a library with helpful functions for AstraInteractive plugins

As an example - you can see how it beign used in [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

### You can use AstraLibs as you want, but you are not allowed to use it in "pay to win projects"
## Add a ktx-core dependency to your project
This will allow you to use non-spigot utilities
```groovy
implementation("com.astrainteractive.astralibs:ktx-core:<version>")
```
## Add a spigt-core dependency to your project
This will allow you to use spigot utilities
```groovy
implementation("com.astrainteractive.astralibs:spigot-core:<version>")
```
## Also, add repository
```groovy
maven {
    url = uri("https://maven.pkg.github.com/Astra-Interactive/AstraLibs")
    credentials {
        username = <username>
        password = <token>
    }
}
```

## What can it do?
- Advanced utility functions
- valueOfOrNull for enums
- dsl catching function to safely run your functions
- dsl command executor
- dsl event handler
- advanced functions for ConfigurationSection such as getFloat, getHexString,
- advanced extensions such as String.HEX(), Player.uuid
- advanced function for ResultSet - for working with database such as ResultSet.forEach or ResultSet.map
- File management - You can easily create config files with one line: FileManager("data/config.yml")
- Time estimator: AstraEstimator{ block() }
- Logging in file
- Serializer with kotlinx - allow you to create data classes and forget about parsing config files by hand
- Cooldown class - allows you to set cooldowns for any object or event
- Licence checker - allows you to add servers with their unique keys to whitelist
- Menu and paginated menu
- Async work with coroutines
- And other minor things

As an example - you can see how it beign used in [AstraAuction](https://github.com/Astra-Interactive/AstraAuctions)

You can check auto generated documentation [HERE](https://astrainteractive.ru/documentation/)
