#!/bin/bash
#!/usr/bin/env python3
#This code is made for The Magnus Collective. 
#
#This script is responsible to convert the data from sensor value file into an
#ipfs hash and sends it over through the ipfs. this code assumes ipfs daemon is running
#which is called at startup in rc.local or platform equivalent



#This function encodes the data in a ipfs hash and stores the hash data in a file

InitializingIPFS()
{
  echo "Initializing IPFS"
  #encoding the data into an ipfs hash and storing the hash in a file for further use
  ipfs add /home/pi/Aido/Sensor/Sensorfhc.txt > /home/pi/Aido/Robot_IoT/Hashcode
  code=$(awk ' {print $2; exit} ' /home/pi/Aido/Robot_IoT/Hashcode)
  echo "converted $code"
  echo "$code" > /home/pi/Aido/Robot_IoT/Hashcode
}

UpdatingFB()
{
 InitializingIPFS 
 echo "Updating Firebase"
 #python script called to update firebase with the converted hash code
 sudo python3 /home/pi/Aido/Robot_IoT/fb_update.py
}


UpdatingFB





