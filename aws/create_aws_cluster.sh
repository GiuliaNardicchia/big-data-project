#!/usr/bin/env bash

read -p "Enter AWS PROFILE:" AWS_PROFILE
read -p "Enter KEY PAIR NAME:" KEY_PAIR_NAME

# echo "EC2 authorize security group ingress"
# aws ec2 authorize-security-group-ingress --group-name ElasticMapReduce-master --ip-permissions IpProtocol=tcp,FromPort=22,ToPort=22,IpRanges="[{CidrIp=0.0.0.0/0}]"

export AWS_PROFILE="$AWS_PROFILE"

echo "EMR Create cluster"
CLUSTER_ID=$(aws emr create-cluster \
    --name "Big Data Cluster" \
    --release-label "emr-7.3.0" \
    --applications Name=Hadoop Name=Spark \
    --instance-groups InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m4.large InstanceGroupType=CORE,InstanceCount=6,InstanceType=m4.large \
    --service-role EMR_DefaultRole \
    --ec2-attributes InstanceProfile=EMR_EC2_DefaultRole,KeyName="${KEY_PAIR_NAME}" \
    --region "us-east-1" \
    --query 'ClusterId' \
    --output text)
echo "Cluster ID: $CLUSTER_ID"

echo "Waiting for the cluster to be ready"
while true; do
    STATUS=$(aws emr describe-cluster --cluster-id "$CLUSTER_ID" --query 'Cluster.Status.State' --output text)
    if [[ "$STATUS" == "WAITING" ]]; then
        echo "Cluster status: $STATUS"
        break
    fi
    echo "Cluster status: $STATUS. I will try again in 10 seconds"
    sleep 10
done

PUBLIC_DNS=$(aws emr list-instances --cluster-id "$CLUSTER_ID" --instance-group-type MASTER --query 'Instances[0].PublicDnsName' --output text)
echo "Public DNS: $PUBLIC_DNS"

# aws emr list-clusters --max-items 1
# aws emr list-instances --cluster-id <cluster_id> --instance-group-type MASTER