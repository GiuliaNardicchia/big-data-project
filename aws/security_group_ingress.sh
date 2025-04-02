#!/usr/bin/env bash

SECURITY_GROUP="ElasticMapReduce-master"
PORT=22

EXISTS=$(aws ec2 describe-security-groups --group-names "$SECURITY_GROUP" \
    --query "SecurityGroups[?IpPermissions[?FromPort==\`$PORT\` && ToPort==\`$PORT\` && IpProtocol==\`tcp\` && IpRanges[?CidrIp=='0.0.0.0/0']]]" \
    --output text)

if [[ -n "$EXISTS" ]]; then
    echo  "EC2 security group for port $PORT already exists"
else
    echo "EC2 authorize security group ingress"
    aws ec2 authorize-security-group-ingress --group-name "$SECURITY_GROUP" \
        --ip-permissions IpProtocol=tcp,FromPort=$PORT,ToPort=$PORT,IpRanges="[{CidrIp=0.0.0.0/0}]"
fi