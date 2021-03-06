// dslBuildMavenDocker.groovy

def gitUrl = "https://github.com/pchen2145/springbootmaven"

job("dslBuildMavenDocker") {
    description "Build and package application and push image to Dockerhub"
    parameters {
        stringParam('dockertag', 'latest', '')
    }

    scm {
        git {
            remote {
                url gitUrl
                branch "*/master"
            }

            extensions {
                wipeOutWorkspace()
                localBranch "master"
            }
        }
    }

    steps {
        maven {
            goals('package')
            mavenInstallation('Maven-3.6.0')
            rootPOM('pom.xml')
        }

        dockerBuildAndPublish {
            repositoryName('pchen2145/springbootimage')
            tag('$dockertag')
            dockerHostURI('tcp://localhost:2375')
            registryCredentials('dockerhub')
            forcePull(true)
            createFingerprints(true)
            forceTag(false)
        }
    }

}
