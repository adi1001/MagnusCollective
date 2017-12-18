#!/bin/bash
#!/usr/bin/env python3

##
#This code is made for The Magnus Collective. 
#
#This main bash script controls all the sub-scripts including python scripts
#this script is the back-end of robot and user interaction wherein data is transferred
#to the Aido Robot only after the blockchain transaction is done and balance is updated
#It is currently assumed that the transaction is successful and a mechanism to skip data
#transfer when transaction fails is yet to be developed
##

#load the password file to ssh and obtain image from the main processor
#a temporary solution until the code is moved to the main processor


pass=$(cat /etc/pass) 


#Firebase monitoring and transaction function which updates firebase flag with hash code of image
#only after the token transfer is done

monitoringfb()
{
	#requestfrom monitors the firebase flag continuously which is updated by the device requesting the data
	#in this case the user
    requestfrom=$(sudo python3 /home/pi/Aido/User_Robot/fbm.py)
    #output redirected to file for logs
    echo "$requestfrom" > /home/pi/Aido/User_Robot/requests
    echo "message $requestfrom"
	#when firebase flag is updated with anything other than null the following is performed
    if [ "$requestfrom" != "NULL" ]
    then
	
        b=1
        
		#get token balance of robot before transaction
        tok_bal1=$(curl -s server_ip/getTokenValueHuman.php?)
        echo " hum bal: $tok_bal1"
        tok_bal=$(curl -s server_ip/getTokenValueAido1.php?)
        echo "balance before tx: $tok_bal"
		#execute the token transfer
        curl -s server_ip/HumantoAido1tokentransfer.php?
		#an iteration counter variable
        n=1
        while [ $b -eq 1 ]
        do
          n=$((n+1))
          echo "$n"    
		  #get token balance continuously from a php running at the server
          currenttokens=$(curl -s server_ip/getTokenValueAido1.php?)
          echo "bal: $tok_bal"
		  #if transaction(token transfer) is successful
          if [ $currenttokens -gt $tok_bal ]
          then
            echo "checking... current bal: $currenttokens"
            break
          fi
          #waiting for transaction to happen, every iteration is approx 0.5 to 1 sec, breaks after iterations 
		  #but still sends data to AI afterwards
          if [ $n -gt 15 ]
          then
            echo "working ..."
            break
          fi
        done 
        tok_bal1=$(curl -s server_ip/getTokenValueAido1.php?)
		
        echo "bal after: $tok_bal1"
        ip=$(sudo python3 /home/pi/Aido/fb_comms.py)  
        #obtaining the image from main processor
		sshpass -p $pass scp user@$ip:/path/to/the/photo.jpg  /path/to/copy/the/photo.jpg
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
