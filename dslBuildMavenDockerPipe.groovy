// dslBuildMavenDockerPipe.groovy

import org.yaml.snakeyaml.Yaml

def gitUrl = "https://github.com/pchen2145/springbootmaven"

// Add yaml class to parse yaml file
//Yaml yaml = new Yaml()
//InputStream inputStream = this.getClass()
// .getClassLoader()  
// .getResourceAsStream("pipelinelist.yml")
//Map pipelineobj = yaml.load(inputStream)
def pipelineobj = yaml.load(new File("${env.WORKSPACE}/pipelinelist.yml").text)

println pipelineobj

// Loop through names object in yaml file and create pipelines for each
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
