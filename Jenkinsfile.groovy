pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                echo '##Getting the code from the Github##'
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CleanBeforeCheckout']], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/AvinashK13/cdac-demo']]])
            }
        }
        stage('Build Demo Application') {
            steps {
                echo '##Building the Demo application##'
                sh '''
                    rm -rf /home/ubuntu/bkp/*
                    cp $WORKSPACE/demo.js /home/ubuntu/bkp/
                '''
            }
        }
                stage('Build Approval') {
            steps {
              input "Do you want to proceed for build ?"
            }
        }
        stage('Deploy Demo Application on Web node') {
            steps {
                echo '##Deploying Demo Application on Web node##'
                sh '''
                    cd /opt/ansible_script/plays/
                    ansible-playbook cdacdemo.yml
                '''
            }
        }
    }
 }
