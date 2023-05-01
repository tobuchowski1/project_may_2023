import random
from tqdm import tqdm


random.seed(99999)

def gen_file(max_atm_id: int, max_region: int, name: str):
    # Open a file for writing
    file = open(f"in/atm-{name}-1M.json", "w")

    request_types = ["STANDARD", "FAILURE_RESTART", "SIGNAL_LOW", "PRIORITY"]

    json = "["

    for i in tqdm(range(100_000)):
        region = random.randint(1,max_region)
        atm_id = random.randint(1,max_atm_id)
        json += f"""
  {{
    "region": {region},
    "requestType": "{random.choice(request_types)}",
    "atmId": {atm_id}
  }},"""


    json = json[:-1] + "\n]"


    file.write(json)
    file.close()
    
gen_file(9999, 9999, "random")
gen_file(100, 200, "random-dup")