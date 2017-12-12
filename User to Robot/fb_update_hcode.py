import firebase
import firebase_admin
from firebase import firebase
from firebase_admin import db
from firebase_admin import credentials

cred = credentials.Certificate("/home/pi/Aido/MadLIN/lircaidotest-firebase-adminsdk-9vikw-d32a1c578e.json")
firebase=firebase.FirebaseApplication('https://lircaidotest.firebaseio.com/')
firebase_admin.initialize_app(cred, {'databaseURL': 'https://lircaidotest.firebaseio.com/'})


reference = db.reference('/AidoIPFS')
child_reference = reference.child('Aido2')
child_reference.update({
     'hcodeinfo': ("NULL"),
       })
