pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven'
    }

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
    }

    environment {
        SONARQUBE = 'sonarscanner'
        SONAR_PROJECT_KEY = 'CMS-Service'
        SONAR_PROJECT_NAME = 'CMS-Service'

        DOCKER_IMAGE = 'kodemi-cms'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '========================================'
                echo 'CHECKOUT'
                echo '========================================'

                checkout scm
            }
        }

        stage('Environment Check') {
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
                    echo WORKSPACE
                    echo ========================================
                    echo %WORKSPACE%
                '''
            }
        }

        stage('Build & Test with Coverage') {
            steps {
                bat '''
                    echo ========================================
                    echo CLEAN BUILD
                    echo ========================================

                    mvn clean
                    if errorlevel 1 exit /b 1

                    echo ========================================
                    echo PREPARING JACOCO AGENT
                    echo ========================================

                    mvn org.jacoco:jacoco-maven-plugin:prepare-agent
                    if errorlevel 1 exit /b 1

                    echo ========================================
                    echo RUNNING TESTS
                    echo ========================================

                    mvn test
                    if errorlevel 1 exit /b 1

                    echo ========================================
                    echo GENERATING JACOCO REPORT
                    echo ========================================

                    mvn org.jacoco:jacoco-maven-plugin:report
                    if errorlevel 1 exit /b 1

                    echo ========================================
                    echo VERIFYING SUREFIRE REPORTS
                    echo ========================================

                    if exist "target\\surefire-reports" (
                        echo Surefire reports directory FOUND.
                        dir /s /b target\\surefire-reports
                    ) else (
                        echo ERROR: Surefire reports directory NOT FOUND.
                        exit /b 1
                    )

                    echo ========================================
                    echo VERIFYING JACOCO XML REPORT
                    echo ========================================

                    if exist "target\\site\\jacoco\\jacoco.xml" (
                        echo JaCoCo XML report FOUND.
                        dir "target\\site\\jacoco\\jacoco.xml"
                    ) else (
                        echo ERROR: JaCoCo XML report NOT FOUND.
                        exit /b 1
                    )

                    echo ========================================
                    echo VERIFYING JACOCO HTML REPORT
                    echo ========================================

                    if exist "target\\site\\jacoco\\index.html" (
                        echo JaCoCo HTML report FOUND.
                    ) else (
                        echo WARNING: JaCoCo HTML report was not generated.
                    )
                '''
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    withCredentials([
                        string(
                            credentialsId: 'nvd-api-key',
                            variable: 'NVD_KEY'
                        )
                    ]) {
                        echo '========================================'
                        echo 'OWASP DEPENDENCY CHECK'
                        echo '========================================'

                        dependencyCheck(
                            odcInstallation: 'OWASP',
                            additionalArguments: """
                                --nvdApiKey ${NVD_KEY}
                                --format HTML
                                --format CSV
                                --out .
                                --disableOssIndex
                            """
                        )
                    }
                }
            }
        }

        stage('Publish OWASP Report') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    dependencyCheckPublisher(
                        pattern: 'dependency-check-report.csv'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE}") {
                    withCredentials([
                        string(
                            credentialsId: 'sonartk',
                            variable: 'SONAR_TOKEN'
                        )
                    ]) {
                        bat '''
                            echo ========================================
                            echo SONARQUBE ANALYSIS
                            echo ========================================

                            echo Project Key: %SONAR_PROJECT_KEY%
                            echo Project Name: %SONAR_PROJECT_NAME%

                            mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar ^
                                -Dsonar.projectKey=%SONAR_PROJECT_KEY% ^
                                -Dsonar.projectName=%SONAR_PROJECT_NAME% ^
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml ^
                                -Dsonar.token=%SONAR_TOKEN%

                            if errorlevel 1 (
                                echo ERROR: SonarQube analysis failed.
                                exit /b 1
                            )
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo '========================================'
                echo 'WAITING FOR SONARQUBE QUALITY GATE'
                echo '========================================'

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

                    if errorlevel 1 (
                        echo ERROR: Maven package failed.
                        exit /b 1
                    )
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

                    if errorlevel 1 (
                        echo ERROR: Docker image build failed.
                        exit /b 1
                    )
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

                    if errorlevel 1 (
                        echo ERROR: Docker tag failed.
                        exit /b 1
                    )
                '''
            }
        }
    }

    post {
        always {
            echo '========================================'
            echo 'PUBLISHING TEST AND COVERAGE REPORTS'
            echo '========================================'

            junit(
                allowEmptyResults: true,
                testResults: '**/target/surefire-reports/*.xml'
            )

            archiveArtifacts(
                artifacts: '**/target/site/jacoco/**,dependency-check-report.*',
                allowEmptyArchive: true,
                fingerprint: true
            )
        }

        success {
            echo '========================================'
            echo 'CMS-SERVICE PIPELINE COMPLETED SUCCESSFULLY'
            echo '========================================'
        }

        unstable {
            echo 'Pipeline completed with an unstable stage. Check the OWASP Dependency Check results.'
        }

        failure {
            echo '========================================'
            echo 'CMS-SERVICE PIPELINE FAILED'
            echo 'Check Jenkins console logs for the exact failure.'
            echo '========================================'
        }
    }
}
