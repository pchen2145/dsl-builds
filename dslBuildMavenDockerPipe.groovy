// dslBuildMavenDockerPipe.groovy

import org.yaml.snakeyaml.Yaml

def gitUrl = "https://github.com/pchen2145/springbootmaven"

Yaml yaml = new Yaml()
def pipelineobj = yaml.load(("pipelinelist.yml" as File).text)

println pipelineobj

pipelineobj.names.each {

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