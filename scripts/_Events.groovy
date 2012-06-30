import grails.teamcity.ServiceMessage

eventTestSuiteStart = { name ->
    def message = new ServiceMessage("testSuiteStarted")
    message.attributes.name = name
    println message.toString()
}
