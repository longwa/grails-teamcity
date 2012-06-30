eventTestSuiteStart = { name ->
    if( isEnabled() ) {
        def msg = loadMessageClass("ServiceMessage").newInstance("testSuiteStarted")
        msg.name = name
        grailsConsole.log msg.toString()
    }
}

eventTestSuiteEnd = { name ->
    if( isEnabled() ) {
        def msg = loadMessageClass("ServiceMessage").newInstance("testSuiteEnded")
        msg.name = name
        grailsConsole.log msg.toString()
    }
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
    if( isEnabled() ) {
        def msg = loadMessageClass("ServiceMessage").newInstance("testStarted")
        msg.name = "${currentTestCaseName}.${testName}"
        grailsConsole.log msg.toString()
    }
}

eventTestFailure = { testName, failure, isError ->
    if( isEnabled() ) {
        def msg = loadMessageClass("ServiceMessage").newInstance("testFailed")
        msg.name = "${currentTestCaseName}.${testName}"
        if( failure instanceof Throwable ) {
            msg.message = failure.message
            msg.details = failure
        }
        else {
            msg.message = failure
            msg.details = failure
        }
        grailsConsole.log msg.toString()
    }
}

eventTestEnd = { testName ->
    if( isEnabled() ) {
        def msg = loadMessageClass("ServiceMessage").newInstance("testFinished")
        msg.name = "${currentTestCaseName}.${testName}"
        grailsConsole.log msg.toString()
    }
}

// Make sure the classes are loaded and compile if needed
loadMessageClass = { className ->
    def doLoad = {-> classLoader.loadClass("grails.teamcity.${className}") }
    try {
        doLoad()
    }
    catch(ClassNotFoundException ignored) {
        includeTargets << grailsScript("_GrailsCompile")
        compile()
        doLoad()
    }
}

isEnabled = {
    System.getProperty("teamcity.version") != null
}

