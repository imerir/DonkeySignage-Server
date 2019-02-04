pipeline {
    agent any

    stages {

        stage('Clone') { // for display purposes
            // Get some code from a GitHub repository
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
        stage('Gradle Build'){
            steps{
                script{
                    rtGradle = Artifactory.newGradleBuild()

                    buildInfo = rtGradle.run rootDir: ".", buildFile: 'build.gradle', tasks: 'build'
                }

            }

        }
        stage('Build Docker image') {
            /* This builds the actual image; synonymous to
             * docker build on the command line */
            steps{
                script{
                    app = docker.build("donkeysignage/donkeysignage-server",'--build-arg BUILD_NBR=${BUILD_NUMBER} --build-arg BRANCH_NAME=${BRANCH_NAME} --rm=true .')
                }

            }

        }
        /*stage('Push Docker image') {
            steps{
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker-hub-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
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
        }*/
        stage('Cleaning'){
            steps{
                sh "docker image prune -f"
            }

        }
    }
}

