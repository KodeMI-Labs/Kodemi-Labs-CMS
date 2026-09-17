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
                    echo ========================================
                    echo JAVA VERSION
                    echo ========================================
                    java -version

                    echo ========================================
                    echo MAVEN VERSION
                    echo ========================================
                    mvn -version

                    echo ========================================
                    echo CLEAN BUILD, TESTS AND JACOCO COVERAGE
                    echo ========================================
                    mvn clean verify

                    if %ERRORLEVEL% NEQ 0 (
                        echo Maven build or tests failed.
                        exit /b %ERRORLEVEL%
                    )

                    echo ========================================
                    echo CHECKING JACOCO COVERAGE REPORT
                    echo ========================================

                    if exist "target\\site\\jacoco\\jacoco.xml" (
                        echo JaCoCo XML report FOUND.
                        echo Coverage report:
                        echo target\\site\\jacoco\\jacoco.xml
                    ) else (
                        echo ERROR: JaCoCo XML report NOT FOUND.
                        echo Expected:
                        echo target\\site\\jacoco\\jacoco.xml
                        exit /b 1
                    )

                    if exist "target\\site\\jacoco\\index.html" (
                        echo JaCoCo HTML report FOUND.
                    ) else (
                        echo WARNING: JaCoCo HTML report not found.
                    )
                '''
            }
        }

        stage('Verify Test Reports') {
            steps {
                bat '''
                    echo ========================================
                    echo VERIFYING SUREFIRE TEST REPORTS
                    echo ========================================

                    if exist "target\\surefire-reports" (
                        echo Surefire reports directory FOUND.
                        dir /s /b target\\surefire-reports
                    ) else (
                        echo WARNING: Surefire reports directory NOT FOUND.
                    )
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    bat '''
                        echo ========================================
                        echo SONARQUBE ANALYSIS
                        echo ========================================

                        echo Project Key: %SONAR_PROJECT_KEY%
                        echo Project Name: %SONAR_PROJECT_NAME%
                        echo Coverage Report:
                        echo target/site/jacoco/jacoco.xml

                        mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar ^
                            -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                            -Dsonar.projectName=%SONAR_PROJECT_NAME% ^
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
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
                    echo ========================================
                    echo PACKAGE
                    echo ========================================

                    mvn package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                    echo ========================================
                    echo DOCKER BUILD
                    echo ========================================

                    docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% .
                '''
            }
        }

        stage('Docker Tag Latest') {
            steps {
                bat '''
                    echo ========================================
                    echo DOCKER TAG
                    echo ========================================

                    docker tag %DOCKER_IMAGE%:%DOCKER_TAG% %DOCKER_IMAGE%:latest
                '''
            }
        }
    }

    post {

        always {
            echo 'Publishing test results and coverage reports...'

            junit(
                allowEmptyResults: true,
                testResults: '**/target/surefire-reports/*.xml'
            )

            archiveArtifacts(
                artifacts: 'target/site/jacoco/**',
                allowEmptyArchive: true
            )
        }

        success {
            echo 'CMS-Service CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'CMS-Service CI/CD pipeline failed. Check the Jenkins console logs.'
        }
    }
}
