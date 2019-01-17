// DSL Build File for Maven build and Docker image

def gitUrl = "https://github.com/pchen2145/springbootmaven"

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
        maven('package', 'pom.xml')
        maven {
            goals('package')
            mavenOpts('-Xms256m')
            mavenOpts('-Xmx512m')
            localRepository(LocalRepositoryLocation.LOCAL_TO_WORKSPACE)
            properties(skipTests: true)
            mavenInstallation('Maven 3.6.0')
            providedSettings('central-mirror')
            rootPOM('./pom.xml')
        }
    }
}
