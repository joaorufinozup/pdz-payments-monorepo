### Request
```
POST http://localhost:8080/payment/ HTTP/1.1
content-type: application/json

{
    "id_customer": "123",
    "id_subscription": "123",
    "value": 0.0
}
```
### Response
```
Status 200 { "message": "Pagamento efetuado com sucesso" }
Status 400 { "message": "Ocorreu um erro ao processar o pagamento" }
```
