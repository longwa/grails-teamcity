class TeamcityGrailsPlugin {
    def version = "1.0.4"
    def grailsVersion = "2.0 > *"
    def dependsOn = [:]
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "TeamCity Integration"
    def author = "Aaron Long"
    def authorEmail = "longwa@gmail.com"
    def description = 'Provides build status and test run integration with TeamCity'

    def documentation = "https://github.com/longwa/grails-teamcity"
    def license = "APACHE"
    def issueManagement = [ url: "https://github.com/longwa/grails-teamcity/issues" ]
    def scm = [ url: "https://github.com/longwa/grails-teamcity" ]
}
