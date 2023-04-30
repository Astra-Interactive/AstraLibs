## AstraLibs DI package

This package will help you to create manual di

With this manual di you can easily(almost) implement your di in Dagger style

## Declaring a module

Firstly, create a module interface, which will contains necessary dependencies

```kotlin
/**
 * This is your module with two dependencies
 */
interface PluginModule : Module {
    val simpleDatabase: Single<Database>
    val pluginTranslation: Single<PluginTranslation>
}
```

After that, you will need to create implementation

```kotlin
object PluginModuleImpl : PluginModule {
    override val simpleDatabase: Single<Database> = Single {
        TODO()
    }
    override val pluginTranslation: Single<PluginTranslation> = Single {
        TODO()
    }
}
```

## Using module in function

```kotlin
/**
 * This if your function, in which you need [PluginModule.simpleDatabase]
 * and [PluginModule.pluginTranslation]
 */
fun myPluginFunction(module: PluginModule) {
    TODO()
}
```

## Using SubModules

```kotlin
/**
 * This is our custom subModule, which contains factory, which will return random UUID
 */
object SubModule : Module {
    val randomUUID = Factory {
        UUID.randomUUID()
    }
}

/**
 * This is our root module
 */
object RootModule : Module {
    /**
     * Here we getting via kotlin's ReadProperty SubModule;
     */
    val subModule by SubModule

    /**
     * Thing above actually equals to this commented expression
     */
//    val subModule: SubModule
//        get() = SubModule
    /**
     * Here we remember uuid, provided by SubModule's factory
     */
    val uuid = Single {
        subModule.randomUUID.build()
    }
}
```

That's it! As easy as it looks

## DI Components

- `Dependency` - is an interface which has getValueProperty, so can be used by `val expression by dependency`
- `Factory` - is a fun interface which can build data for your classes
- `Lateinit` - is used for components which can't be initialized internall
- `Module` - is an interface is a definition for module package, which will contains
- `Provider` - is a fun interface which can provider some data for your dependency
- `Reloadable` - can be used to create reloadable components with kotlin object
- `Single` - is a singleton value which will be a unique and single instant