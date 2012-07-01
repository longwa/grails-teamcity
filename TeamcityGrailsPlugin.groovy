class TeamcityGrailsPlugin {
    def version = "1.0.2"
    def grailsVersion = "2.0 > *"
    def dependsOn = [:]
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Teamcity Integration" // Headline display name of the plugin
    def author = "Aaron Long"
    def authorEmail = "longwa@gmail.com"
    def description = 'Provides build status and test run integration with TeamCity'

    def documentation = "http://grails.org/plugin/teamcity"
    def license = "APACHE"
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]
    def scm = [ url: "fixme" ]
}
