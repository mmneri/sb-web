#!groovy​
// BuildDiscarderProperty since Jenkins core 2.147 API
// properties([[$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '10']]])

def utilities
def appname = "sb-web"
def downstreamJob = "sb-update-manifest"
def v = 0
def BUILD_URL = ""
def GIT_COMMIT = ""
def BRANCH_TYPE = ""
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
			gitBranch = "${scmVars.GIT_BRANCH}"
			gitBranch = gitBranch.replace("origin/","")        
			BRANCH_NAME = "${gitBranch}"
		}
        utilities.log "BRANCH_NAME" , "${BRANCH_NAME}"  
        BRANCH_TYPE = utilities.getBranchType("${BRANCH_NAME}")
        BUILD_URL = "${scmVars.GIT_URL}"
		GIT_COMMIT = "${scmVars.GIT_COMMIT}"    
        versions = majorVersion()
        major = versions[1]
        minor = versions[2]
        patch = versions[3]
        v = "${major}.${minor}.${patch}"
        currentBuild.displayName = "${BRANCH_TYPE}-${v}-${env.BUILD_NUMBER}" 
        echo "WORKSPACE = ${env.WORKSPACE}"  
        v = "${v}-${env.BUILD_NUMBER}"
		stash exclude: 'target/', include: '**', name: 'source'    
    }
}

stage('Unit Test') {
    node {  
	unstash 'source'        
        try {
            utilities.mvn "clean test -Dmaven.test.failure.ignore=true"
        } finally {
            junit 'target/surefire-reports/**/*.xml'
        }
    }
}

stage('Build') {
    node {
        unstash 'source'        
        try {
		    utilities.mvn "clean package -DskipTests -DBUILD_NUMBER=${env.BUILD_NUMBER} -DBUILD_URL=${BUILD_URL} -DGIT_COMMIT=${GIT_COMMIT}"
		} finally {
            // Archive the build output artifacts.
			archiveArtifacts artifacts: 'target/*.war' , fingerprint: true
        }
    }
}

if(BRANCH_TYPE == "dev") {
	stage('Trigger Release Build') {
	       build job: downstreamJob, parameters: [[$class: 'StringParameterValue', name: "app", value: "${appname}/${BRANCH_TYPE}"], [$class: 'StringParameterValue', name: 'revision', value: v]], wait: false
	}
}

def version() {
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    return matcher ? matcher[0][1] : null
}

def majorVersion() {
    def matcher = readFile('pom.xml') =~ '<version>(\\d*)\\.(\\d*)\\.(\\d*)(-.*)*</version>'
    matcher ? matcher[0] : null
}