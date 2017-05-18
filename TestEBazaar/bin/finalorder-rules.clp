(defclass finalorder-template business.rulesbeans.ShopCartBean)

(defmodule rules-finalorder)
(import java.util.*)

(deffunction keysNoGreaterThanValues(?map)
    (bind ?keys ((?map keySet) toArray))
    (foreach ?key ?keys
        (bind ?value (?map get ?key))
        (if ( > ?key ?value)
            then (return FALSE)
        )
    )
    (return TRUE)
)
(defrule req-not-more-than-avail
    (finalorder-template (requestedAvailableMap ?z) )
    (test (= (keysNoGreaterThanValues ?z) FALSE))
     =>    
    (throw (new rulesengine.ValidationException "One of your Orders requests more items than are currently in stock.")))
 
;(bind ?h (new HashMap))
;(?h put 10 8)  
;(?h put 9 9)
;(bind ?h2 (new HashMap))
;(?h2 put 10 8)  
;(?h2 put 9 11)
;(printout t "h passes test? " (valuesNoGreaterThanKeys ?h) crlf)
;(printout t "h2 passes test? " (valuesNoGreaterThanKeys ?h2) crlf)

