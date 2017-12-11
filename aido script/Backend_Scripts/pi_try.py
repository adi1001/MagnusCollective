#!/usr/bin/env python

import rospy
import copy
from std_msgs.msg import String
from pi_trees_ros.pi_trees_ros import *
from aido_example_1.srv import *
import json
import os
import collections
import time
from messagehandle import Message

json_object = None
exemptedprops = ["type"]
datadir = "/home/viki/brain/test/data/"
databrain = {}
count =0
increase=True
PREV_TEMPERATURE=25
THRESHOLD = 0
ansyes = True
class CreateBrain():
	def __init__(self):
		global count
		### init JSON
		jsonpath = open("/home/viki/brain/playvideo/brain.json").read()
		json_object = json.loads(jsonpath,object_pairs_hook=collections.OrderedDict)

		rospy.init_node("brain")
		rospy.on_shutdown(self.shutdown)
		#setup_task_environment(self)

 		root = ParallelAll("SOUL")


		prevnode = root
		node= None
		typeoperation = -1

		#leaf1 = MonitorTask("leaf1", "aidopub1", String, self.perform_task)

		rospy.Subscriber("tolinux2",String,self.hearing_callback)
		rospy.Subscriber("face",String,self.face_callback)


		self.create_tree(root,"ParallelAll",json_object)

		print "Brain Tree"
		print_tree(root)
		#sleep(2)
		# Run the tree
		#while not rospy.is_shutdown():
		root.run()
		#	count+=1
		#	rospy.sleep(1)


	def create_tree(self,rootnode, typeofnode,*jsonobj):
		global exemptedprops
		for node in jsonobj[0]:
			if str(node) in exemptedprops:
				print "TYPE IS PRESENT"
				continue
			typeofnode =  "Sequence"
			if("type" in  jsonobj[0][node]):
				typeofnode =  jsonobj[0][node]["type"]
			print "creating node :", node, ":", typeofnode, "\n"
			print "subnode : ", jsonobj[0][node], "\n"
			newparent = self.add_child(rootnode,typeofnode,node)
			#ifjsonobj[0][node]) > 0):
			self.create_tree(newparent, typeofnode, jsonobj[0][node])


		


	def add_child(self,prevnode,typeofnode,nameofnode):
		name = str(nameofnode)
		if(typeofnode == "Sequence"):
			node = Sequence("" + name)
		if(typeofnode == "Selector"):
			node = Selector("" + name)
		if(typeofnode == "Loop"):
			node = Loop("" + name,iterations=700)
		if(typeofnode == "Iterator"):
			node = Iterator("" + name)
		if(typeofnode=="CallbackTask"):
			node = CallbackTask(name,self.callbackex,[name])
			#MonitorTask("leaf" + str(nodeid) , "aidopub1", String, self.perform_task)
		if(typeofnode == "ParallelAll"):
			node = ParallelAll("P_" + name)
		if(typeofnode == "IgnoreFailure"):
			node = IgnoreFailure("I_" + name)

		prevnode.add_child(node)
		return node
		
	def callbackex(self, node):
		global count
		count +=1
		rospy.loginfo("Calling function (" + node + ")")
		status = eval("self." + node+ "(\"" + node + "\")")
		return status



	#### NODESs

#### PLAY MOVIE

	def openmediaplayer(self, nodename):
		if(self.get_node_value(nodename) != "0"):
			return TaskStatus.SUCCESS
		aidohear=self.get_node_value("hearing")
		self.set_node_value("hearing","0")

		if("movie" in aidohear):
			self.set_node_value("speech","Do you want head or body projector?")
			self.set_node_value(nodename,"active")
			return TaskStatus.SUCCESS 
		else:
			self.set_node_value(nodename, "0")
			return TaskStatus.FAILURE


	def checkprojectortype(self,nodename):
		aidohear=self.get_node_value("hearing")

		if("body" in aidohear):
			self.send_message_to_android("MEDIA_BODY", "open")
			self.set_node_value("hearing", "0")
			return TaskStatus.SUCCESS	
		if("head" in aidohear):
			self.send_message_to_android("MEDIA_HEAD", "open")
			self.set_node_value("hearing", "0")
			return TaskStatus.SUCCESS
		
	
		return TaskStatus.FAILURE	



