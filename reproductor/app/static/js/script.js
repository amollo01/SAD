document.addEventListener('DOMContentLoaded', function () {
    
    var reproductorAudio = document.getElementById('audio-player');
    var llistaSongs = document.getElementById('song-list');
    var busqueda = document.getElementById('search-box');

    function updateSongList (songs) {

        llistaSongs.innerHTML = '';

        // afegim les cançons a la llista
        for (var i = 0; i < songs.length; i++) { 
            var song = songs[i];
        
            //si el nom de la canço conte el text del buscador la mostrem
            if (song.toLowerCase().includes(busqueda.value.toLowerCase())) {
                var elementLlista = document.createElement('li');
                elementLlista.textContent = song;
                elementLlista.onclick = function () {
                    //repodruim la canço seleccionada
                    reproductorAudio.src = '/static/songs/' + song;
                    reproductorAudio.play();
                };
                llistaSongs.appendChild(elementLlista);
            }
        }
    }

    //solicitem les cançons al servidor
    fetch('/songs')
        .then(function(response) {
            return response.json();
        })
        .then(function(songs) {
            //actualiza la lista al escriure en el buscador
            busqueda.addEventListener('input', function() {
                updateSongList(songs);
            });
            //actualitzem llista al cargar la pagina
            updateSongList(songs);
        });
});
