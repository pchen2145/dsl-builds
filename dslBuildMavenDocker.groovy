// DSL Build File for Maven build and Docker image

def gitUrl = "https://github.com/pchen2145/springbootmaven.git"

job("dslBuildMavenDocker") {
    description "Build and package application and push image to Dockerhub"
    parameters {
        stringParam('docker-image-tag', 'latest', '')
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
            rootPOM(pom.xml)
            goals('package')

        }
    }

    publishers {
        downstream('ps-archive', 'SUCCESS')
    }
}

