#!/bin/bash
#!/usr/bin/env python3

##
#This code is made for The Magnus Collective. 
#
#This script is responsible to convert the image taken from Aido's camera into an
#ipfs hash and sends it over through the ipfs. this code assumes ipfs daemon is running
#which is called at startup in rc.local or platform equivalent
##


#This function encodes the data in a ipfs hash and stores the hash data in a file
InitializingIPFS()
{
  echo "Initializing IPFS"
  #encoding the data into an ipfs hash and storing the hash in a file for further use
  ipfs add photo.jpg > Hashcode
  code=$(awk ' {print $2; exit} ' Hashcode)
  echo "converted $code"
  #echo "$code" > Hashcode
}

UpdatingFB()
{
 InitializingIPFS 
 echo "Updating Firebase"
 #python script called to update firebase with the converted hash code
 sudo python3 /path/to/fb_update.py
}



UpdatingFB
