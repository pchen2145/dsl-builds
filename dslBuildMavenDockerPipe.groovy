// dslBuildMavenDockerPipe.groovy

def gitUrl = "https://github.com/pchen2145/springbootmaven"
def pipelineList = ["Pipeline 1", "Pipeline2", "Pipeline3"]

println pipelineList

//pipelineList.each {

    job("Pipeline1") {
        description "Pipeline" {
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
//}
