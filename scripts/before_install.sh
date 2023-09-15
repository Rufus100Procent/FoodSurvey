#!/bin/bash
# installing docker, maven, java, x-ray, codedeploy-agent >> if not installed


cd /home/ec2-user/
# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Installing..."
    sudo yum install -y docker
    sudo service docker start
    sudo systemctl enable docker

    sudo usermod -aG docker ec2-user

    sudo chmod 666 /var/run/docker.sock

    wget https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -O /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
else
    echo "Docker is already installed."
fi


# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Installing..."
    sudo yum install -y java
else
    echo "Java is already installed."
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Installing..."
    sudo yum install -y maven
else
    echo "Maven is already installed."
fi



#x-ray deamon
if ! command -v xray &> /dev/null; then
    echo "AWS X-Ray daemon is not installed. Installing..."
    curl https://s3.eu-north-1.amazonaws.com/aws-xray-assets.eu-north-1/xray-daemon/aws-xray-daemon-3.x.rpm -o /home/ec2-user/xray.rpm
    sudo yum install -y /home/ec2-user/xray.rpm
    sudo systemctl start xray
    sudo systemctl enable xray
else
    echo "AWS X-Ray daemon is already installed."
fi
# Clean package cache
sudo yum clean all