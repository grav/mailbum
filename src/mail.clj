(import '(javax.mail Session URLName))

(defn parts [content]
  (let [count (. content getCount)]
    (map #(. content getBodyPart %) (range count))))

(defn image-name [contentType]
  (first
    (rest
      (first
        (re-seq #"(?i)name=(.*\.jpg)" contentType)))))

(defn image? [part]
  (not (nil? (re-seq #"(?i)image/jpeg;" (. part getContentType)))))

(defn multipart [m]
  (let [ctype (. m getContentType)]
   (not (empty? (re-seq #"multipart/MIXED;" ctype)))))

(defn urlenc [s]
  (java.net.URLEncoder/encode s "UTF-8"))

(defn image-parts [msg]
  (filter image? (parts (. msg getContent))))

(defn image-map [part]
  {:title (image-name (. part getContentType))
    :img (. part getContent)})

(defn img-mail? [msg]
  (and
    (multipart msg)
    (not
      (empty? (image-parts msg)))))

; "subj: img1, img2" description of a mail
(defn img-mail-desc [msg]
  (let [parts (parts (. msg getContent))]
    (apply str
      (concat
        (. msg getSubject)
        ": "
        (apply str
          (apply concat
            (map
              #(concat (image-name %) ", ")
              (map #(. % getContentType) parts))))))))


(defn store [email password]
  (let [url (apply str (concat "imaps://" (urlenc email) ":" (urlenc password) "@imap.gmail.com"))
        urlName (URLName. url)
        session (Session/getDefaultInstance (System/getProperties))]
    (. session getStore urlName)))

(defn image-messages [email password path]
  (let [myStore (store email password)]
    (dosync
      (. myStore connect))
      (let [myFolder (. myStore getFolder path)]
        (dosync
          (. myFolder open 1)
          (filter img-mail? (. myFolder getMessages ))))))

