// dslBuildMavenDockerPipe.groovy

def gitUrl = "https://github.com/pchen2145/springbootmaven"

def pipelineList = readYaml file: "pipelinelist.yml"

println pipelineList

pipelineList.each {

    job("${it}") {
        description ("${it}")
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
}