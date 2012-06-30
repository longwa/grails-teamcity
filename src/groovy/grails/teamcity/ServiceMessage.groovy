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
        attributes.timestamp = SimpleDateFormat.newInstance().format(new Date())
    }

    /**
     * Format the message
     */
    String toString() {
        def attrString = attributes.collect { k,v -> "${k}='${v}'" }.join(" ")
        "##teamcity[${messageName} ${attrString}]"
    }
}
