#!/usr/bin/env python
#
#This code was developed for The Magnus Collective
#The below code updates the firebase trigger flag to null indicating that the all instructions are executed
#and maked the program ready to be triggered again. It also updates a certain firebase node with
#hash code generated through ipfs so that the android front end can access the data.

import firebase
import firebase_admin
from firebase import firebase
from firebase_admin import db
from firebase_admin import credentials

#point to credentials file to initiate firebase_admin
cred = credentials.Certificate("/path/to/credentials.json")
firebase=firebase.FirebaseApplication('https://example.firebaseio.com/')
firebase_admin.initialize_app(cred, {'databaseURL': 'https://example.firebaseio.com/'})

reference = db.reference('/AidoIPFS')
child_reference = reference.child('Aido2').child('requestfrom')
request_from = child_reference.get()
print("The request is from")
print(request_from)

#open file containing hash code data
file = open("/path/to/Hashcode","r")
hash_code = file.read()
print(hash_code)


#define firebase child and reference to be updated and update it with the hash code
reference = db.reference('/rflags')
child_reference = reference.child('IoT_data')
child_reference.update({
         'hcodeinfo': ('https://ipfs.io/ipfs/'+hash_code),
            })

#Set firebase trigger flag to NULL			
reference = db.reference('/rflags')
reference.update({
         'riot': 'NULL',
            })

print('firebase updated with value from Aido')    
    
    
