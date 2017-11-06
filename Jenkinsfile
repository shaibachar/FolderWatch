pipeline { 
    agent any  
  
    stages { 
        stage('Build') { 
            steps { 
               sh 'sudo mvn clean install -e'
            }
        }
        stage('Test') { 
            steps { 
               echo 'This is a minimal pipeline.' 
            }
        }
        stage('Deploy') { 
            steps { 
              
              sh 'sudo docker-compose up' 
            }
        }

    }

}
