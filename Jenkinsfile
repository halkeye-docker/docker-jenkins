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
                sh 'docker build -t halkeye/jenkins-docker --no-cache .'
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
                sh 'docker login --username $DOCKER_USR --password=$DOCKER_PSW'
                // sh 'docker push halkeye/jenkins-docker'
            }
        }
    }
}
