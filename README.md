# TablutProject - Partecipare è importante
Progetto di Intelligenza Artificiale (IA) - 1^ anno magistrale

L'intero sviluppo dell'IA è contenuto nel package IAPlayer. Abbiamo comunque tenuto nel progetto il server per usufruire delle classi già realizzate per rappresentare il gioco.

La classe PartecipareEImportante, situata appunto nel package IAPlayer, contiene il main del nostro giocatore. Essa lancerà un'istanza della classe PartecipareEImportante (che estende la classe TablutClient) e inizierà a scambiare i messaggi con il server per giocare alla partita. Al lancio di questa classe vanno passati il parametro riguardante il giocatore (una stringa "white"/"black") e il numero di secondi concessi per la mossa (il progetto prevede già qualche secondo di "scarto" per evitare problemi di sincronizzazione). In più, per comodità, esistono le classi PartecipareEImportanteNero e PartecipareEImportanteBianco che permettono di lanciare il main con già i parametri definiti (al momento le classi contengono 40 secondi per mossa come parametro).

L'unica libreria di cui si fa uso in questo progetto è la "gson-2.2.2.jar" la quale dovrebbe essere già disponibile nel progetto. Nel caso ci fossero problemi saremo reperibili per correggere il tutto al più presto.
