from flask import Flask, render_template, jsonify, request, redirect
import os
from werkzeug.utils import secure_filename

CARPETA_PUJAR= 'app/static/songs'

app = Flask(__name__, static_folder='static')

app.config['CARPETA_PUJAR'] = CARPETA_PUJAR

def comprovacio_extensio(filename):
    return '.' in filename and filename.endswith('.mp3')

# Gestio pujada musica
@app.route('/upload', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        if 'song' not in request.files:
            return redirect('/')
        file = request.files['song']
        if file.filename == '' or not comprovacio_extensio(file.filename):
             return render_template('error.html', message='El arxiu ha de ser de tipus MP3.')
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['CARPETA_PUJAR'], filename))
        return redirect('/')

# Ruta per llistar cançons
@app.route('/songs')
def list_songs():
    directorio = 'app/static/songs'
    llista_musica = []

    for archivo in os.listdir(directorio):
        if archivo.endswith('.mp3'):  
            llista_musica.append(archivo)

    return jsonify(llista_musica)  # Retorna la lista de cancçons en format JSON

# Ruta per la página principal
@app.route('/')
def index():
    return render_template('index.html')

if __name__ == '__main__':
    app.run()