pipeline {
    agent any
    properties([
        // only keep 25 builds to prevent disk usage from growing out of control
        buildDiscarder(
            logRotator(
                artifactDaysToKeepStr: '',
                artifactNumToKeepStr: '',
                daysToKeepStr: '',
                numToKeepStr: '25',
                ),
        ),
    ])


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


        stage('Archive Artifacts') {
            steps {
                archiveArtifacts 'build/libs/*jar'
            }
        }

        stage('Javadoc') {
            steps {
                script {
                    sh "./gradlew javadoc"
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/docs/javadoc', reportFiles: 'overview-summary.html', reportName: 'Javadoc', reportTitles: ''])
                }

            }
        }

        stage('Build Docker image') {
            steps {
                script {
                    app = docker.build("donkeysignage/donkeysignage-server", '--build-arg BUILD_NBR=${BUILD_NUMBER} --build-arg BRANCH_NAME=${BRANCH_NAME} --rm=true .')
                }

            }
        }

        stage('Push Docker image') {
            /* Finally, we'll push the image with two tags:
             * First, the incremental build number from Jenkins
             * Second, the 'latest' tag.
             * Pushing multiple tags is cheap, as all the layers are reused. */
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerhub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                    sh 'docker login -u $USERNAME -p $PASSWORD'
                    script {
                        if (env.BRANCH_NAME == 'master') {
                            app.push("master")
                        } else {
                            app.push("devel")
                        }
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