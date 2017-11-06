pipeline { 
    agent any  
  
    stages { 
        stage('Build') { 
            steps { 
               sh 'mvn clean install'
            }
        }
        stage('Test') { 
            steps { 
               echo 'This is a minimal pipeline.' 
            }
        }
        stage('Deploy') { 
            steps { 
              
              sh 'docker-compose up' 
            }
        }

    }

}
