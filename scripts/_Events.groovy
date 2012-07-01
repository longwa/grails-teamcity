// Dynamically load the service method class
def messageClass = loadMessageClass("ServiceMessage")

/**
 * Compile events
 */
eventCompileStart = { type ->
    def msg = messageClass.newInstance("compilationStarted")
    msg.compiler = "groovyc"
    msg.write(grailsConsole.&log)
}

eventCompileEnd = { type ->
    def msg = messageClass.newInstance("compilationFinished")
    msg.compiler = "groovyc"
    msg.write(grailsConsole.&log)
}

/**
 * Test Events
 */
eventTestSuiteStart = { name ->
    def msg = messageClass.newInstance("testSuiteStarted")
    msg.name = name
    msg.write(grailsConsole.&log)
}

eventTestSuiteEnd = { name ->
    def msg = messageClass.newInstance("testSuiteFinished")
    msg.name = name
    msg.write(grailsConsole.&log)
}

// No message in TeamCity for these, just need to get information for use in the other messages
def currentTestCaseName = ""
eventTestCaseStart = { testCaseName ->
    currentTestCaseName = testCaseName
}
eventTestCaseEnd = { testCaseName, out, err ->
    currentTestCaseName = ""
}

eventTestStart = { testName ->
    def msg = messageClass.newInstance("testStarted")
    msg.name = "${currentTestCaseName}.${testName}"
    msg.captureStandardOutput = true
    msg.write(grailsConsole.&log)
}

eventTestFailure = { testName, failure, isError ->
        def msg = messageClass.newInstance("testFailed")
        msg.name = "${currentTestCaseName}.${testName}"
        if (failure instanceof Throwable) {
            msg.message = failure.message
            msg.details = failure
        }
        else {
            msg.message = failure
            msg.details = failure
        }
        msg.write(grailsConsole.&log)
}

eventTestEnd = { testName ->
    def msg = messageClass.newInstance("testFinished")
    msg.name = "${currentTestCaseName}.${testName}"
    msg.write(grailsConsole.&log)
}

// Make sure the classes are loaded and compile if needed
def loadMessageClass(className) {
    def doLoad = {-> classLoader.loadClass("grails.teamcity.${className}") }
    try {
        doLoad()
    }
    catch (ClassNotFoundException ignored) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}

