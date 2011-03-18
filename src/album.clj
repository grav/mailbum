(import '(java.io File))
(import '(java.net URL))

(import '(com.google.gdata.data PlainTextConstruct))
(import '(com.google.gdata.client.photos PicasawebService))
(import '(com.google.gdata.data.photos AlbumEntry UserFeed))

;(import ('com.google.gdata.client.*))
;(import ('com.google.gdata.client.photos.*))
;(import ('com.google.gdata.data.*))
;(import ('com.google.gdata.data.media.*))

(def username "mikkelg")
(def usermail (apply str (concat username "@gmail.com")))
(def password "")

(def myService (PicasawebService. "albumMaker"));
(. myService setUserCredentials usermail password )

(def feedUrlStr
  (apply str
    (concat
      "https://picasaweb.google.com/data/feed/api/user/" username "?kind=album")))

(def postUrlStr
  (apply str
    (concat
      "https://picasaweb.google.com/data/feed/api/user/" username)))

(def postUrl (URL. postUrlStr ))

(def feedUrl (URL. feedUrlStr ))
(def myUserFeed (. myService getFeed feedUrl UserFeed))

;(map #(.. % getTitle getPlainText) (. myUserFeed getAlbumEntries))

(def myAlbum (AlbumEntry.))
(. myAlbum setTitle (PlainTextConstruct. "Trip to France"))
(. myAlbum setDescription (PlainTextConstruct. "My recent trip to France was delightful!"))
(def insertedEntry (. myService insert postUrl myAlbum ))

