
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
                echo 'CHECKOUT'
                checkout scm
            }
        }

        stage('Environment Check') {
            steps {
                sh '''
                    set -e
                    echo "===== JAVA VERSION ====="
                    java -version

                    echo "===== MAVEN VERSION ====="
                    mvn -version

                    echo "===== WORKSPACE ====="
                    pwd
                    ls -la
                '''
            }
        }

        stage('Build - Skip Tests') {
            steps {
                sh '''
                    set -e

                    echo "===== CLEAN BUILD ====="
                    mvn clean

                    echo "===== COMPILE AND PACKAGE WITHOUT TEST COMPILATION ====="
                    mvn -B package -Dmaven.test.skip=true

                    echo "===== BUILD COMPLETED ====="
                '''
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                catchError(
                    buildResult: 'SUCCESS',
                    stageResult: 'UNSTABLE'
                ) {
                    withCredentials([
                        string(
                            credentialsId: 'nvd-api-key',
                            variable: 'NVD_KEY'
                        )
                    ]) {
                        dependencyCheck(
                            odcInstallation: 'OWASP',
                            additionalArguments: "--nvdApiKey ${env.NVD_KEY} --format HTML --format CSV --out . --disableOssIndex"
                        )
                    }
                }
            }
        }

        stage('Publish OWASP Report') {
            steps {
                catchError(
                    buildResult: 'SUCCESS',
                    stageResult: 'UNSTABLE'
                ) {
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
                        sh '''
                            set -e

                            mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                                -Dsonar.projectKey="$SONAR_PROJECT_KEY" \
                                -Dsonar.projectName="$SONAR_PROJECT_NAME" \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                -Dsonar.token="$SONAR_TOKEN"
                        '''
                    }
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
                    set -e
                    mvn -B package -DskipTests -Dmaven.test.skip=true
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    set -e
                    docker build -t "$DOCKER_IMAGE:$DOCKER_TAG" .
                '''
            }
        }

        stage('Docker Tag Latest') {
            steps {
                sh '''
                    set -e
                    docker tag "$DOCKER_IMAGE:$DOCKER_TAG" "$DOCKER_IMAGE:latest"
                '''
            }
        }
    }

    post {
        always {
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
            echo 'CMS-SERVICE pipeline completed successfully.'
        }

        unstable {
            echo 'Pipeline is unstable. Check the OWASP Dependency Check results.'
        }

        failure {
            echo 'CMS-SERVICE pipeline failed. Check the Jenkins console logs.'
        }
    }
}
