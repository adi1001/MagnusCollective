#!/usr/bin/env python
#
#This code was developed for The Magnus Collective.
#The below code updates the firebase trigger flag to null indicating that the all instructions are executed
#and maked the program ready to be triggered again. It also updates a certain firebase node with
#hash code generated through ipfs so that the android front end can access the data.

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
child_reference = reference.child('riot')
#get the data from the node
request_from1 = child_reference.get()


print(request_from1)


                                                         
