# sentiment-analysis-ETL

![NOwzJiGm48HxFyMKKEG5A2BnGu54HP0eJbUid055vpX7rZvqRy_QYIL4YjZQx_FiY4_B2cJbwbpve-lnSJ_W3c_ypjI4nrL-WZDnkaXYXTldohKp__dzzP-EcvDydkHsK_gQNoFrl4Gb3XL36KSCsNkg7jt8lqiSOvhI5IynSOrCBYpB6H82PhFbY](https://user-images.githubusercontent.com/68533862/216820580-6874d21c-7e6a-437a-a6d7-3f68b033cbe1.png)

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
  ETL -> ETL : save result [DB]
end
@enduml
```
</div>
</details>
