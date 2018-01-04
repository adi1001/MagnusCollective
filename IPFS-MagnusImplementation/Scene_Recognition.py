
#	This code was developed for The Magnus Collective
'''	
	The below code sends an image to the google cloud after reading it as a file buffer
	The results from the cloud_vision api are retrieved and written into a text file

'''
from google.cloud import vision
from google.cloud.vision import types
import json
import os
import sys
from oauth2client.client import GoogleCredentials
import googleapiclient.discovery
import time
from PIL import Image,ImageDraw,ImageFont
import numpy as np

#time measurement just for performance considerations
s_time = time.time()


#environment variable pointing to the google vision api json keyfile 
os.environ['GOOGLE_APPLICATION_CREDENTIALS']='/path/to/Credentials/'

DETECTION_TYPES = [
    'TYPE_UNSPECIFIED',
    'FACE_DETECTION',
    'LANDMARK_DETECTION',
    'LOGO_DETECTION',
    'LABEL_DETECTION'
    'TEXT_DETECTION'
    'SAFE_SEARCH_DETECTION',]

    
def scene_analyse():

    s1_time = time.time()

    #read image file from directory
    with open('/path/to/the/photo.jpg','rb') as f:
        content = f.read()
		
    #send image to google cloud as a string buffer 
    image = types.Image(content=content)
    client = vision.ImageAnnotatorClient()
    response = client.label_detection(image=image)
    labels = response.label_annotations
    #print all the results
    print('Labels:')
    l=[]
    for label in labels:
        print(label.description)
        l.append(label.description)
	#convert all the results to a single string
    lin="\n".join(l)
	#write the results in a text file
    with open("/path/to/scene_results.txt","w") as f:
      f.write(lin)
    print('file generated')
	#print elapsed time and time taken to complete this frame
    elapsed_time = time.time()-s_time
    #freal_time = time.time()-s1_time
    print('Elapsed time:',elapsed_time,'sec')
    #print('This Frame', freal_time,'sec')
    
    print('------------------------')
    

   
#call the function
scene_analyse()
