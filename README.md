Deployment workflow
====================
The [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) 
workflow in short:

1. if you have no account [sign up](https://issues.sonatype.org/secure/Signup!default.jspa)
1. create a project [ticket](http://central.sonatype.org/pages/ossrh-guide.html#create-a-ticket-with-sonatype)
1. follow the maven [deployment](http://central.sonatype.org/pages/apache-maven.html) example

Set up ossrh username and password
==================================
Add *orssh* **server** tag to **servers** to your **settings.xml**:

        <?xml version="1.0" encoding="UTF-8"?>
        <settings xmlns="http://maven.apache.org/POM/4.0.0" 
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
          <servers>
            <server>
              <id>ossrh</id>
              <username>your-jira-id</username>
              <password>your-jira-pwd</password>
            </server>
          </servers>
        </settings>


**your-jira-id** and **your-jira-pwd** can be obtained as follows:

1. log in to [https://oss.sonatype.org](https://oss.sonatype.org)
1. profile => user token => access user token

Deployment
==========

* [snapshot](http://central.sonatype.org/pages/apache-maven.html#performing-a-snapshot-deployment)

        mvn clean deploy

* [release](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin)

        mvn release:clean release:prepare
        mvn release:perform
