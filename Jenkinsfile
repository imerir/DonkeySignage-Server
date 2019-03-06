pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }


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
        stage('Webhook') {
            steps {
                script {
                    sh "curl -X POST https://webhook.site/f02282c4-f339-4299-818d-faff2f3214fb"
                }

            }
        }
    }
}