// dslBuildMavenDocker.groovy

def gitUrl = "https://github.com/pchen2145/springbootmaven.git"

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
