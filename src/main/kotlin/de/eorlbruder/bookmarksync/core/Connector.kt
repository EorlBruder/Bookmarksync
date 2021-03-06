package de.eorlbruder.bookmarksync.core

import de.eorlbruder.bookmarksync.core.utils.ResponseUtils
import de.eorlbruder.bookmarksync.shaarli.ShaarliConnector
import mu.KLogging

abstract class Connector {

    companion object : KLogging()

    val config: Sysconfig = Sysconfig()

    val entries = HashSet<Entry>()
    var entriesToSync = ArrayList<Entry>()
    abstract val name: String
    protected val requiredConfigs = HashSet<String>()

    fun write(source: String) {
        logger.info("Writing all retrieved and modified Entries to $name")
        entriesToSync.forEach { writeEntry(it, source) }
    }

    protected fun checkRequiredConfig() {
        fillRequiredConfig()
        if (requiredConfigs.any { it.isEmpty() }) {
            throw Exception("Required config for $name is missing!")
        }
    }

    protected abstract fun fillRequiredConfig()

    protected fun getAuthHeader(): Map<String, String> = ResponseUtils.getAuthorizationHeaderWithToken(getAccessToken())

    protected abstract fun getAccessToken(): String

    protected abstract fun writeEntry(entry: Entry, source: String)

}