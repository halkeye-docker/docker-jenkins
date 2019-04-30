def response = httpRequest 'https://updates.jenkins.io/stable/latestCore.txt'
def version = new Date().format("yyMMdd.HHmm", TimeZone.getTimeZone('UTC'))

properties([[$class: 'GithubProjectProperty', displayName: '', projectUrlStr: 'https://github.com/halkeye/jenkins-docker/']])

pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
        ansiColor('xterm')
    }
    stages {
        stage('Build') {
            steps {
                sh """
                docker build \
                    -t halkeye/jenkins:${response.content}_${version} \
                    --build-arg JENKINS_VERSION=${response.content} \
                    --no-cache .
                """
            }
        }

        stage('Deploy') {
            when {
                branch 'master'
            }
            environment {
                DOCKER = credentials('dockerhub-halkeye')
            }
            steps {
                sh """
                  docker login --username $DOCKER_USR --password=$DOCKER_PSW
                  docker push halkeye/jenkins:${response.content}_${version}
                  docker tag halkeye/jenkins:${response.content}_${version} halkeye/jenkins:latest
                  docker push halkeye/jenkins:latest
                """
            }
        }
    }
}
