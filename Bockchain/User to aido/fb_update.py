'''
	This code was developed for Ingen Dynamics Inc. for use in Aido Robot
	
	The below code updates the firebase trigger flag to null indicating that the all instructions are executed
	and maked the program ready to be triggered again. It also updates a certain firebase node with
	hash code generated through ipfs so that the android front end can access the data.

'''
import firebase
import firebase_admin
from firebase import firebase
from firebase_admin import db
from firebase_admin import credentials

#point to credentials file to initiate firebase_admin
cred = credentials.Certificate("/path/to/credentials.json")
#firebase=firebase.FirebaseApplication('https://example.firebaseio.com/')
firebase_admin.initialize_app(cred, {'databaseURL': 'https://example.firebaseio.com/'})

#open file containing hash code data
file = open("/home/pi/Aido/User_Robot/Hashcode","r")
hash_code = file.read()
print(hash_code)


#define firebase child and reference to be updated and update it with the image url
reference = db.reference('/rflags')
child_reference = reference.child('user_data')
child_reference.update({
         'hcodeinfo': ('https://ipfs.io/ipfs/'+hash_code),
            })
#a transaction status flag to display status in the front end
reference = db.reference('/rflags')
child_reference = reference.child('user_data')
child_reference.update({
         'txn_staus': 'done'
            })

#Set firebase trigger flag to NULL			
reference = db.reference('/rflags')
reference.update({
         'ruser': 'NULL',
            })

print('firebase updated with value from Aido')    
    
    
