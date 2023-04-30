## AstraLibs DI package

This package will help you to create manual di

With this manual di you can easily(almost) implement your di in Dagger style

Firstly, create a module class, which will contains necessary dependencies

```kotlin
/**
 * This is your module with two dependencies
 */
interface PluginModule : Module {
    val simpleDatabase: Single<Database>
    val pluginTranslation: Single<PluginTranslation>
}

/**
 * This if your function, in which you need [PluginModule.simpleDatabase]
 * and [PluginModule.pluginTranslation]
 */
fun myPluginFunction(module: PluginModule) {
    TODO()
}
```

Next you need to create your root ApplicationModule

```kotlin
object ApplicationModule : Module {
    /**
     * Lateinit property for plugin instance
     */
    val plugin = Lateinit<Plugin>()

    /**
     * Provider of random integer
     */
    val randomIntProvider = Provider { Random.nextInt() }

    /**
     * Integer factory which will add 10 to every integer provided by [randomIntProvider]
     */
    val intFactory = Factory {
        val providedInt = randomIntProvider.provide()
        providedInt + 10
    }

    /**
     * This file will create reloadable instance of plugin translation
     */
    private val pluginTranslation = Reloadable {
        parse(File("./translation.yml")) as PluginTranslation
    }

    /**
     * This will bind instance of Database
     */
    private val simpleDatabase = Single {
        Database("...")
    }

    /**
     * This will create submodule
     */
    val pluginModule: PluginModule by lazy {
        PluginModuleImpl
    }

    /**
     * Private submovule implementation
     */
    private object PluginModuleImpl : PluginModule {
        override val pluginTranslation = ApplicationModule.pluginTranslation
        override val simpleDatabase = ApplicationModule.simpleDatabase
    }
}
```