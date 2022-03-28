from email.message import EmailMessage
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from pydoc import plain
import face_recognition
import cv2
import pyttsx3
import datetime
from playsound import playsound
import smtplib
import imghdr
import os
import time
from time import sleep
import numpy as np
from sendgrid.helpers.mail import Mail, Email, To, Content
import email.mime
import keyboard
from pynput.keyboard import Key, Listener
import keyboard
import speech_recognition as sr
from email.message import EmailMessage
import pyrebase
    
def search_for_commer(code):
    firebaseConfig = {
      "apiKey": "AIzaSyBwL_X-IA1QArKx2U51lJ9hB7bLeBz5Dco",
      "authDomain": "security-system-739fc.firebaseapp.com",
      "databaseURL": "https://security-system-739fc-default-rtdb.firebaseio.com",
      "projectId": "security-system-739fc",
      "storageBucket": "security-system-739fc.appspot.com",
      "messagingSenderId": "906327188388",
      "appId": "1:906327188388:web:0b32da1793ec991cf2d731",
      "measurementId": "G-82DF75PCC7",
      "serviceAccount": "serviceAccountKey.json"
    }

    firebase_storage = pyrebase.initialize_app(firebaseConfig)
    storage = firebase_storage.storage()

    image_name = code+".jpg"

    print("Image name is : " + image_name)
    
    all_files = storage.list_files()
    
    for file in all_files:
        if(file.name == image_name):
            storage.child(image_name).download(filename = "firebase_image.jpg", path = os.path.basename(image_name))
            return 1

    return -1

def greeting():
    curr_time = datetime.datetime.now()
    curr_time = str(curr_time.hour).zfill(2)+str(curr_time.minute).zfill(2)
    if(curr_time > '0000' and curr_time <= '1159'):
        engine = pyttsx3.init()
        engine.say("good morning")
        engine.runAndWait()
    elif(curr_time <= '1700'):
        engine = pyttsx3.init()
        engine.say("good afternoon")
        engine.runAndWait()
    else:
        engine = pyttsx3.init()
        engine.say("good evening")
        engine.runAndWait()

def capture_image():
    videoCaptureObject = cv2.VideoCapture(0)
    result = True
    while(result):
        ret, frame = videoCaptureObject.read()
        cv2.imwrite("./Visitor/visitor.jpg", frame)
        result = False
    videoCaptureObject.release()
    cv2.destroyAllWindows()

def takeInfo(code):
    firebaseConfig = {
      "apiKey": "AIzaSyBwL_X-IA1QArKx2U51lJ9hB7bLeBz5Dco",
      "authDomain": "security-system-739fc.firebaseapp.com",
      "databaseURL": "https://security-system-739fc-default-rtdb.firebaseio.com",
      "projectId": "security-system-739fc",
      "storageBucket": "security-system-739fc.appspot.com",
      "messagingSenderId": "906327188388",
      "appId": "1:906327188388:web:0b32da1793ec991cf2d731",
      "measurementId": "G-82DF75PCC7",
      "serviceAccount": "serviceAccountKey.json"
    }
    
    print('Code is : "{}"'.format(code))
    
    firebase = pyrebase.initialize_app(firebaseConfig)
    db = firebase.database()
    
    value = db.child("Visitors").child(code).child("Name").get()
    name = value.val()
    
    value = db.child("Visitors").child(code).child("Want to meet").get()
    meetingWith = value.val()
    
    print(name)
    print(meetingWith)
    
    return [name,meetingWith];

def email(name, meetingWith):
    
    #Sender_Email = meetingWith + "commoncode@gmail.com"
    
    Sender_Email = "semesterfourproject@gmail.com"
    Reciever_Email = "semesterfourproject@gmail.com"
    Password = 'aparrbnjdemjybmx'

    newMessage = EmailMessage()                         
    newMessage['Subject'] = "Meeting alert!" 
    newMessage['From'] = Sender_Email                   
    newMessage['To'] = Reciever_Email     
    newMessage.set_content('"{}" came and wants to meet you'.format(name))
    with open('./Visitor/visitor.jpg', 'rb') as f:
        image_data = f.read()
        image_type = imghdr.what(f.name)
        image_name = f.name

    newMessage.add_attachment(image_data, maintype='image', subtype=image_type, filename=image_name)

    with smtplib.SMTP_SSL('smtp.gmail.com', 465) as smtp:

        smtp.login(Sender_Email, Password)              
        smtp.send_message(newMessage)

def recog():
    code = input("Enter your code: ")
    name = input("Enter your name: ")
    
    ret = search_for_commer(code)
    
    if(ret==-1):
        engine = pyttsx3.init()
        engine.say("You haven't take apointment please take appointment first")
        engine.runAndWait()
        return False
    
    try:
        train_image = 'firebase_image.jpg'
        imgTest = face_recognition.load_image_file('./Visitor/visitor.jpg')
        imgTest = cv2.cvtColor(imgTest,cv2.COLOR_BGR2RGB)
        encodeTest = face_recognition.face_encodings(imgTest)[0]

        imgTrain = face_recognition.load_image_file(train_image)
        encodeTrain = face_recognition.face_encodings(imgTrain)[0]
        result = face_recognition.compare_faces([encodeTrain],encodeTest)
        faceDis = face_recognition.face_distance([encodeTrain],encodeTest)
        matchIndex = np.argmin(faceDis)
    
        if result[matchIndex]:
            engine = pyttsx3.init()
            engine.say("Yes you have appointment please wait")
            engine.runAndWait()
            info = takeInfo(code)
            email(info[0], info[1])
            return True
        
        else:
            engine = pyttsx3.init()
            engine.say("Your code is wrong")
            engine.runAndWait()
            return False
    
    except IndexError as e:
        print(e)
        engine = pyttsx3.init()
        engine.say("Please come in frame")
        engine.runAndWait()
        return False

while True:
    keyboard.wait("e")
    capture_image()
    greeting()
    name = recog()
    print(name)
    