#### FACE DETECTION

	def face_callback(self, data):
		if(str(data.data) != ""):
			message = Message(str(data.data))
			if("DETECT" in message.getType()):	
				rospy.loginfo("AIDO DETECTED FACE : " + message.getValue())
				self.set_node_value("detectface", "yes")
			elif("RECOG" in   message.getType()):
				rospy.loginfo("AIDO RECOGISED FACE of : " +  message.getValue())
				self.set_node_value("recogniseface", message.getValue())
			else:
				self.set_node_value("recogniseface", "0")
					
				
	def detectface(self, nodename):
		return TaskStatus.SUCCESS
	
	def recogniseface(self, nodename):
		return TaskStatus.SUCCESS
	
	
	def sayhello(self, nodename):
		if(self.get_node_value("detectface")=="yes"):
			rospy.loginfo("AIDO DETECTED FACE")

			self.set_node_value("speech", "Hello how are you doing?")

		if(self.get_node_value("recogniseface") != "0"):
			rospy.loginfo("AIDO SAYING HELLO TO  " +  self.get_node_value("recogniseface"))
			self.set_node_value("speech","Hi " + self.get_node_value("recogniseface") + ". How are you doing?")
		return TaskStatus.SUCCESS

	def showemail(self, nodename):
		rospy.loginfo("AIDO SHOWING EMAIL: ")
		return TaskStatus.SUCCESS


#### TEMPERATURE


	def temparature(self, nodename):
		global PREV_TEMPERATURE, increase, THRESHOLD
		temp = int(self.get_node_value("temparature"))

		if(temp == 0):
			PREV_TEMPERATURE = 25
			temp=25		
		
		if(PREV_TEMPERATURE - temp >= 4):
			increase=False
		elif(PREV_TEMPERATURE - temp < -4):
			increase=True

		if(increase):
			temp+=1
		else:
			temp-=1
	
		PREV_TEMPERATURE = temp
		

		

		self.set_node_value(nodename,temp)
		rospy.loginfo(nodename + " is: " + str(temp))

		time.sleep(.1)

		return TaskStatus.SUCCESS

	def checkroomtemparature(self, nodename):
		temp = int(self.get_node_value("temparature"))
		if(temp <=20 -THRESHOLD and temp >2):
			self.set_node_value("adjusttemparature","raise")
			return TaskStatus.SUCCESS
		elif(temp >=30 + THRESHOLD):
			self.set_node_value("adjusttemparature","lower")
			return TaskStatus.SUCCESS
		else:
			self.set_node_value("adjusttemparature","none")			

		#rospy.loginfo("Setting adjusttemparature : " + self.get_node_value("adjusttemparature"))

		return TaskStatus.FAILURE
		
	def askfortemparatureadjust(self, nodename):
		global THRESHOLD
		adjust = self.get_node_value("adjusttemparature")
		speechval = self.get_node_value("speech")
		if(adjust=="none"):
			return TaskStatus.FAILURE
		if(speechval =="0"):
			self.set_node_value("speech","Should I " + adjust + " Temperature?")
			return TaskStatus.FAILURE				
		else:
			#self.set_node_value("speech", "0")
			hearval = self.get_node_value("hearing")

			rospy.loginfo("I HEARD (RAW) : " + hearval)

			if("yes" not in hearval and "no" not in hearval):
				#self.set_node_value("speech","Should I " + adjust + " Temperature?")
				rospy.loginfo("I HEARD WRONG : " + hearval)
				
				return TaskStatus.FAILURE
			if("yes" in hearval):
				self.set_node_value("speech","I heard YES. So I am changing the temperature")
				#self.set_node_value("speech","0")

				rospy.loginfo("I HEARD : " + hearval)
				return TaskStatus.SUCCESS
			else:
				self.set_node_value("speech","I heard NO. So I am NOT changing the temperature")
				#self.set_node_value("speech","0")

				rospy.loginfo("I HEARD : " + hearval)
				rospy.loginfo("INCREASING THRESHOLD.. will wait for more extreme temperature : " + hearval)				
				THRESHOLD += 8
				return TaskStatus.FAILURE
					
			return TaskStatus.FAILURE
		return TaskStatus.FAILURE
	
	def adjusttemparature(self, nodename):
		global THRESHOLD
		rospy.loginfo("Reseting temparature to 25 & Threshold to normal")				
		
		self.set_node_value("temparature","25")
	 	threshold = 0	
		return  TaskStatus.SUCCESS

	def message_to_android_callback(req):
		return AidoExample1Response(req.a)



		
