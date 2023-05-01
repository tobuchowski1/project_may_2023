import random


random.seed(99999)

# Open a file for writing
file = open("in/clan-random-20k.json", "w")

json = """
{
  "groupCount": 1000,
  "clans": ["""

for i in range(20_000):
    player_num = random.randint(1,400)
    json = json + f"""
    {{
        "numberOfPlayers": {player_num},
        "points": {player_num * random.randint(1,100) + random.randint(1,20)}
    }},"""


json = json[:-1] + "\n]\n}"


file.write(json)
file.close()