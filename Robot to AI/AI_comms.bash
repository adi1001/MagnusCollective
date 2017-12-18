#!/bin/bash
#!/usr/bin/env python3


##
#This code is made for The Magnus Collective. 
#
#This main bash script controls all the sub-scripts including python scripts
#this script is the back-end of robot and AI interaction wherein data is transferred
#to the AI(cloud_vision) only after the blockchain transaction is done and balance is updated
##

#load the password file to ssh and obtain image from the main processor
#a temporary solution until the code is moved to the main processor

pass=$(cat /etc/pass) 

#Firebase monitoring and transaction function which sends an image to the cloud vision api
#only after the token transfer is done

monitoringfb()
{
  
	#requestfrom monitors the firebase flag continuously which is updated by the device requesting the data
	#in this case the Aido Robot	
    requestfrom=$(sudo python3 /home/pi/Aido/Robot_AI_blockchain/fbm.py)
	#output redirected to file for logs
    echo "$requestfrom" > requests
    echo "message $requestfrom"
	#when firebase flag is updated with anything other than null the following is performed
    if [ "$requestfrom" != "NULL" ]
    then
		
        b=1
		#get token balance of AI before transaction
        tok_bal=$(curl -s server_ip/getTokenValueAI.php?)
        echo "bal befor tx: $tok_bal"
		
		#execute the token transfer
        curl -s server_ip/Aido1toAItokentransfer.php 
		#an iteration counter variable
        n=1 
		
        while [ $b -eq 1 ];
        do
          n=$((n+1)) 
          echo "$n"    
		  #get token balance continuously from a php running at the server
          currenttokens=$(curl -s server_ip/getTokenValueAI.php?) 
          echo "bal: $tok_bal"
		  #if transaction(token transfer) is successful
          if [ $currenttokens -gt $tok_bal ]
          then
            echo "tx done... current bal: $currenttokens"
            break
          fi
        #waiting for transaction to happen, every iteration is approx 0.5 to 1 sec, breaks after iterations 
		#but still sends data to AI afterwards
          if [ $n -gt 50 ]
          then
            echo "working ..."
            break
          fi
        done
		
		sshpass -p $pass scp user@mainprr:/path/to/the/photo.jpg  /path/to/copy/the/photo.jpg
        #sending image to google-cloud-vision for scene analysis
        sudo python3 Scene_Recognition.py
		#results updated to firebase and program prepared for next request	
        ./IPFSDT 
       
    fi
  
}
# infinite loop for the function to run in background
a=1
while [ $a -eq 1 ]
do
      
  monitoringfb

done
