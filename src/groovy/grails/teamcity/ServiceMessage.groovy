package grails.teamcity

import java.text.SimpleDateFormat

/**
 * A service message is the basic way of communicating with TeamCity by outputting specially formatted
 * messages to the build log.
 */
class ServiceMessage {

    def attributes = [:] as TreeMap
    def messageName

    ServiceMessage(def name) {
        messageName = name

        // Default attributes
        attributes.flowId = "grails"
        attributes.timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date())
    }

    /**
     * Format the message
     */
    String toString() {
        def attrString = attributes.collect { k, v -> "${k}='${escape(v)}'" }.join(" ")
        "##teamcity[${messageName} ${attrString}]"
    }

    private String escape(String str) {
        // TODO - Need to escape some things with "|"
        str
    }
}
