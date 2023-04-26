# Bitcoin Wallet Balance History
_Anymind's technical assignment_

**Assumption:**
The system allows adding past transactions as real-time operations arrival is not implied by the task

### Running the app
Run from the project's root:
```bash
docker-compose up -d
```

### Request examples
#### Save record:
```bash
curl -d '{"datetime":"2019-10-05T14:45:05+07:00","amount":1.1}' -H "Content-Type: application/json" -X POST http://localhost:8080/transaction
```

#### Get history of the wallet balance 
Balance history at the end of each hour **between** two dates.
Each exact hour **between** provided two dates (inclusively) is counted.
```bash
curl -d '{"startDatetime": "2019-10-05T12:48:01+07:00", "endDatetime": "2019-10-05T17:48:02+07:00"}' -H "Content-Type: application/json" -X GET http://localhost:8080/balanceHourly
```