import random

random.seed(99999)

def gen_unique(count: int):
    file = open(f"in/transaction-unique-{count}.json", "w")

    debit_account = 32309111922661937852684864
    credit_account = 16105023389842834748547303

    json = "["

    for i in range(count):
        json = json + f"""
        {{
            "debitAccount": "{debit_account + i}",
            "creditAccount": "{credit_account - i}",
            "amount": {random.randint(1,1000000)/100.0}
        }},"""
    
    json = json[:-1] + "\n]"
    file.write(json)
    file.close()

def gen_repeated(count: int):
    file = open(f"in/transaction-repeated-{count}.json", "w")
    json = "["

    for i in range(int(count/6)):
        json = json + sample_test_transfers

    json = json[:-1] + "\n]"
    file.write(json)
    file.close()
  
  
  
sample_test_transfers = """{
    "debitAccount": "32309111922661937852684864",
    "creditAccount": "06105023389842834748547303",
    "amount": 10.90
  },
  {
    "debitAccount": "31074318698137062235845814",
    "creditAccount": "66105036543749403346524547",
    "amount": 200.90
  },
  {
    "debitAccount": "66105036543749403346524547",
    "creditAccount": "32309111922661937852684864",
    "amount": 50.10
  },
  {
    "debitAccount": "32309111922661937852684864",
    "creditAccount": "06105023389842834748547303",
    "amount": 10.90
  },
  {
    "debitAccount": "31074318698137062235845814",
    "creditAccount": "66105036543749403346524547",
    "amount": 200.90
  },
  {
    "debitAccount": "66105036543749403346524547",
    "creditAccount": "32309111922661937852684864",
    "amount": 50.10
  },"""
  
  
  
for c in (20_000, 100_000):
     gen_unique(c)
     gen_repeated(c)
     
gen_unique(10)