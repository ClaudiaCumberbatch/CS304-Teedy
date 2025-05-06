pipeline {
  agent any
  environment {
    // Add Docker to PATH (macOS default location)
    PATH = "/usr/local/bin:${env.PATH}"

    // define environment variable
    // Use full path to Docker socket on macOS
    DOCKER_HOST = "unix://${env.HOME}/.docker/run/docker.sock"
    // Disable buildkit to avoid potential issues
    DOCKER_BUILDKIT = "1"

    // Jenkins credentials configuration
    DOCKER_HUB_CREDENTIALS = credentials('dockerhub_credentials') // Docker Hub credentials ID store in Jenkins
    // Docker Hub Repository's name
    DOCKER_IMAGE = 'claudiacumberbatch/teedy-app' // your Docker Hub user name and Repository's name
    DOCKER_TAG = "${env.BUILD_NUMBER}" // use build number as tag
  }
  tools {
      maven 'Maven 3' 
      // dockerTool 'Docker 27'
  }
  stages {
    stage('Verify Docker') {
      steps {
        script {
          // Print PATH for debugging
          sh 'echo $PATH'
          // Verify Docker access
          sh 'which docker'
          sh 'docker --version'
        }
      }
    }
    stage('Build') {
      steps {
        checkout scmGit(
        branches: [[name: '*/master']],
        extensions: [],
        userRemoteConfigs: [[url: 'https://github.com/ClaudiaCumberbatch/CS304-Teedy.git']]
        // your github Repository
        )
        sh 'mvn -B -DskipTests clean package'
      }
    }
    stage('Verify Docker Access') {
      steps {
        script {
            // Test Docker connectivity
            sh 'docker info'
            sh 'docker version'
        }
      }
    }
    // Building Docker images
    stage('Building image') {
      steps {
        script {
          // Explicitly use the Docker socket path
          withEnv(["DOCKER_HOST=unix://${env.HOME}/.docker/run/docker.sock"]) {
            sh "docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ."
          }
        }
      }
    }
    stage('Upload image') {
      // 将 withCredentials 直接应用于上传阶段
      steps {
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub_credentials',
            usernameVariable: 'DOCKER_HUB_USER',
            passwordVariable: 'DOCKER_HUB_PASSWORD'
        )]) {
          script {
            sh """
              docker login -u $DOCKER_HUB_USER -p $DOCKER_HUB_PASSWORD
              docker push ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}
              docker push -a ${env.DOCKER_IMAGE}
            """
          }
        }
      }
    }
    // Running Docker container
    stage('Run containers') {
      steps {
        script {
          // stop then remove containers if exists 
          sh 'docker stop teedy-container-8082 || true' 
          sh 'docker rm teedy-container-8082 || true' 
          sh 'docker stop teedy-container-8083 || true' 
          sh 'docker rm teedy-container-8083 || true' 
          sh 'docker stop teedy-container-8084 || true' 
          sh 'docker rm teedy-container-8084 || true' 
          // run Container 
          docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run( 
          '--name teedy-container-8082 -d -p 8082:8080' 
          ) 

          docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run( 
          '--name teedy-container-8083 -d -p 8083:8080' 
          )

          docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run( 
          '--name teedy-container-8084 -d -p 8084:8080' 
          ) 
          // Optional: list all teedy-containers 
          sh 'docker ps --filter "name=teedy-container"' 
        } 
      }
    }
  }
}
