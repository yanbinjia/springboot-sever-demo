#!/bin/bash

echo "count code lines" 
echo `date +"%Y-%m-%d %H:%M:%S"`
echo 'shell script;find . -name "*.java" -o -name "*.jsp" -o -name "*.xml" | grep -v test | xargs cat | wc -l'
count=`find . -name "*.java" -o -name "*.jsp" -o -name "*.xml" | grep -v test | xargs cat | wc -l`
echo "code_count:"$count
