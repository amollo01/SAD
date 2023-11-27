document.addEventListener('DOMContentLoaded', function () { //quan la pàgina s'ha carregat (HTML i CSS)
    
    var reproductorAudio = document.getElementById('reproductor_musica'); //obtenim l'element reproductor
    var llistaMusica = document.getElementById('llista_musica');
    var busqueda = document.getElementById('barra_busqueda');

    function actulitzaLlista (songs) { //funció per a actualitzar la llista de cançons

        llistaMusica.innerHTML = '';

        // afegim les cançons a la llista
        for (var i = 0; i < songs.length; i++) { 
            (function () { 
                var song = songs[i];
        
                // Mostrem les cançons que el seu nom coincideix amb l'escrtit al bucador
                if (song.toLowerCase().includes(busqueda.value.toLowerCase())) {
                    var elementLlista = document.createElement('li'); //Creem un element de la llista
                    var nomcanco = song.replace(/\.[^/.]+$/, ""); //Eliminar l'extensio de l'arxiu
                    elementLlista.textContent = nomcanco ; //Afegim nom de canço al element de la llista
                    elementLlista.onclick = function () { //Quan cliquem al element, reproduim canço
                        
                        reproductorAudio.src = '/static/songs/' + song; //Establim la ruta de la canço
                        reproductorAudio.play();
                    };
                    llistaMusica.appendChild(elementLlista); //Afegim element a la llista
                }
            })();
        }
    }

    //solicitem les cançons al servidor
    fetch('/songs') //solicitud HTTP GET al servidor
        .then(function(response) { //quan el servidor respon
            return response.json(); //retornem la resposta en format JSON
        })
        .then(function(songs) { //quan el client rep la resposta
            busqueda.addEventListener('input', function() { // per a que s'actualitzi la llista al escriure
                actulitzaLlista(songs);
            });
            actulitzaLlista(songs); //mostrem incialment la llista
        });
});