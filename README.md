TeamCity Integration
====================
TeamCity provides several test runners which automatically configure listeners for build events, test events, etc. With
these listeners, TeamCity provides real-time test results, test breakage history, and other niceties. Unfortunately, 
when spawning grails commands such as "grails test-app" the configured listeners aren't passed along
to the grails process. This causes the detailed build events to be lost and just generic "Success" and "Failure" messages
to be reported for each build.

Usage
-----
The plugin simply hooks into the Grails build event system to notify TeamCity of key events during the build and test
process. TeamCity has a Service Message API (http://confluence.jetbrains.net/display/TCD65/Build+Script+Interaction+with+TeamCity) 
which is parsed from the Build Log of a running process. Any service message detected in the log are stripped from the log
and processed by TeamCity for things such as test start and end, failures, etc.

With the new Grails wrapper functionality added in 2.1, it's even easier to setup TeamCity for running Grails commands.
Just create a build step with "Command" type and run _grailsw_, passing in options such as -Dgrails.env=test.


Configuration
-------------
The plugin is activated whenever the environment variable **TEAMCITY_VERSION** is present. This should already be
configured when grails is running inside of a TeamCity build agent so no configuration is necessary.

Events
------
The plugin currently generates the following TeamCity events:

* progressMessage - Indicates when packaging is complete
* testSuiteStarted - Each test phase is indicated as it's own suite (unit, integration, ...)
* testSuiteFinished
* testStarted - Notifies the start of each test case in a suite
* testFailed - Indicates test failure, stack traces are deep sanitized, escaped, and notified.
* testFinished - Test finished
                  
Compatibility
-------------
The plugin has been tested with TeamCity version 6.5 and 7.0.3 along with Grails 2.0.4 and Grails 2.1.0. It should work 
with older versions of TeamCity, but I haven't tested it.

TODO
----
Teamcity Sevice Messages allow rich notifications including things like: compilation, packaging, statistics such as total
test failures and success, publishing of artifacts as soon as they are generated (WAR files, etc.)

I will probably add these as they are useful.

