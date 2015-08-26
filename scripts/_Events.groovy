/**
 * Packaging
 */
eventPackagingEnd = {
    def msg = loadMessageClass().newInstance("progressMessage")
    msg.text = "Packaging Complete"
    msg.write(grailsConsole.&log)
}

// Make the build number available via a TeamCity property
eventStatusFinal = {
    def msg = loadMessageClass().newInstance("setParameter")
    msg.name = "grails.app.version"
    msg.value = metadata.'app.version'
    msg.write(grailsConsole.&log)
}

/**
 * Test Events
 */
eventTestSuiteStart = { name ->
    def msg = loadMessageClass().newInstance("testSuiteStarted")
    msg.name = name
    msg.write(grailsConsole.&log)
}

eventTestSuiteEnd = { name ->
    def msg = loadMessageClass().newInstance("testSuiteFinished")
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
    def msg = loadMessageClass().newInstance("testStarted")
    msg.name = "${currentTestCaseName}.${testName}"
    msg.captureStandardOutput = true
    msg.write(grailsConsole.&log)
}

eventTestFailure = { testName, failure, isError ->
        def msg = loadMessageClass().newInstance("testFailed")
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
    def msg = loadMessageClass().newInstance("testFinished")
    msg.name = "${currentTestCaseName}.${testName}"
    msg.write(grailsConsole.&log)
}

// Make sure the classes are loaded and compile if needed
def loadMessageClass() {
    def doLoad = {-> classLoader.loadClass("grails.teamcity.ServiceMessage") }
    try {
        doLoad()
    }
    catch (ClassNotFoundException ignored) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}
