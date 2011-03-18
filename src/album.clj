(import '(java.io File))
(import '(java.net URL))

(import '(com.google.gdata.data PlainTextConstruct))
(import '(com.google.gdata.client.photos PicasawebService))
(import '(com.google.gdata.data.photos AlbumEntry PhotoEntry AlbumFeed UserFeed))
(import '(com.google.gdata.data.media MediaStreamSource))

;(import ('com.google.gdata.client.*))
;(import ('com.google.gdata.client.photos.*))
;(import ('com.google.gdata.data.*))
;(import ('com.google.gdata.data.media.*))

;(def postUrlStr
;  (apply str
;    (concat
;      "https://picasaweb.google.com/data/feed/api/user/" username)))
;
;(def postUrl (URL. postUrlStr ))
;(def feedUrl (URL. feedUrlStr ))

;(map #(.. % getTitle getPlainText) (. myUserFeed getAlbumEntries))

;(def myAlbum (AlbumEntry.))
;(. myAlbum setTitle (PlainTextConstruct. "mailbumtest"))
;(. myAlbum setDescription (PlainTextConstruct. "Test of the mailbum service"))
;(def insertedEntry (. myService insert postUrl myAlbum ))
;

(defn username [gmail]
  (first (rest (first (re-seq #"(.*)@(gmail.com|googlemail.com)" gmail)))))

(defn feedUrl [username]
  (URL. (apply str
    (concat
      "https://picasaweb.google.com/data/feed/api/user/" username "?kind=album"))))

(defn get-service [username password]
  (let [myService (PicasawebService. "mailbum")]
    (dosync
      (myService setUserCredentials usermail password)
      myService )))

(defn album-name [album]
  (.. album getTitle getPlainText))

(defn get-album [name feedUrl service]
  (let [userFeed (. service getFeed feedUrl UserFeed)]
    (find-first #(= (album-name %) name))))

(defn upload [img service album]
  (let [myMedia (MediaStreamSource. img "image/jpeg")
        feedUrl (. album getId)]
    (. service insert feedUrl PhotoEntry myMedia)))

