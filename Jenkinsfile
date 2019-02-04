pipeline {
  agent any
  stages {
    stage('Clone') {
      steps {
        echo env.BRANCH_NAME
        script {
          if (env.BRANCH_NAME == 'master') {
            git url: 'https://github.com/imerir/DonkeySignage-Server.git', branch: 'master'
          } else {
            git url: 'https://github.com/imerir/DonkeySignage-Server.git', branch: 'devel'
          }
        }

      }
    }
    stage('Gradle Build') {
      steps {
        script {
          sh "./gradlew clean build"
        }

      }
    }
    stage('Build Docker image') {
      parallel {
        stage('Build Docker image') {
          steps {
            script {
              app = docker.build("donkeysignage/donkeysignage-server",'--build-arg BUILD_NBR=${BUILD_NUMBER} --build-arg BRANCH_NAME=${BRANCH_NAME} --rm=true .')
            }

          }
        }
        stage('Archive Artifacts') {
          steps {
            archiveArtifacts 'build/libs/*jar'
          }
        }
      }
    }
    stage('Cleaning') {
      steps {
        sh 'docker image prune -f'
      }
    }
  }
}