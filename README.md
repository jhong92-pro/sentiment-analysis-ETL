# sentiment-analysis-ETL

![plantUML](https://user-images.githubusercontent.com/68533862/216350835-e09ff4b8-c1ad-4049-a5d7-f7ced8ebb082.png)
<details>
<summary>plantUML</summary>
<div markdown="1">
  
```
@startuml
actor  user

user -> WAS : Request Crawl
activate WAS       
WAS --> ETL: Request Crawl (kafka)
WAS -> user : Response [kafka send Success]
deactivate WAS
entity Internet
loop all message consumed
  ETL->Internet : crawl Request
  Internet ->ETL: crawl Response
  ETL->Sentiment : sentence
  Sentiment ->ETL : sentiment analysis result
end
@enduml
```
</div>
</details>
