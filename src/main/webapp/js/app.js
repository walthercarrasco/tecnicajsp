/**
 * app.js - Utilidades compartidas
 * TEMA: JavaScript + Cookies
 */

// Leer cookie por nombre
function getCookie(nombre) {
    const nameEQ = nombre + '=';
    const cookies = document.cookie.split(';');
    for (let c of cookies) {
        c = c.trim();
        if (c.indexOf(nameEQ) === 0)
            return decodeURIComponent(c.substring(nameEQ.length));
    }
    return null;
}

// Escribir cookie
function setCookie(nombre, valor, dias) {
    const fecha = new Date();
    fecha.setTime(fecha.getTime() + (dias * 24 * 60 * 60 * 1000));
    document.cookie = nombre + '=' + encodeURIComponent(valor) +
                      '; expires=' + fecha.toUTCString() + '; path=/';
}
