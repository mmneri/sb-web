#!groovy​

properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]])
def utilities

stage('Checkout and Unit Test') {
    node {
    	git 'https://github.com/mmneri/sb-deploy.git'
      	utilities = load 'utilities.groovy'
        checkout scm
        def v = version()
        currentBuild.displayName = "${env.BRANCH_NAME}-${v}-${env.BUILD_NUMBER}"
        utilities.mvn "clean verify"
    }
}

stage('build') {
    node {
        utilities.mvn "clean package -DskipTests"
    }
}

stage('Create build output'){
    node {	    
	    // Archive the build output artifacts.
	    archiveArtifacts artifacts: 'target/*.war' , fingerprint: true
	    // junit '**/target/surefire-reports/TEST-*.xml'
    }
}

def branch_type = utilities.get_branch_type "${env.BRANCH_NAME}"
def branch_deployment_environment = utilities.get_branch_deployment_environment branch_type

if (branch_deployment_environment) {
    stage('deploy') {
        if (branch_deployment_environment == "prod") {
            timeout(time: 1, unit: 'DAYS') {
                input "Deploy to ${branch_deployment_environment} ?"
            }
        }
        node {
            echo "Deploying to ${branch_deployment_environment}"
            //TODO specify the deployment
        }
    }
}

def version() {
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    return matcher ? matcher[0][1] : null
}