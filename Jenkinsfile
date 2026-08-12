pipeline {
    agent any

    environment {
        JAVA_HOME = '/opt/java/openjdk'
        MAVEN_HOME = '/usr/share/maven'
        PATH = "/opt/java/openjdk/bin:/usr/share/maven/bin:/usr/bin:/bin:/usr/local/bin"

        SONARQUBE = 'SonarQube'
        DOCKER_IMAGE = 'kodemi-cms'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh '''
                    echo "===== JAVA VERSION ====="
                    java -version

                    echo "===== MAVEN VERSION ====="
                    mvn -version

                    echo "===== BUILD & TEST ====="
                    mvn clean verify
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    sh '''
                        echo "===== SONARQUBE ANALYSIS ====="

                        mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh '''
                    echo "===== PACKAGE ====="
                    mvn package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    echo "===== DOCKER BUILD ====="
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                '''
            }
        }

        stage('Docker Tag Latest') {
            steps {
                sh '''
                    echo "===== DOCKER TAG ====="
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                '''
            }
        }
    }

    post {
        success {
            echo 'CMS CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'CMS CI/CD pipeline failed. Check the Jenkins console logs.'
        }

        always {
            junit(
                allowEmptyResults: true,
                testResults: '**/target/surefire-reports/*.xml'
            )
        }
    }
}
