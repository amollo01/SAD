from flask import render_template, jsonify
import os
from app import app

#ruta per llistar cançons
@app.route('/songs')
def list_songs():
    
    directori = 'app/static/songs'
    llistaSongs = []

    for fixer in os.listdir(directori):
        if fixer.endswith('.mp3'): #comprovem que el archiu sigui mp3
            llistaSongs.append(fixer) 

    return jsonify(llistaSongs) #retorna la llista de cançons en format json

#ruta pagina principal
@app.route('/')
def index():
    return render_template('index.html')


