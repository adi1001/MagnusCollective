#!/bin/bash
#!/usr/bin/env python3


##
#This code is made for The Magnus Collective. 
#
#This main bash script controls all the sub-scripts including python scripts
#this script is the back-end of robot2 and robot1 interaction wherein data is transferred
#to robot1 front end only after the blockchain transaction is done and balance is updated
##

#load the password file to ssh and obtain image from the main processor
#a temporary solution until the code is moved to the main processor

ip=$(sudo python3 /home/path/filename.py) 
sshpass -p password scp user@$ip:/home/user/image.jpg  /home/User/image.jpg
echo "image acquired"


check_bal()
{
  b=1
  n=1
  while [ $b -eq 1 ]
  do
    n=n+1    
    currenttokens=$(curl -s severIP/getTokenValue.php?)
    if [ $currenttokens -ge $tok_bal ]
    then
      break
    fi
  
    if [ $n -ge 250 ]
    then
      break
    fi
  done  
}

#Firebase monitoring and transaction function which sends an image to robot1
#only after the token transfer is done

monitoringfb()
{
  	#requestfrom monitors the firebase flag continuously which is updated by the device requesting the data
	#in this case the Aido Robot	
    requestfrom=$(sudo python3 /home/pi/Aido/Robot_Robot_blockchain/fbm.py)
    #output redirected to file for logs
    echo "$requestfrom" > /home/pi/Aido/Robot_Robot_blockchain/requests
    echo "message $requestfrom"
    #when firebase flag is updated with anything other than null the following is performed
    if [ "$requestfrom" != "NULL" ]
    then
        
        b=1
        #get token balance of robot2 before transaction
        n=1
        tok_bal1=$(curl -s severIP/getTokenValueAido1.php?)
        echo " hum bal: $tok_bal1"
        #execute the token transfer
        tok_bal=$(curl -s severIP/getTokenValueAido2.php?)
        echo "balance before tx: $tok_bal"
        curl -s severIP/Aido1toAido2tokentransfer.php?
        #an iteration counter variable
        echo "initiating token transfer" > /home/pi/Aido/Robot_AI_blockchain/status
        sudo python3 /home/pi/Aido/Robot_AI_blockchain/fb_update_hcode.py
        
        while [ $b -eq 1 ]
        do
          n=$((n+1))
          echo "$n" 
          #get token balance continuously from a php running at the server   
          currenttokens=$(curl -s severIP/getTokenValueAido2.php?)
          echo "bal: $tok_bal"
          #if transaction(token transfer) is successful
          if [ $currenttokens -gt $tok_bal ]
          then
            echo "txn done... current bal: $currenttokens"
            echo "token transfer done, txn successful" > /home/pi/Aido/Robot_AI_blockchain/status
            sudo python3 /home/pi/Aido/Robot_AI_blockchain/fb_update_hcode.py
            ip=$(sudo python3 /home/pi/Aido/fb_comms.py)  
            sshpass -p sks123 scp sarath@$ip:/home/sarath/new/bin/gray.jpg  /home/pi/Aido/User_Robot/photo.jpg
            curl -s $ip/gray.jpg > /home/pi/Aido/Robot_Robot_blockchain/photo.jpg
            /home/pi/Aido/Robot_Robot_blockchain/IPFSDT

            break
          fi
        #waiting for transaction to happen, every iteration is approx 0.5 to 1 sec, breaks after iterations 
		#but still sends data to AI afterwards       
          if [ $n -gt 150 ]
          then
            echo "working ..."
            echo "txn not done! error, pls check balances and try again" > /home/pi/Aido/Robot_AI_blockchain/status
            sudo python3 /home/pi/filename.py
            sudo python3 /home/pi/filename.py
            break
          fi
        done 
        tok_bal1=$(curl -s severIP/getTokenValueAido2.php?)

        echo "bal after: $tok_bal1"
       
        
        
    fi
  
}

# infinite loop for the function to run in background
a=1
while [ $a -eq 1 ]
do
      
  monitoringfb

done
