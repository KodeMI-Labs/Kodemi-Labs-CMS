pipeline {

    agent any

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-17'
        MAVEN_HOME = 'C:\\ProgramData\\chocolatey\\lib\\maven\\apache-maven-3.9.16'

        PATH = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;C:\\Windows\\System32;C:\\Windows"

        SONARQUBE = 'SonarQube2'
        SONAR_PROJECT_KEY = 'CMS-Service'
        SONAR_PROJECT_NAME = 'CMS-Service'

        DOCKER_IMAGE = 'kodemi-cms'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test with Coverage') {
            steps {
                bat '''
                    echo ===== JAVA VERSION =====
                    java -version

                    echo ===== MAVEN VERSION =====
                    mvn -version

                    echo ===== BUILD, TEST & COVERAGE =====
                    mvn clean verify org.jacoco:jacoco-maven-plugin:report

                    echo ===== JACOCO COVERAGE REPORT =====
                    if exist target\\site\\jacoco\\jacoco.xml (
                        echo JaCoCo XML coverage report generated successfully.
                    ) else (
                        echo ERROR: JaCoCo XML coverage report was not generated.
                        exit /b 1
                    )
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    bat '''
                        echo ===== SONARQUBE ANALYSIS =====
                        echo Project Key: %SONAR_PROJECT_KEY%
                        echo Project Name: %SONAR_PROJECT_NAME%
                        echo Coverage Report: target\\site\\jacoco\\jacoco.xml

                        mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar ^
                            -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                            -Dsonar.projectName=%SONAR_PROJECT_NAME% ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target\\site\\jacoco\\jacoco.xml
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
                bat '''
                    echo ===== PACKAGE =====
                    mvn package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                    echo ===== DOCKER BUILD =====
                    docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% .
                '''
            }
        }

        stage('Docker Tag Latest') {
            steps {
                bat '''
                    echo ===== DOCKER TAG =====
                    docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_IMAGE%:latest
                '''
            }
        }
    }

    post {

        success {
            echo 'CMS-Service CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'CMS-Service CI/CD pipeline failed. Check the Jenkins console logs.'
        }

        always {
            junit(
                allowEmptyResults: true,
                testResults: '**/target/surefire-reports/*.xml'
            )

            archiveArtifacts(
                artifacts: 'target/site/jacoco/**',
                allowEmptyArchive: true
            )
        }
    }
}
