document.addEventListener('DOMContentLoaded', function () {
    
    var reproductorAudio = document.getElementById('reproductor_musica');
    var llistaMusica = document.getElementById('llista_musica');
    var busqueda = document.getElementById('barra_busqueda');

    function actulitzaLlista (songs) {

        llistaMusica.innerHTML = '';

        // afegim les cançons a la llista
        for (var i = 0; i < songs.length; i++) {
            (function () {
                var song = songs[i];
        
                // Mostrem les cançons que el seu nom coincideix amb l'escrtit al bucador
                if (song.toLowerCase().includes(busqueda.value.toLowerCase())) {
                    var elementLlista = document.createElement('li');
                    var nomcanco = song.replace(/\.[^/.]+$/, ""); //Eliminar l'extensio de l'arxiu
                    elementLlista.textContent = nomcanco ;
                    elementLlista.onclick = function () {
                        // Reproduim la canço
                        reproductorAudio.src = '/static/songs/' + song;
                        reproductorAudio.play();
                    };
                    llistaMusica.appendChild(elementLlista);
                }
            })();
        }
    }

    //solicitem les cançons al servidor
    fetch('/songs')
        .then(function(response) {
            return response.json();
        })
        .then(function(songs) {
            //actualiza la lista al pujar un nova canço 
            busqueda.addEventListener('input', function() {
                actulitzaLlista(songs);
            });
            actulitzaLlista(songs);
        });
});
