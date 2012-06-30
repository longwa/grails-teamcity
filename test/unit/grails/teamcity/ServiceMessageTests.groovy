package grails.teamcity

class ServiceMessageTests extends GroovyTestCase {
    void testToString() {
        def message = new ServiceMessage("test")
        message.attributes.timestamp = 'now'
        message.attributes.foo = 'bar'

        def output = message.toString()

        assert output != null
        assert output == "##teamcity[test flowId='grails' foo='bar' timestamp='now']"
    }
}
