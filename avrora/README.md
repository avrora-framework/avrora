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

* perform a [snapshot deloyment](http://central.sonatype.org/pages/apache-maven.html#performing-a-snapshot-deployment)

  1. increment verion number in *pom.xml*
  1. build and commit snapshot
 
             mvn clean deploy -DskipTests

  1. watch [snapshots](https://oss.sonatype.org/#nexus-search;quick~avrora-framework) at oss.sonatype.org 

* perform a [release deplyoment](http://central.sonatype.org/pages/apache-maven.html#performing-a-release-deployment-with-the-maven-release-plugin)

  1. increment verion number in *pom.xml*
  1. commit *pom.xml*

            git add pom.xml && git commit -m "pre release deployment commit" && git push
        
  1. build and commit snapshot and release:

            mvn release:clean release:prepare -Darguments="-DskipTests"
            mvn release:perform -Darguments="-DskipTests"

  1. commit newly changed *pom.xml*

            git add pom.xml && git commit -m "post release deployment commit" && git push

  1. watch [staging profiles](https://oss.sonatype.org/#stagingProfiles) and [snapshots](https://oss.sonatype.org/#nexus-search;quick~avrora-framework) at oss.sonatype.org 
  
* deploy to local repository

        mvn clean install -DskipTests
