#!groovy​

properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

def utilities
def appname = "sb-web"
def downstreamJob = "sb-update-manifest"
def v = 0
def BRANCH_NAME = ""
if(env.BRANCH_NAME){
    BRANCH_NAME = "${env.BRANCH_NAME}"
}

stage('Checkout') {
    node {
    	git 'https://github.com/mmneri/sb-deploy.git'
      	utilities = load 'utilities.groovy'  
        scmVars = checkout scm
        // scmVars contains the following values
        // GIT_BRANCH=origin/mybranch
        // GIT_COMMIT=fc8279a107ebaf806f2e310fce15a7a54238eb71
        // GIT_PREVIOUS_COMMIT=6f2e319a1fc82707ebaf800fce15a7a54238eb71
        // GIT_PREVIOUS_SUCCESSFUL_COMMIT=310fce159a1fc82707ebaf806f2ea7a54238eb71
        // GIT_URL= 	
	
	// si env.BRANCH_NAME return null    
	if(BRANCH_NAME == ""){
	    branchName = utilities.getBranchName()
	    if(!branchName){
		gitBranch = "${scmVars.GIT_BRANCH}"
		gitBranch = gitBranch.replace("origin/", "")        
	    	BRANCH_NAME = "${gitBranch}"
	    } else {
		BRANCH_NAME = "${branchName}" 
	    }
	}
        utilities.log "BRANCH_NAME" , "${BRANCH_NAME}"    
        v = version()
        currentBuild.displayName = "${BRANCH_NAME}-${v}-${env.BUILD_NUMBER}"        
    }
}

stage('Unit Test') {
    node {
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

stage('Trigger Release Build') {
       build job: downstreamJob, parameters: [[$class: 'StringParameterValue', name: "app", value: "${appname}/${BRANCH_NAME}"], [$class: 'StringParameterValue', name: 'revision', value: v]], wait: false
}

def version() {
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    return matcher ? matcher[0][1] : null
}
