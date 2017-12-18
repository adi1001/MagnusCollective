'''
	This code is developed for The Magnus Collective. 
	
	The below code retrieves data from firebase continuously using firebase-admin
	module, which assumes the path to json keyfile is pointed properly 
	Though future uses may see use of python-firebase at places so both modules are imported

'''

import firebase
import firebase_admin
from firebase import firebase
from firebase_admin import db
from firebase_admin import credentials


cred = credentials.Certificate("/path/to/Credentials.json")
firebase=firebase.FirebaseApplication('https://example.firebaseio.com/')
firebase_admin.initialize_app(cred, {'databaseURL': 'https://example.firebaseio.com/'})

#access the reference and child nodes 
reference = db.reference('/rflags')
child_reference = reference.child('rai')
#get the data from the node
request_from1 = child_reference.get()


print(request_from1)


                                                         
