# sentiment-analysis-ETL

![PO-nJWCn38PtFuL76Eu564u5mO1WeaWCLGShCUZKn2cnq-hUdkJkWf38u4J-Vl-_irEZR2K686Sf8nRb3D0g3YD-tE_n3j_ukx0Qll1yJTatwNGXumMl1zfpg1DFxw__1l3cH9ydkbr5uvAnsEevYJ8U5epAud5Vd6FL8tZ-6mCiDjcCpsASXGr2IcUa431MDNqnkYHQ8dl](https://user-images.githubusercontent.com/68533862/216821372-67c37c5d-254b-4cad-be61-040ec5088784.png)

<details>
<summary>plantUML</summary>
<div markdown="1">
  
```
@startuml
actor  user

user -> WAS : Request Keyword
activate WAS       
WAS --> ETL: Request Keyword (kafka)
WAS -> user : Response [kafka send Success]
deactivate WAS
entity Internet
loop all message consumed
  ETL->Internet : crawl Request
  Internet ->ETL: crawl Response
  ETL->Sentiment : sentence
  Sentiment ->ETL : sentiment analysis result
  ETL -> ETL : save result [DB]
end
@enduml
```
</div>
</details>
  
  


# Server
[Front](https://github.com/jhong92-pro/sentiment-front) : vanillaJS  
[WAS](https://github.com/jhong92-pro/sentiment-was) : Springboot, kafka  
ETL : Springboot, kafka  
[NLP](https://github.com/jhong92-pro/sentiment-backend) : FastAPI, NLP  
[Batch](https://github.com/jhong92-pro/sentiment-crawl-batch) : Spring-batch (crawl website addresses)  

# ISSUE
```
Session ID: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  
Caused by: org.openqa.selenium.NoSuchSessionException: invalid session id
```
