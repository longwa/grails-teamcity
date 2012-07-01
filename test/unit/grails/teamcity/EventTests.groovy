package grails.teamcity

class EventTests extends GroovyTestCase {
    def script
    def mockConsole
    def binding

    void setUp() {
        mockConsole = new MockConsole()

        binding = new Binding()
        binding.setVariable("classLoader", new GroovyClassLoader())


        binding.setVariable("grailsConsole", mockConsole)

        script = new GroovyShell(binding).evaluate(new File("scripts", "_Events.groovy"))
    }

    void testTestStart() {
        MockConsole.metaClass.log << { msg ->
            assert msg.contains("caseName.testName")
            assert msg.contains("testStarted")
        }
        script.eventTestCaseStart("caseName")
        script.eventTestStart("testName")
    }

    void testTestEnd() {
        MockConsole.metaClass.log << { msg ->
            assert msg.contains("caseName.testName")
            assert msg.contains("testFinished")
        }
        script.eventTestCaseStart("caseName")
        script.eventTestEnd("testName")
    }

    void testTestFailure() {
        MockConsole.metaClass.log << { msg ->
            assert msg.contains("caseName.testName")
            assert msg.contains("testFailed")
        }
        script.eventTestCaseStart("caseName")
        script.eventTestFailure("testName", new Exception("..."), true)
    }

    void testSuiteStarted() {
        MockConsole.metaClass.log << { msg ->
            assert msg.contains("suiteName")
            assert msg.contains("testSuiteStarted")
        }
        script.eventTestSuiteStart("suiteName")
    }

    void testSuiteEnd() {
        MockConsole.metaClass.log << { msg ->
            assert msg.contains("suiteName")
            assert msg.contains("testSuiteFinished")
        }
        script.eventTestSuiteEnd("suiteName")
    }
}

class MockConsole {
}
