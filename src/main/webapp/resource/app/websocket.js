/**
 * @Author alan.peng
 * Created by User on 2017/9/21.
 */

var websocket = {};
websocket.socket = null;
websocket.connect = (function(host) {
    if ('WebSocket' in window) {
        websocket.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        websocket.socket = new MozWebSocket(host);
    } else {
        setMessageInnerHTML('Error: WebSocket is not supported by this browser.');
        return;
    }

    websocket.socket.onopen = function () {
        setMessageInnerHTML('Info: WebSocket connection opened.');
        document.getElementById('text').onkeydown = function(event) {
            if (event.keyCode == 13) {
                websocket.sendMessage();
            }
        };
    };

    websocket.socket.onclose = function () {
        document.getElementById('websocket').onkeydown = null;
        setMessageInnerHTML('Info: WebSocket closed.');
    };

    websocket.socket.onmessage = function (message) {
        //Console.log(message.data);
        setMessageInnerHTML(message.data);
    };
});

function setMessageInnerHTML(innerHTML) {
    document.getElementById('message').innerHTML += innerHTML + '<br/>';
    var div = document.getElementById('message');
    div.scrollTop = div.scrollHeight;
}


function send() {
    var message = document.getElementById('text').value;
    websocket.socket.send(message);
    document.getElementById('text').value = '';
}

websocket.initialize = function() {

    if (window.location.protocol == 'http:') {
        websocket.connect('ws://' + window.location.host + '/websocket/' + sessionId);
    } else {
        websocket.connect('wss://' + window.location.host + '/websocket/' + sessionId);
    }
};

websocket.sendMessage = (function() {
    var message = document.getElementById('text').value;
    if (message != '') {
        websocket.socket.send(message);
        document.getElementById('text').value = '';
    }
});

var Console = {};

Console.log = (function(message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = message;
    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
});

websocket.initialize();


document.addEventListener("DOMContentLoaded", function() {
    // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
    var noscripts = document.getElementsByClassName("noscript");
    for (var i = 0; i < noscripts.length; i++) {
        noscripts[i].parentNode.removeChild(noscripts[i]);
    }
}, false);