##Text to speech module
	def speech(self, nodename):
		speechtext = self.get_node_value(nodename)
		if(speechtext != "0" and speechtext != "listening"):
			#self.set_node_value(nodename,"0")
			rospy.loginfo("AIDO SPEECH : " + speechtext)
			rospy.wait_for_service('aidoservice2')
			try:
				servicecall = rospy.ServiceProxy('aidoservice2', AidoExample1)
				res = servicecall(str(count) + "|TEXT_TO_SPEECH|" + speechtext)
				self.set_node_value(nodename,"listening")
				self.set_node_value("hearing","")
			except rospy.ServiceException, e:
				print "Service text to speech failed %s" %e
			return TaskStatus.SUCCESS
		else:
			return TaskStatus.SUCCESS
			
	def hearing_callback(self, data):
		if(str(data.data) != ""):
			rospy.loginfo("AIDO HEARD : " + str(data.data))
			self.set_node_value("hearing", data.data)

	def hearing(self, nodename):
		global ansyes
		#heard="yes"
		#if(not ansyes):
		#	heard = "no"
		#	ansyes = True
		#else:
		#	heard = "yes"
		#	ansyes = False

		#rospy.loginfo("AIDO HEARING : ")
		#rospy.wait_for_service('aidoservice2')
		#try:
		#	servicecall = rospy.ServiceProxy('aidoservice2', AidoExample1)
		#	res = servicecall(str(count) + "|SPEECH_TO_TEXT|hear")
		#except rospy.ServiceException, e:
	#		print "Service speech to text failed %s" %e


		#if(self.get_node_value(nodename) == ""):
		#	return TaskStatus.RUNNING
		return TaskStatus.SUCCESS



	##### function definitions

	def send_message_to_android(self,messagetype,message):
		rospy.loginfo("Android Msg : " + message)
		rospy.wait_for_service('aidoservice2')
		try:
			servicecall = rospy.ServiceProxy('aidoservice2', AidoExample1)
			res = servicecall(str(count) + "|"+messagetype+"|" + message )
		except rospy.ServiceException, e:
			print "Service text to speech failed %s" %e
			return TaskStatus.FAILURE
		return TaskStatus.SUCCESS

	
	def set_node_value(self, nodename, nodevalue):
		databrain[nodename]=nodevalue
		#datapath = self.get_node_data_path(nodename)
		#self.write_into_file(datapath, nodevalue)

	def get_node_value(self, nodename):
		#datapath = self.get_node_data_path(nodename)
		#f=  open(datapatj, 'r')
		#contents = f.read()
		#f.close()
		if(databrain.has_key(nodename)):
			return databrain[nodename]
		else:
			return "0"
		#return contents		
			

	def write_into_file(self, filename, contents):
		print "\nwriting into file : ", filename
		f=open(filename,'w')
		f.write(contents)
		f.close()

	def get_node_data_path(self, nodename):
		global datadir
		return datadir + nodename
	

	def perform_task(self, msg, nameofnode):
		if(int(nameofnode.replace("leaf","")) > 1):
			rospy.loginfo("FORCED SUCCESS: " + nameofnode + ")" + str(msg.data))
			return TaskStatus.RUNNING			
		if (int(msg.data) % 7 == 0):
			rospy.loginfo("COMPLETED FAIL: " + nameofnode + ")" + str(msg.data))
			return TaskStatus.FAILURE
		if (int(msg.data) % 5 == 0):		
			rospy.loginfo("COMPLETED SUCCESS: " + nameofnode + ")" + str(msg.data))
			return TaskStatus.SUCCESS
		rospy.loginfo("RUNN: " + nameofnode + ")" + str(msg.data))
		return TaskStatus.RUNNING





	def shutdown(self):
		rospy.loginfo("Stopping the robot...")
		rospy.sleep(1)


if __name__ == '__main__':
	tree=CreateBrain()	
