package grails.teamcity

import java.text.SimpleDateFormat
import org.codehaus.groovy.runtime.StackTraceUtils

/**
 * A service message is the basic way of communicating with TeamCity by outputting specially formatted
 * messages to the build log.
 */
class ServiceMessage {
    def attributes = [:] as TreeMap
    def messageName
    def text = ""

    ServiceMessage(name) {
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
        def attrString = attributes.collect { k, v -> "${k}='${escape(valueToString(v))}'" }.join(" ")
        if( text ) {
            "##teamcity[${messageName} '${escape(text)}']"
        }
        else {
            "##teamcity[${messageName} ${attrString}]"
        }
    }

    /**
     * Call the given logger function if service messages are enabled.
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

    protected String escape(String str) {
        // Make sure you escape the escape character first
        str.replaceAll("\\|", "||")
            .replaceAll("\n", "|n")
            .replaceAll("\r", "|r")
            .replaceAll("\u0085", "|x")
            .replaceAll("\u2028", "|l")
            .replaceAll("\u2029", "|p")
            .replaceAll("'", "|'")
            .replaceAll("\\[", "|[")
            .replaceAll("\\]", "|]")
    }

    protected String valueToString(def val) {
        if( val == null ) {
            ""
        }
        else if( val instanceof Throwable) {
            Throwable t = StackTraceUtils.deepSanitize(val)

            // Convert the sanitize stack to a string
            StringWriter stringWriter = new StringWriter()
            PrintWriter printWriter = new PrintWriter(stringWriter)
            t.printStackTrace(printWriter)

            stringWriter.toString()
        }
        else {
            val.toString()
        }
    }
}
