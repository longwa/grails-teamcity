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

    void testEscapeStrings() {
        def message = new ServiceMessage("test")
        message.attributes.timestamp = 'now'
        message.attributes.multi = 'Line1\nLine2'
        message.attributes.apost = "It's hot"
        message.attributes.bar   = "Assert |"
        message.attributes.brack = "[Hi]"

        def output = message.toString()

        assert output != null
        assert output == "##teamcity[test apost='It|'s hot' bar='Assert ||' brack='|[Hi|]' flowId='grails' multi='Line1|nLine2' timestamp='now']"
    }

    void testPropertyAccess() {
        def message = new ServiceMessage("test")
        message.timestamp = 'now'
        message.foo = 'bar'

        def output = message.toString()

        assert output != null
        assert output == "##teamcity[test flowId='grails' foo='bar' timestamp='now']"
    }

    void testPropertyTypeConversion() {
        def message = new ServiceMessage("test")
        message.timestamp = 'now'
        message.foo = true

        def output = message.toString()

        assert output != null
        assert output == "##teamcity[test flowId='grails' foo='true' timestamp='now']"
    }
}
