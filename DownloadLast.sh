#!/bin/bash
#This script download the last stable build on jenkins
echo "Branch: "$1
if [[  $1 = "master"  ]]
then
	base_url="http://10.0.30.28:8080/job/DonkeySignage-Server-Gradle/lastStableBuild"
else
	base_url="http://10.0.30.28:8080/job/DonkeySignage-Server-Gradle/lastStableBuild/"
fi

data=$(curl -s -g ${base_url}"/api/xml?xpath=/freeStyleBuild/artifact&wrapper=artifacts")
relativePath=$(grep -oPm1 "(?<=<relativePath>)[^<]+" <<< "$data")
jarFile=$(grep -oPm1 "(?<=<fileName>)[^<]+" <<< "$data")



wget ${base_url}"/artifact/"${relativePath} -O app.jar -nv


