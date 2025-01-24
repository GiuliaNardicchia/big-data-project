
# Progetto - Big Data course (81932), University of Bologna

Giulia Nardicchia - [giulia.nardicchia@studio.unibo.it](mailto:giulia.nardicchia@studio.unibo.it)

---
### Datasets

I datasets usati per questo progetto sono scaricabili dalla seguente cartella di [OneDrive](https://liveunibo-my.sharepoint.com/:f:/g/personal/giulia_nardicchia_studio_unibo_it/Ei2686kRO3JFrY-4LnImGpwBtge9FRErDnIgvT2h2QB-Pg?e=VrufWl):
- [itineraries-sample02.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/Eev55wChE4ZEmziiu4ozDGQBZZEn3NNNhnpaof_O0sbtQw?e=2BCdKv) (*593 MB*)
- [itineraries-sample16.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/EfFlDDrsJKhJqLlg3P0CzDgBdFzKS32tefS4x4sNGV2drg?e=4IOvIa) (*4.63 GB*)
- [itineraries-sample33.csv](https://liveunibo-my.sharepoint.com/:x:/g/personal/giulia_nardicchia_studio_unibo_it/EakEQx8NAHFNpzvWU8vraMQB9ji3U9wcCSUCbimpfafAgA?e=s7pXmF) (*9.55 GB*)

### Organizzazione di file e cartelle
- `src/` contiene il codice sorgente ed è suddiviso in linguaggio di programmazione
  - `python/` include i seguenti notebook:
    - [data-exploration.ipynb](src/main/python/understand_dataset/data-exploration.ipynb): dedicato alla comprensione e all'analisi esplorativa dei dati
    - [main-notebook.ipynb](src/main/python/main-notebook.ipynb): implementa il *job* proposto, sia in versione non ottimizzata sia in versione ottimizzata
  - `scala/`
    - [Flight.scala](src/main/scala/Flight.scala)
    - [FlightParser.scala](src/main/scala/FlightParser.scala)
    - [MainApplication.scala](src/main/scala/MainApplication.scala)
    - `utils/`
      - [Commons.scala](src/main/scala/utils/Commons.scala)
      - [Config.scala](src/main/scala/utils/Config.scala)
      - [Distance.scala](src/main/scala/utils/Distance.scala)
- `history/` contiene la cronologia dell'esecuzione dei *job*
  - [README.md](history/README.md)
- `reports/` contiene report generati con Power BI
  - [README.md](reports/README.md)

### AWS
I seguenti *script* sono stati creati per gestire più facilmente le operazioni da effettuare a linea di comando. Sono stati eseguiti con la *shell* `Git Bash`.

Per avviare una nuova sessione su AWS, eseguire lo script [start_aws_session.sh](/aws/start_aws_session.sh). Verranno richieste le seguenti informazioni:
- `AWS Access Key ID` *AWS details > AWS CLI > aws_access_key_id*
- `AWS Secret Access Key` *AWS details > AWS CLI > aws_secret_access_key*
- `Default region name` *us-east-1*
- `Default output format` *json*
- `AWS Session Token` *AWS details > AWS CLI > aws_session_token*

```shell
./aws/start_aws_session.sh
```

Eseguire lo script [create_aws_cluster.sh](/aws/create_aws_cluster.sh) per avviare un cluster Amazon EMR utilizzando Hadoop e Spark, avente le seguenti caratteristiche:
- Versione di EMR: `emr-7.3.0`
- Applicazioni installate: `Hadoop` e `Spark`
- Gruppo di istanze:
  - `MASTER`: una istanza di tipo `m4.large`
  - `CORE`: 6 istanze di tipo `m4.large`

Una volta creato il cluster verrà visualizzato il `ClusterID` e lo script rimarrà in esecuzione fino a quando lo stato del Cluster non sarà passato da `STARTING` a `WAITING`, viene effettuato un controllo ogni 10 secondi. Al termine, verrà mostrato su console anche il `PublicDNS`, utile in seguito per l'`SSH Configuration`.
```shell
./aws/create_aws_cluster.sh
```