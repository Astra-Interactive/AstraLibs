package ru.astrainteractive.astralibs.server

import java.util.Locale

/** Something with a language of its own, such as a connected player. */
public interface KLocaleOwner {
    /** [Locale.ROOT] while the language is unknown, so texts show their default language. */
    public val locale: Locale
}
