pipeline {
    agent any

    environment {
        DEPLOYMENT_NAME = "teedy-deployment"
        CONTAINER_NAME = "teedy-app"
        IMAGE_NAME = "claudiacumberbatch/teedy-app:24" // or your preferred version
        PATH = "/opt/homebrew/bin:/usr/local/bin:${PATH}"
    }

    stages {
        stage('Start Minikube') {
            steps {
                script {
                    // Check if minikube is running
                    if (!sh(returnStatus: true, script: 'minikube status | grep -q "Running"')) {
                        echo "Starting Minikube..."
                        sh 'minikube start'
                    } else {
                        echo "Minikube already running."
                    }
                }
            }
        }

        stage('Create/Update Deployment') {
            steps {
                script {
                    // Check if deployment exists
                    def deploymentExists = !sh(returnStatus: true, script: "/opt/homebrew/bin/kubectl get deployment ${DEPLOYMENT_NAME}")
                    
                    if (deploymentExists) {
                        echo "Updating existing deployment..."
                        sh "/opt/homebrew/bin/kubectl set image deployment/${DEPLOYMENT_NAME} ${CONTAINER_NAME}=${IMAGE_NAME}"
                    } else {
                        echo "Creating new deployment..."
                        sh "/opt/homebrew/bin/kubectl create deployment ${DEPLOYMENT_NAME} --image=${IMAGE_NAME}"
                    }
                }
            }
        }

        stage('Expose Service') {
            steps {
                script {
                    // Check if service exists
                    def serviceExists = !sh(returnStatus: true, script: "/opt/homebrew/bin/kubectl get service ${DEPLOYMENT_NAME}")
                    
                    if (!serviceExists) {
                        echo "Exposing deployment as service..."
                        sh "/opt/homebrew/bin/kubectl expose deployment ${DEPLOYMENT_NAME} --type=LoadBalancer --port=8080"
                    } else {
                        echo "Service already exists."
                    }
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                sh "/opt/homebrew/bin/kubectl rollout status deployment/${DEPLOYMENT_NAME}"
                sh "/opt/homebrew/bin/kubectl get pods"
                sh "/opt/homebrew/bin/kubectl get services"
            }
        }

        stage('Open Application') {
            steps {
                script {
                    // Try to open the service in browser
                    try {
                        sh "minikube service ${DEPLOYMENT_NAME}"
                    } catch (Exception e) {
                        echo "Failed to automatically open browser. Please access the service manually using:"
                        sh "minikube service ${DEPLOYMENT_NAME} --url"
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed. Check Kubernetes resources:"
            sh "/opt/homebrew/bin/kubectl get pods"
            sh "/opt/homebrew/bin/kubectl get services"
        }
    }
}