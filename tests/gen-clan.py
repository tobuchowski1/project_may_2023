import random


random.seed(99999)

entry = '{{"numberOfPlayers": {player_num},"points": {points}}},'

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

file = open("in/clan-square-20k.json", "w")

json = """
{
  "groupCount": 1000,
  "clans": ["""

for i in range(1000):
    for j in range(20):
        player_num = 1000 - i 
        points = player_num
        json += entry.format(player_num=player_num,points=points)


json = json[:-1] + "\n]\n}"


file.write(json)
file.close()