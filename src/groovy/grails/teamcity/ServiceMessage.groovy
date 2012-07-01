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

    def propertyMissing(String name, value) {
        attributes[name] = value
    }

    def propertyMissing(String name) {
        attributes[name]
    }

    /**
     * Format the message
     */
    String toString() {
        def attrString = attributes.collect { k, v -> "${k}='${escape(v?.toString())}'" }.join(" ")
        "##teamcity[${messageName} ${attrString}]"
    }

    /**
     * Call the given logger function if enabled.
     * @param method that can take a single string argument
     */
    void write(Closure logger) {
        if( enabled ) {
            logger(toString())
        }
    }

    /**
     * Only enable by default if TeamCity is in the environment
     */
    boolean isEnabled() {
        System.getenv("TEAMCITY_VERSION") != null
    }

    private String escape(String str) {
        str.replaceAll("\\|", "||")
            .replaceAll("\n", "|n")
            .replaceAll("'", "|'")
            .replaceAll("\\[", "|[")
            .replaceAll("\\]", "|]")
    }
}
