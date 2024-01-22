const cacheName = 'DeskClockLite-v7';
const BASE_PATH = "/deskclocklite"
var appShellFiles = [
    BASE_PATH + '/',
    BASE_PATH + '/index.html',
    BASE_PATH + '/serviceWorker.js',
    BASE_PATH + '/favicon.ico',
    BASE_PATH + '/favicon-16x16.png',
    BASE_PATH + '/favicon-32x32.png',
]

this.addEventListener('install', function (event) {
    console.log('[Service Worker] Install', event);
    event.waitUntil(
        caches.open(cacheName).then(function (cache) {
            console.log('[Service Worker] Caching all: app shell and content');
            return cache.addAll(appShellFiles);
        })
    );
});

this.addEventListener('fetch', function (event) {
    event.respondWith(
        caches.match(event.request).then(function (r) {
            console.log('[Service Worker] Fetching resource: ' + event.request.url);
            return r || fetch(event.request)
        })
    );
});

self.addEventListener('activate', function (event) {
    event.waitUntil(
        caches.keys().then(function (keyList) {
            return Promise.all(keyList.map(function (key) {
                if (cacheName !== key) {
                    return caches.delete(key);
                }
            }));
        })
    );
});